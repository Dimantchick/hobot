package tk.dimantchick.hobot.domain.operations;

import ru.tinkoff.invest.openapi.model.rest.Currency;
import ru.tinkoff.invest.openapi.model.rest.OperationStatus;
import ru.tinkoff.invest.openapi.model.rest.OperationTypeWithCommission;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.position.HobotPosition;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Операция по инструменту.
 * В случае создания "виртуальной" операции, необходимо присвоить уникальный идентификатор.
 * Можно использовать за основу текущее время.
 */

@Entity
@Table(name = "OPERATIONS")
public class HobotOperation {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private HobotPosition position;

    private OperationStatus status;

    private BigDecimal commission;

    private Currency currency;

    private BigDecimal payment;

    private BigDecimal price;

    private Integer quantity;

    private Integer quantityExecuted;

    private OffsetDateTime operationDate;

    private OperationTypeWithCommission operationType;

    private boolean virtual = false;

    public HobotOperation() {
    }

    public HobotOperation(String id, Instrument instrument, HobotPosition position,
                          OperationStatus status, BigDecimal commission, Currency currency,
                          BigDecimal payment, BigDecimal price, Integer quantity,
                          Integer quantityExecuted, OffsetDateTime operationDate, OperationTypeWithCommission operationType,
                          boolean virtual) {
        this.id = id;
        this.instrument = instrument;
        this.position = position;
        this.status = status;
        this.commission = commission;
        this.currency = currency;
        this.payment = payment;
        this.price = price;
        this.quantity = quantity;
        this.quantityExecuted = quantityExecuted;
        this.operationDate = operationDate;
        this.operationType = operationType;
        this.virtual = virtual;
    }

    public HobotOperation(String id, Instrument instrument, HobotPosition position,
                          OperationStatus status, BigDecimal commission, Currency currency,
                          BigDecimal payment, BigDecimal price, Integer quantity,
                          Integer quantityExecuted, OffsetDateTime operationDate,
                          OperationTypeWithCommission operationType) {
        this.id = id;
        this.instrument = instrument;
        this.position = position;
        this.status = status;
        this.commission = commission;
        this.currency = currency;
        this.payment = payment;
        this.price = price;
        this.quantity = quantity;
        this.quantityExecuted = quantityExecuted;
        this.operationDate = operationDate;
        this.operationType = operationType;
    }

    public HobotOperation(ru.tinkoff.invest.openapi.model.rest.Operation operation,
                          Instrument instrument, HobotPosition position) {
        this.id = operation.getId();
        this.instrument = instrument;
        this.status = operation.getStatus();
        this.commission = operation.getCommission() != null ? operation.getCommission().getValue() : BigDecimal.ZERO;
        this.currency = operation.getCurrency();
        this.payment = operation.getPayment();
        this.price = operation.getPrice();
        this.quantity = operation.getQuantity();
        this.quantityExecuted = operation.getQuantityExecuted();
        this.operationDate = operation.getDate();
        this.operationType = operation.getOperationType();
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityExecuted() {
        return quantityExecuted;
    }

    public void setQuantityExecuted(Integer quantityExecuted) {
        this.quantityExecuted = quantityExecuted;
    }

    public OffsetDateTime getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(OffsetDateTime date) {
        this.operationDate = date;
    }

    public OperationTypeWithCommission getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypeWithCommission operationType) {
        this.operationType = operationType;
    }

    public boolean isFilled() {
        if (commission == null) {
            return false;
        }
        return !commission.equals(BigDecimal.ZERO);
    }

    public void setPosition(HobotPosition position) {
        this.position = position;
    }

    public HobotPosition getPosition() {
        return position;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }
}
