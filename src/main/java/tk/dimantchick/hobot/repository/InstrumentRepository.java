package tk.dimantchick.hobot.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public interface InstrumentRepository extends PagingAndSortingRepository<Instrument, Long> {
    Optional<Instrument> findByFigi(String figi);

    Optional<Instrument> findByTicker(String ticker);

    Set<Instrument> findByStatusNot(InstrumentStatus status);

    Set<Instrument> findByCurrency(Currency currency);

    List<Instrument> findByCurrency(Currency currency, Pageable pageable);

    Set<Instrument> findAll(Sort sort);

    /**
     * Выбор инструментов по фильтру.
     */
    List<Instrument> findInstrumentByTickerContainingIgnoreCaseAndNameContainingIgnoreCaseAndFigiContainingIgnoreCaseAndCurrencyInAndStatusIn(String ticker, String name, String figi, Currency[] currency, InstrumentStatus[] status, Pageable pageable);

    /**
     * Подсчет инструментов по фильтру.
     * Используется для подсчета страниц.
     */
    int countByTickerContainingIgnoreCaseAndNameContainingIgnoreCaseAndFigiContainingIgnoreCaseAndCurrencyInAndStatusIn(String ticker, String name, String figi, Currency[] currency, InstrumentStatus[] status);

    Set<Instrument> findAll();

    long countByCurrency(Currency currency);
    long count();
}
