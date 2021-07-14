package tk.dimantchick.hobot.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface InstrumentRepository extends PagingAndSortingRepository<Instrument, Long> {
    Optional<Instrument> findById(Long l);

    Optional<Instrument> findByFigi(String figi);

    Optional<Instrument> findByTicker(String ticker);

    Set<Instrument> findByStatusNot(InstrumentStatus status);

    Set<Instrument> findByCurrency(Currency currency);

    List<Instrument> findByCurrency(Currency currency, Pageable pageable);

    Set<Instrument> findAll(Sort sort);

    List<Instrument> findInstrumentByTickerContainingIgnoreCaseAndNameContainingIgnoreCaseAndFigiContainingIgnoreCaseAndCurrencyInAndStatusIn(String ticker, String name, String figi, Currency[] currency, InstrumentStatus[] status, Pageable pageable);

    int countByTickerContainingIgnoreCaseAndNameContainingIgnoreCaseAndFigiContainingIgnoreCaseAndCurrencyInAndStatusIn(String ticker, String name, String figi, Currency[] currency, InstrumentStatus[] status);

//    List<Instrument> findInstrumentByTickerContainingIgnoreCaseAndNameContainingIgnoreCaseAndFigiContainingIgnoreCaseAndCurrencyIs(String ticker, String name, String figi, Currency currency, Pageable pageable);
//
//    long countByTickerContainingIgnoreCaseAndNameContainingIgnoreCaseAndFigiContainingIgnoreCaseAndCurrencyIs(String ticker, String name, String figi, Currency currency);
//
//    List<Instrument> findInstrumentByTickerContainingIgnoreCaseAndNameContainingIgnoreCaseAndFigiContainingIgnoreCaseAndCurrencyLikeAndStatusLike(String ticker, String name, String figi, String currency, String status, Pageable pageable);
//
//    long countByTickerContainingIgnoreCaseAndNameContainingIgnoreCaseAndFigiContainingIgnoreCaseAndCurrencyLikeAndStatusLike(String ticker, String name, String figi, Currency currency, InstrumentStatus status);

    Set<Instrument> findAll();

    long countByCurrency(Currency currency);
    long count();
}
