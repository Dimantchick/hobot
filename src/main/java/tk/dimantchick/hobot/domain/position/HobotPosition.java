package tk.dimantchick.hobot.domain.position;

import org.apache.commons.collections4.list.TreeList;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.tinkoff.invest.openapi.model.rest.OperationTypeWithCommission;
import ru.tinkoff.invest.openapi.model.rest.PlacedLimitOrder;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.operations.HobotOperation;
import tk.dimantchick.hobot.domain.position.utils.OrderConverter;
import tk.dimantchick.hobot.strategies.utils.StrategiesDatabaseConverter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

/**
 * Позиция для робота.
 */
@Entity
public class HobotPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull(message = "Укажите инструмент")
    private Instrument instrument;

    private PositionStatus status = PositionStatus.WAITING;
    private BigDecimal priceToBuy = BigDecimal.ZERO;
    private BigDecimal priceSL = BigDecimal.ZERO;

    private BigDecimal averageBuyPrice = BigDecimal.ZERO;
    private BigDecimal averageSellPrice = BigDecimal.ZERO;
    private BigDecimal averageProfit = BigDecimal.ZERO;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable
    private List<HobotOperation> operations = new TreeList<>();

    private int quantity;
    @Min(value = 0, message = "Должно быть >= 0")
    private BigDecimal maxPosition = BigDecimal.ZERO;
    private OffsetDateTime readyToBuyTime;

    @Convert(converter = StrategiesDatabaseConverter.class)
    @NotEmpty(message = "Укажите стратегии")
    private Set<String> buyStrategy;
    @Convert(converter = StrategiesDatabaseConverter.class)
    @NotEmpty(message = "Укажите стратегии")
    private Set<String> sellStrategy;
    @Convert(converter = OrderConverter.class)
    private PlacedLimitOrder placedLimitOrder;
    private boolean virtual = false; // не проводить реальных сделок

    private boolean restart = true; // Пересоздавать позицию после закрытия


    public BigDecimal countAverageBuyPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        int num = 0;
        for (HobotOperation operation : operations) {
            if (operation.getOperationType() == OperationTypeWithCommission.BUY) {
                sum = sum.add(operation.getPrice());
                num += operation.getQuantityExecuted();
            }
        }
        return num > 0 ? sum.divide(BigDecimal.valueOf(num), RoundingMode.HALF_DOWN) : BigDecimal.ZERO;
    }

    public BigDecimal countAverageSellPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        int num = 0;
        for (HobotOperation operation : operations) {
            if (operation.getOperationType() == OperationTypeWithCommission.SELL) {
                sum = sum.add(operation.getPrice());
                num += operation.getQuantityExecuted();
            }
        }
        return num > 0 ? sum.divide(BigDecimal.valueOf(num), RoundingMode.HALF_DOWN) : BigDecimal.ZERO;
    }

    public BigDecimal countAverageProfit() {
        BigDecimal sum = BigDecimal.ZERO;
        for (HobotOperation operation : operations) {
            if (operation.getOperationType() == OperationTypeWithCommission.BUY) {
                sum = sum.subtract(operation.getPrice()).add(operation.getCommission());
            } else if (operation.getOperationType() == OperationTypeWithCommission.SELL) {
                sum = sum.add(operation.getPrice()).add(operation.getCommission());
            }
        }
        if (quantity > 0) {
            sum = sum.add(getLastPrice().subtract(getAverageBuyPrice()).multiply(BigDecimal.valueOf(quantity)));
        }
        return sum;
    }

    public BigDecimal getAverageBuyPrice() {
        return averageBuyPrice;
    }

    public void setAverageBuyPrice(BigDecimal averageBuyPrice) {
        this.averageBuyPrice = averageBuyPrice;
    }

    public BigDecimal getAverageSellPrice() {
        return averageSellPrice;
    }

    public void setAverageSellPrice(BigDecimal averageSellPrice) {
        this.averageSellPrice = averageSellPrice;
    }

    public BigDecimal getAverageProfit() {
        averageProfit = countAverageProfit();
        return averageProfit;
    }

    public void setAverageProfit(BigDecimal averageProfit) {
        this.averageProfit = averageProfit;
    }

    public BigDecimal getLastPrice() {
        return instrument.getLastPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public PositionStatus getStatus() {
        return status;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }

    public BigDecimal getPriceToBuy() {
        return priceToBuy;
    }

    public void setPriceToBuy(BigDecimal priceToBuy) {
        this.priceToBuy = priceToBuy;
    }

    public BigDecimal getPriceSL() {
        return priceSL;
    }

    public void setPriceSL(BigDecimal priceSL) {
        this.priceSL = priceSL;
    }

    public List<HobotOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<HobotOperation> operations) {
        this.operations = operations;
    }

    public void addOperation(HobotOperation operation) {
        TreeList<HobotOperation> newList = new TreeList<>();
        newList.add(operation);
        newList.addAll(operations);
        operations = newList;
        averageBuyPrice = countAverageBuyPrice();
        averageSellPrice = countAverageSellPrice();
        averageProfit = countAverageProfit();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(BigDecimal maxPosition) {
        this.maxPosition = maxPosition;
    }

    public OffsetDateTime getReadyToBuyTime() {
        return readyToBuyTime;
    }

    public void setReadyToBuyTime(OffsetDateTime readyToBuyTime) {
        this.readyToBuyTime = readyToBuyTime;
    }

    public Set<String> getBuyStrategy() {
        return buyStrategy;
    }

    public void setBuyStrategy(Set<String> buyStrategy) {
        this.buyStrategy = buyStrategy;
    }

    public Set<String> getSellStrategy() {
        return sellStrategy;
    }

    public void setSellStrategy(Set<String> sellStrategy) {
        this.sellStrategy = sellStrategy;
    }

    public PlacedLimitOrder getPlacedLimitOrder() {
        return placedLimitOrder;
    }

    public void setPlacedLimitOrder(PlacedLimitOrder placedLimitOrder) {
        this.placedLimitOrder = placedLimitOrder;
    }

    public boolean isRestart() {
        return restart;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    @Override
    public String toString() {
        return "HobotPosition{" +
                "id=" + id +
                ", instrument=" + instrument +
                ", status=" + status +
                ", priceToBuy=" + priceToBuy +
                ", priceSL=" + priceSL +
                ", operations=" + operations +
                ", quantity=" + quantity +
                ", maxPosition=" + maxPosition +
                ", readyToBuyTime=" + readyToBuyTime +
                ", buyStrategy=" + buyStrategy +
                ", sellStrategy=" + sellStrategy +
                ", placedLimitOrder=" + placedLimitOrder +
                ", restart=" + restart +
                '}';
    }
}
