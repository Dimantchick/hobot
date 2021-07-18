package tk.dimantchick.hobot.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;
import tk.dimantchick.hobot.domain.instrument.InstrumentsFilter;

/**
 * Класс спецификаций для поиска инструментов по критериям.
 */
public class InstrumentsSpecification {
    //currency
    public static Specification<Instrument> byCurrency(final Currency currency) {
        if (currency == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("currency"));
        } else {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("currency"), currency);
        }
    }

    //status
    public static Specification<Instrument> byStatus(final InstrumentStatus status) {
        if (status == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("status"));
        } else {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
        }
    }

    //custom String field
    public static Specification<Instrument> byFieldLike(final String name, final String value) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(name)), String.format("%%%S%%", value));
    }

    public static Specification<Instrument> byFilter(InstrumentsFilter filter) {
        return InstrumentsSpecification.byFieldLike("ticker", filter.getTicker()).
                and(InstrumentsSpecification.byFieldLike("name", filter.getName())).
                and(InstrumentsSpecification.byFieldLike("figi", filter.getFigi())).
                and(InstrumentsSpecification.byCurrency(filter.getCurrency())).
                and(InstrumentsSpecification.byStatus(filter.getStatus()));
    }
}
