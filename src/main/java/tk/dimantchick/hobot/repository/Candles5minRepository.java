package tk.dimantchick.hobot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tk.dimantchick.hobot.domain.candles.Candle5min;
import tk.dimantchick.hobot.domain.instrument.Instrument;

import java.time.OffsetDateTime;

@Repository
public interface Candles5minRepository extends CrudRepository<Candle5min, Long> {

    Slice<Candle5min> findByInstrument(Instrument instrument, Pageable pageable);

    @Transactional
    void deleteAllByTimeLessThan(OffsetDateTime time);

}
