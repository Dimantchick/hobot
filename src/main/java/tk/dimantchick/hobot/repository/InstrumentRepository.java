package tk.dimantchick.hobot.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;

import java.util.Optional;
import java.util.Set;


/**
 * Репозиторий инструментов.
 */
@Repository
public interface InstrumentRepository extends PagingAndSortingRepository<Instrument, Long>, JpaSpecificationExecutor<Instrument> {

    Optional<Instrument> findByTickerIgnoreCase(String ticker);

    Set<Instrument> findByStatusNot(InstrumentStatus status);

    Set<Instrument> findAll(Sort sort);

    long count();
}
