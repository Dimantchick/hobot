package tk.dimantchick.hobot.domain.instrument;

import ru.tinkoff.invest.openapi.model.rest.Currency;

/**
 * Фильтр для отбора инструментов.
 */
public class InstrumentsFilter {

    private int page = 1;
    private String ticker = "";
    private String name = "";
    private String figi = "";
    private Currency currency;
    private InstrumentStatus status = null;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFigi() {
        return figi;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public InstrumentStatus getStatus() {
        return status;
    }

    public void setStatus(InstrumentStatus status) {
        this.status = status;
    }

    public boolean isWithoutStatus() {
        return status == null;
    }

    public boolean isWithoutCurrency() {
        return currency == null;
    }

    @Override
    public String toString() {
        return "InstrumentsFilter{" +
                "page=" + page +
                ", ticker='" + ticker + '\'' +
                ", name='" + name + '\'' +
                ", figi='" + figi + '\'' +
                ", currency=" + currency +
                ", status=" + status +
                '}';
    }
}
