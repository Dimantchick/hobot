package tk.dimantchick.hobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.model.rest.OperationStatus;
import ru.tinkoff.invest.openapi.model.rest.OperationType;
import ru.tinkoff.invest.openapi.model.rest.OperationTypeWithCommission;
import ru.tinkoff.invest.openapi.model.rest.PlacedLimitOrder;
import tk.dimantchick.hobot.api.Api;
import tk.dimantchick.hobot.domain.operations.HobotOperation;
import tk.dimantchick.hobot.domain.position.HobotPosition;
import tk.dimantchick.hobot.domain.position.PositionFilter;
import tk.dimantchick.hobot.domain.position.PositionStatus;
import tk.dimantchick.hobot.repository.PositionRepository;
import tk.dimantchick.hobot.repository.specifications.PositionsSpecification;
import tk.dimantchick.hobot.strategies.Strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Сервис, отвечающий за позиции.
 * Держит в памяти актуальный список активных позиций.
 */
@Service
public class PositionsService {

    private Logger logger = LoggerFactory.getLogger(PositionsService.class);

    private final PositionRepository positionRepository;

    private final InstrumentsService instrumentsService;

    private final StrategiesService strategiesService;

    private final OperationsService operationsService;

    private final Api api;


    private Set<HobotPosition> activePositions;

    public PositionsService(PositionRepository positionRepository, InstrumentsService instrumentsService, StrategiesService strategiesService, OperationsService operationsService, Api api) {
        this.positionRepository = positionRepository;
        this.instrumentsService = instrumentsService;
        this.strategiesService = strategiesService;
        this.operationsService = operationsService;
        this.api = api;
        updatePositions();
    }

    public synchronized Set<HobotPosition> getActivePositions() {
        if (activePositions == null) {
            updatePositions();
        }
        return activePositions != null ? activePositions : new HashSet<>();
    }

    public synchronized void updatePositions() {
        logger.debug("load active positions");
        activePositions = positionRepository.findByStatusNot(PositionStatus.CLOSED);
    }

    public synchronized void workOnPositions() {
//        logger.debug("workOnPositions");
        for (HobotPosition position : activePositions) {
            if (position.getLastPrice().equals(BigDecimal.ZERO)) {
                continue;
            }
            switch (position.getStatus()) {
                case CLOSED:
                    positionRepository.save(position);
                    activePositions.remove(position);
                    if (position.isRestart()) {
                        HobotPosition newPosition = new HobotPosition();
                        newPosition.setInstrument(position.getInstrument());
                        newPosition.setStatus(PositionStatus.READY);
                        newPosition.setMaxPosition(position.getMaxPosition());
                        newPosition.setBuyStrategy(position.getBuyStrategy());
                        newPosition.setSellStrategy(position.getSellStrategy());
                        newPosition.setVirtual(position.isVirtual());
                        newPosition.setRestart(position.isRestart());
                        newPosition(newPosition);
                    }
                    break;
                case WAITING:

                    break;
                case READY:
                case NEED_TO_BUY:
                    boolean buy = true;
                    Set<String> buyStrategy = position.getBuyStrategy();
                    for (String strategyName : buyStrategy) {
                        Strategy strategy = strategiesService.getStrategyByName(strategyName);
                        if (!strategy.isTimeToDoAction(position)) {
                            buy = false;
                        }
                    }
                    if (buy) {
                        int lotsToBuy = position.getMaxPosition().divide(position.getLastPrice(), RoundingMode.DOWN).intValue();
                        if (lotsToBuy > 0) {
                            if (!position.isVirtual()) {
                                logger.info("Placing buy order: buy " + lotsToBuy + " of " + position.getInstrument().getTicker());
                                placeBuyOrder(lotsToBuy, position);
                            } else {
                                //todo virtual buy
                                String id = "virt" + OffsetDateTime.now();
                                BigDecimal price = position.getLastPrice();
                                logger.info("Virtual buy: buy " + lotsToBuy + "x" + price + " of " + position.getInstrument().getTicker());
                                HobotOperation virtualOperation = new HobotOperation(id, position.getInstrument(), position, OperationStatus.DONE, BigDecimal.ZERO, position.getInstrument().getCurrency(),
                                        price.multiply(new BigDecimal(position.getQuantity())), price, position.getQuantity(), position.getQuantity(),
                                        OffsetDateTime.now(), OperationTypeWithCommission.BUY, true);
                                operationsService.save(virtualOperation);
                                position.addOperation(virtualOperation);
                                position.setQuantity(lotsToBuy);
                                position.setStatus(PositionStatus.BUYED);
                                positionRepository.save(position);

                            }
                        }
                    }
                    break;
                case BUYING:
                case SELLING:
                    fillOrder(position);
                    break;
                case BUYED:
                    boolean sell = false;
                    Set<String> sellStrategy = position.getSellStrategy();
                    for (String strategyName : sellStrategy) {
                        Strategy strategy = strategiesService.getStrategyByName(strategyName);
                        if (strategy.isTimeToDoAction(position)) {
                            sell = true;
                        }
                    }
                    if (sell) {
                        if (!position.isVirtual()) {
                            logger.info("Placing sell order: sell " + position.getQuantity() + " of " + position.getInstrument().getTicker());
                            placeSellOrder(position.getQuantity(), position);
                        } else {
                            //todo virtual sell
                            String id = "virt" + OffsetDateTime.now();
                            BigDecimal price = position.getLastPrice();
                            logger.info("Virtual sell: sell " + position.getQuantity() + "x" + price + " of " + position.getInstrument().getTicker());
                            HobotOperation virtualOperation = new HobotOperation(id, position.getInstrument(), position, OperationStatus.DONE, BigDecimal.ZERO, position.getInstrument().getCurrency(),
                                    price.multiply(new BigDecimal(position.getQuantity())), price, position.getQuantity(), position.getQuantity(),
                                    OffsetDateTime.now(), OperationTypeWithCommission.SELL, true);
                            operationsService.save(virtualOperation);
                            position.addOperation(virtualOperation);
                            position.setQuantity(0);
                            position.setStatus(PositionStatus.CLOSED);
                            positionRepository.save(position);
                        }
                    }
                    break;
            }
        }
    }

