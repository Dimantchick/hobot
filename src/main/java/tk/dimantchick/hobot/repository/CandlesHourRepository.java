package tk.dimantchick.hobot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tk.dimantchick.hobot.domain.candles.CandleHour;
import tk.dimantchick.hobot.domain.instrument.Instrument;

import java.time.OffsetDateTime;

@Repository
public interface CandlesHourRepository extends CrudRepository<CandleHour, Long> {

    Slice<CandleHour> findByInstrument(Instrument instrument, Pageable pageable);

    @Transactional
    void deleteByTimeLessThan(OffsetDateTime time);
}
