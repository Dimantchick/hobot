package tk.dimantchick.hobot.service;

import org.apache.commons.collections4.list.TreeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.invest.openapi.model.rest.Operation;
import ru.tinkoff.invest.openapi.model.rest.OperationTypeWithCommission;
import tk.dimantchick.hobot.api.Api;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.operations.HobotOperation;
import tk.dimantchick.hobot.repository.OperationsRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Сервис, постоянно получающий операции по активным инструментам.
 */
@Service
public class OperationsService {

    private Logger logger = LoggerFactory.getLogger(OperationsService.class);

    private final Api api;

    private final InstrumentsService instrumentsService;

    private final OperationsRepository operationsRepository;

    public OperationsService(Api api, InstrumentsService instrumentsService, OperationsRepository operationsRepository) {
        this.api = api;
        this.instrumentsService = instrumentsService;
        this.operationsRepository = operationsRepository;
    }

    @Transactional
    public void updateOperations() {
        OffsetDateTime yesterday = OffsetDateTime.now().minusDays(1);
        List<HobotOperation> toSave = new TreeList<>();
        for (Instrument instrument : instrumentsService.getActiveInstruments()) {
            List<Operation> operations = api.getOperations(instrument.getFigi(), yesterday).getOperations();
            for (Operation operation : operations) {
                if (operation.getOperationType() == OperationTypeWithCommission.BUY
                || operation.getOperationType() == OperationTypeWithCommission.SELL) {
                    Optional<HobotOperation> byId = operationsRepository.findById(operation.getId());
                    if (byId.isPresent()) {
                        HobotOperation operationFromDB = byId.get();
                        if (operationFromDB.isFilled()) {
                            continue;
                        }
                    }
                    HobotOperation hobotOperation = new HobotOperation(operation, instrument, null);
                    toSave.add(hobotOperation);
                }
            }
        }
        operationsRepository.saveAll(toSave);
        operationsRepository.deleteByOperationDateBeforeAndPositionIsNull(yesterday);
    }

    public Optional<HobotOperation> getById(String id) {
        return operationsRepository.findById(id);
    }

    public void save(HobotOperation operation) {
        operationsRepository.save(operation);
    }


}