    private void placeBuyOrder(int lots, HobotPosition position) {
        final PlacedLimitOrder placedLimitOrder = api.placeLimitOrder(position.getInstrument().getFigi(), lots, position.getPriceToBuy(), OperationType.BUY);
        if (placedLimitOrder != null) {
            position.setStatus(PositionStatus.BUYING);
            position.setPlacedLimitOrder(placedLimitOrder);
            positionRepository.save(position);
        }
    }

    private void placeSellOrder(int lots, HobotPosition position) {
        final PlacedLimitOrder placedLimitOrder = api.placeLimitOrder(position.getInstrument().getFigi(), lots, position.getPriceToBuy(), OperationType.SELL);
        if (placedLimitOrder != null) {
            position.setStatus(PositionStatus.SELLING);
            position.setPlacedLimitOrder(placedLimitOrder);
            positionRepository.save(position);
        }
    }

    private void fillOrder(HobotPosition position) {
        PlacedLimitOrder placedLimitOrder = position.getPlacedLimitOrder();
        if (placedLimitOrder != null) {
            Optional<HobotOperation> oOperation = operationsService.getById(placedLimitOrder.getOrderId());
            if (oOperation.isPresent()) {
                HobotOperation operation = oOperation.get();
                if (operation.getStatus() != OperationStatus.DECLINE && operation.isFilled()) {
                    logger.info("Filling order for " + position.getInstrument().getTicker());
                    operation.setPosition(position);
                    position.addOperation(operation);
                    position.setPlacedLimitOrder(null);
                    switch (position.getStatus()) {
                        case BUYING:
                            position.setStatus(PositionStatus.BUYED);
                            position.setQuantity(position.getQuantity() + operation.getQuantityExecuted());
                            break;
                        case SELLING:
                            position.setQuantity(position.getQuantity() - operation.getQuantityExecuted());
                            if (position.getQuantity() == 0) {
                                position.setStatus(PositionStatus.CLOSED);
                            } else {
                                position.setStatus(PositionStatus.BUYED);
                            }
                            break;
                    }
                    positionRepository.save(position);
                    operationsService.save(operation);
                } else if (operation.getStatus() == OperationStatus.DECLINE) {
                    position.setPlacedLimitOrder(null);
                    switch (position.getStatus()) {
                        case BUYING:
                            position.setStatus(PositionStatus.NEED_TO_BUY);
                            break;
                        case SELLING:
                            position.setStatus(PositionStatus.BUYED);
                            break;
                    }
                }
            }
        }
    }

    public Optional<HobotPosition> getByID(Long id) {
        return positionRepository.findById(id);
    }

    public synchronized void newPosition(HobotPosition position) {
        logger.info("New " + position);
        positionRepository.save(position);
        instrumentsService.enable(position.getInstrument());
        updatePositions();
    }

    public synchronized void savePosition(HobotPosition position) {
        logger.info("Save " + position);
        positionRepository.save(position);
        updatePositions();
    }

    public synchronized void delete(HobotPosition position) {
        logger.info("Delete " + position);
        positionRepository.delete(position);
    }

    public Page<HobotPosition> findByFilter(PositionFilter filter, Pageable pageable) {
        return positionRepository.findAll(
                PositionsSpecification.byFilter(filter),
                pageable
        );
    }

    public long countByFilter(PositionFilter filter) {
        return positionRepository.count(
                PositionsSpecification.byFilter(filter)
        );
    }

}
