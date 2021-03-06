package tk.dimantchick.hobot.domain.position;

import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Фильтр для отбора позиций.
 */
public class PositionFilter {

    private static final List<String> sorts = Arrays.asList(new String[]{"id", "instrument_ticker", "status", "priceToBuy", "priceSL", "averageBuyPrice", "averageSellPrice", "averageProfit", "quantity", "maxPosition", "virtual", "restart"});

    private String ticker = "";
    private PositionStatus status;
    private BigDecimal priceToBuyLow = BigDecimal.ZERO;
    private BigDecimal priceToBuyHigh = BigDecimal.valueOf(1000000);
    private BigDecimal priceSLLow = BigDecimal.ZERO;
    private BigDecimal priceSLHigh = BigDecimal.valueOf(1000000);
    private BigDecimal averageBuyPriceLow = BigDecimal.ZERO;
    private BigDecimal averageBuyPriceHigh = BigDecimal.valueOf(1000000);
    private BigDecimal averageSellPriceLow = BigDecimal.ZERO;
    private BigDecimal averageSellPriceHigh = BigDecimal.valueOf(1000000);
    private BigDecimal averageProfitLow = BigDecimal.ZERO;
    private BigDecimal averageProfitHigh = BigDecimal.valueOf(1000000);
    private int quantityLow = 0;
    private int quantityHigh = 1000000;
    private BigDecimal maxPositionLow = BigDecimal.ZERO;
    private BigDecimal maxPositionHigh = BigDecimal.valueOf(1000000);
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

    public BigDecimal getAverageBuyPriceLow() {
        return averageBuyPriceLow;
    }

    public void setAverageBuyPriceLow(BigDecimal averageBuyPriceLow) {
        this.averageBuyPriceLow = averageBuyPriceLow;
    }

    public BigDecimal getAverageBuyPriceHigh() {
        return averageBuyPriceHigh;
    }

    public void setAverageBuyPriceHigh(BigDecimal averageBuyPriceHigh) {
        this.averageBuyPriceHigh = averageBuyPriceHigh;
    }

    public BigDecimal getAverageSellPriceLow() {
        return averageSellPriceLow;
    }

    public void setAverageSellPriceLow(BigDecimal averageSellPriceLow) {
        this.averageSellPriceLow = averageSellPriceLow;
    }

    public BigDecimal getAverageSellPriceHigh() {
        return averageSellPriceHigh;
    }

    public void setAverageSellPriceHigh(BigDecimal averageSellPriceHigh) {
        this.averageSellPriceHigh = averageSellPriceHigh;
    }

    public BigDecimal getAverageProfitLow() {
        return averageProfitLow;
    }

    public void setAverageProfitLow(BigDecimal averageProfitLow) {
        this.averageProfitLow = averageProfitLow;
    }

    public BigDecimal getAverageProfitHigh() {
        return averageProfitHigh;
    }

    public void setAverageProfitHigh(BigDecimal averageProfitHigh) {
        this.averageProfitHigh = averageProfitHigh;
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
                ", averageBuyPriceLow=" + averageBuyPriceLow +
                ", averageBuyPriceHigh=" + averageBuyPriceHigh +
                ", averageSellPriceLow=" + averageSellPriceLow +
                ", averageSellPriceHigh=" + averageSellPriceHigh +
                ", averageProfitLow=" + averageProfitLow +
                ", averageProfitHigh=" + averageProfitHigh +
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
