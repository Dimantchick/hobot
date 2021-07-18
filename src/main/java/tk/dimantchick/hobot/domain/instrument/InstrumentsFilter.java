package tk.dimantchick.hobot.domain.instrument;

import org.springframework.data.domain.Sort;
import ru.tinkoff.invest.openapi.model.rest.Currency;

import java.util.Arrays;
import java.util.List;

/**
 * Фильтр для отбора инструментов.
 */
public class InstrumentsFilter {

    private static final List<String> sorts = Arrays.asList(new String[]{"ticker", "name", "figi", "currency", "status"});

    private String ticker = "";
    private String name = "";
    private String figi = "";
    private Currency currency;
    private InstrumentStatus status = null;
    private int page = 1;
    private int onPage = 20;
    private String sort = "ticker";
    private Sort.Direction sortDirection = Sort.Direction.ASC;

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

    public int getOnPage() {
        return onPage;
    }

    public void setOnPage(int onPage) {
        this.onPage = onPage;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
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
                ", onPage=" + onPage +
                ", sort='" + sort + '\'' +
                ", sortDirection=" + sortDirection +
                '}';
    }

    public void fix(int maxPage) {
        if (page > maxPage) {
            page = maxPage;
        }
        if (page < 1) {
            page = 1;
        }
        if (onPage < 1) {
            onPage = 1;
        }
        if (onPage > 1000) {
            onPage = 1000;
        }
        if (!sorts.contains(sort)) {
            sort = "id";
        }
    }
}
