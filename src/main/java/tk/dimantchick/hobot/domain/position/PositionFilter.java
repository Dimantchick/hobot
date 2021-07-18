package tk.dimantchick.hobot.domain.position;

import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Фильтр для отбора позиций.
 */
public class PositionFilter {

    private static final List<String> sorts = Arrays.asList(new String[]{"id", "instrument_ticker", "status", "priceToBuy", "priceSL", "quantity", "maxPosition", "virtual", "restart"});

    private String ticker = "";
    private PositionStatus status;
    private BigDecimal priceToBuyLow = BigDecimal.ZERO;
    private BigDecimal priceToBuyHigh = BigDecimal.valueOf(Integer.MAX_VALUE);
    private BigDecimal priceSLLow = BigDecimal.ZERO;
    private BigDecimal priceSLHigh = BigDecimal.valueOf(Integer.MAX_VALUE);
    private int quantityLow = 0;
    private int quantityHigh = Integer.MAX_VALUE;
    private BigDecimal maxPositionLow = BigDecimal.ZERO;
    private BigDecimal maxPositionHigh = BigDecimal.valueOf(Integer.MAX_VALUE);
    private Boolean virtual;
    private Boolean restart;
    private int page = 1;
    private int onPage = 20;
    private String sort = "id";
    private Sort.Direction sortDirection = Sort.Direction.ASC;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public PositionStatus getStatus() {
        return status;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }

    public BigDecimal getPriceToBuyLow() {
        return priceToBuyLow;
    }

    public void setPriceToBuyLow(BigDecimal priceToBuyLow) {
        this.priceToBuyLow = priceToBuyLow;
    }

    public BigDecimal getPriceToBuyHigh() {
        return priceToBuyHigh;
    }

    public void setPriceToBuyHigh(BigDecimal priceToBuyHigh) {
        this.priceToBuyHigh = priceToBuyHigh;
    }

    public BigDecimal getPriceSLLow() {
        return priceSLLow;
    }

    public void setPriceSLLow(BigDecimal priceSLLow) {
        this.priceSLLow = priceSLLow;
    }

    public BigDecimal getPriceSLHigh() {
        return priceSLHigh;
    }

    public void setPriceSLHigh(BigDecimal priceSLHigh) {
        this.priceSLHigh = priceSLHigh;
    }

    public int getQuantityLow() {
        return quantityLow < 0 ? 0 : quantityLow;
    }

    public void setQuantityLow(int quantityLow) {
        this.quantityLow = quantityLow;
    }

    public int getQuantityHigh() {
        return quantityHigh;
    }

    public void setQuantityHigh(int quantityHigh) {
        this.quantityHigh = quantityHigh;
    }

    public BigDecimal getMaxPositionLow() {
        return maxPositionLow;
    }

    public void setMaxPositionLow(BigDecimal maxPositionLow) {
        this.maxPositionLow = maxPositionLow;
    }

    public BigDecimal getMaxPositionHigh() {
        return maxPositionHigh;
    }

    public void setMaxPositionHigh(BigDecimal maxPositionHigh) {
        this.maxPositionHigh = maxPositionHigh;
    }

    public Boolean getVirtual() {
        return virtual;
    }

    public void setVirtual(Boolean virtual) {
        this.virtual = virtual;
    }

    public Boolean getRestart() {
        return restart;
    }

    public void setRestart(Boolean restart) {
        this.restart = restart;
    }

    public int getOnPage() {
        return onPage;
    }

    public void setOnPage(int onPage) {
        this.onPage = onPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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

    public boolean isWithoutStatus() {
        return status == null;
    }

    public boolean isWithoutVirtual() {
        return virtual == null;
    }

    public boolean isWithoutRestart() {
        return restart == null;
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

    @Override
    public String toString() {
        return "PositionFilter{" +
                "ticker='" + ticker + '\'' +
                ", status=" + status +
                ", priceToBuyLow=" + priceToBuyLow +
                ", priceToBuyHigh=" + priceToBuyHigh +
                ", priceSLLow=" + priceSLLow +
                ", priceSLHigh=" + priceSLHigh +
                ", quantityLow=" + quantityLow +
                ", quantityHigh=" + quantityHigh +
                ", maxPositionLow=" + maxPositionLow +
                ", maxPositionHigh=" + maxPositionHigh +
                ", virtual=" + virtual +
                ", restart=" + restart +
                ", page=" + page +
                ", onPage=" + onPage +
                ", sort='" + sort + '\'' +
                ", sortDirection=" + sortDirection +
                '}';
    }
}
