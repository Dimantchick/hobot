package tk.dimantchick.hobot.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import tk.dimantchick.hobot.domain.position.HobotPosition;
import tk.dimantchick.hobot.domain.position.PositionFilter;
import tk.dimantchick.hobot.domain.position.PositionStatus;

import java.math.BigDecimal;

/**
 * Класс спецификаций для поиска позиций по критериям.
 */
public class PositionsSpecification {
    //ticker
    public static Specification<HobotPosition> byTickerLike(final String ticker) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.upper(root.get("instrument").get("ticker")),
                String.format("%%%S%%", ticker));
    }

    //status
    public static Specification<HobotPosition> byStatus(final PositionStatus status) {
        if (status == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("status"));
        } else {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
        }
    }

    //field BigDecimal
    public static Specification<HobotPosition> byFieldGreaterThan(final String name, final BigDecimal low) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(name), low);
    }

    public static Specification<HobotPosition> byFieldLessThan(final String name, final BigDecimal high) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(name), high);
    }

    // field Integer
    public static Specification<HobotPosition> byFieldGreaterThan(final String name, final Integer low) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(name), low);
    }

    public static Specification<HobotPosition> byFieldLessThan(final String name, final Integer high) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(name), high);
    }

    //field Boolean
    public static Specification<HobotPosition> byFieldBoolean(final String name, final Boolean value) {
        if (value == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(name));
        } else {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(name), value);
        }
    }

    public static Specification<HobotPosition> byFilter(PositionFilter filter) {
        return PositionsSpecification.byTickerLike(filter.getTicker()).
                and(PositionsSpecification.byStatus(filter.getStatus())).
                and(PositionsSpecification.byFieldGreaterThan("priceToBuy", filter.getPriceToBuyLow())).
                and(PositionsSpecification.byFieldLessThan("priceToBuy", filter.getPriceToBuyHigh())).
                and(PositionsSpecification.byFieldGreaterThan("priceSL", filter.getPriceSLLow())).
                and(PositionsSpecification.byFieldLessThan("priceSL", filter.getPriceSLHigh())).
                and(PositionsSpecification.byFieldGreaterThan("averageBuyPrice", filter.getAverageBuyPriceLow())).
                and(PositionsSpecification.byFieldLessThan("averageBuyPrice", filter.getAverageBuyPriceHigh())).
                and(PositionsSpecification.byFieldGreaterThan("averageSellPrice", filter.getAverageSellPriceLow())).
                and(PositionsSpecification.byFieldLessThan("averageSellPrice", filter.getAverageSellPriceHigh())).
                and(PositionsSpecification.byFieldGreaterThan("averageProfit", filter.getAverageProfitLow())).
                and(PositionsSpecification.byFieldLessThan("averageProfit", filter.getAverageProfitHigh())).
                and(PositionsSpecification.byFieldGreaterThan("quantity", filter.getQuantityLow())).
                and(PositionsSpecification.byFieldLessThan("quantity", filter.getQuantityHigh())).
                and(PositionsSpecification.byFieldGreaterThan("maxPosition", filter.getMaxPositionLow())).
                and(PositionsSpecification.byFieldLessThan("maxPosition", filter.getMaxPositionHigh())).
                and(PositionsSpecification.byFieldBoolean("virtual", filter.getVirtual())).
                and(PositionsSpecification.byFieldBoolean("restart", filter.getRestart()));
    }
}
