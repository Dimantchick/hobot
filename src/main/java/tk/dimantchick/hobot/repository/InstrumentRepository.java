package tk.dimantchick.hobot.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Репозиторий инструментов.
 */
@Repository
public interface InstrumentRepository extends PagingAndSortingRepository<Instrument, Long>, JpaSpecificationExecutor<Instrument> {

    Optional<Instrument> findByTicker(String ticker);

    Set<Instrument> findByStatusNot(InstrumentStatus status);

    Set<Instrument> findByCurrency(Currency currency);

    List<Instrument> findByCurrency(Currency currency, Pageable pageable);

    Set<Instrument> findAll(Sort sort);

    long countByCurrency(Currency currency);

    long count();
}
