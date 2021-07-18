package tk.dimantchick.hobot.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;
import tk.dimantchick.hobot.domain.instrument.InstrumentsFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Класс спецификаций для поиска инструментов по критериям.
 */
public class InstrumentsSpecification {
    //currency
    public static Specification<Instrument> byCurrency(final Currency currency) {
        if (currency == null) {
            return new Specification<Instrument>() {
                @Override
                public Predicate toPredicate(Root<Instrument> root,
                                             CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.isNotNull(root.get("currency"));
                }
            };
        } else {
            return new Specification<Instrument>() {
                @Override
                public Predicate toPredicate(Root<Instrument> root,
                                             CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("currency"), currency);
                }
            };
        }
    }

    //status
    public static Specification<Instrument> byStatus(final InstrumentStatus status) {
        if (status == null) {
            return new Specification<Instrument>() {
                @Override
                public Predicate toPredicate(Root<Instrument> root,
                                             CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.isNotNull(root.get("status"));
                }
            };
        } else {
            return new Specification<Instrument>() {
                @Override
                public Predicate toPredicate(Root<Instrument> root,
                                             CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("status"), status);
                }
            };
        }
    }

    //custom String field
    public static Specification<Instrument> byFieldLike(final String name, final String value) {
        return new Specification<Instrument>() {
            @Override
            public Predicate toPredicate(Root<Instrument> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(criteriaBuilder.upper(root.get(name)), String.format("%%%S%%", value));

            }
        };
    }

    public static Specification<Instrument> byFilter(InstrumentsFilter filter) {
        return InstrumentsSpecification.byFieldLike("ticker", filter.getTicker()).
                and(InstrumentsSpecification.byFieldLike("name", filter.getName())).
                and(InstrumentsSpecification.byFieldLike("figi", filter.getFigi())).
                and(InstrumentsSpecification.byCurrency(filter.getCurrency())).
                and(InstrumentsSpecification.byStatus(filter.getStatus()));
    }
}
