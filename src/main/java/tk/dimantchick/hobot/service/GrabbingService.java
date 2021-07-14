package tk.dimantchick.hobot.service;

import org.apache.commons.collections4.list.TreeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.invest.openapi.model.rest.Candle;
import ru.tinkoff.invest.openapi.model.rest.CandleResolution;
import tk.dimantchick.hobot.api.Api;
import tk.dimantchick.hobot.domain.candles.Candle5min;
import tk.dimantchick.hobot.domain.candles.CandleHour;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.repository.Candles5minRepository;
import tk.dimantchick.hobot.repository.CandlesHourRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Service
public class GrabbingService {

    private Logger logger = LoggerFactory.getLogger(GrabbingService.class);

    @Autowired
    private Api api;

    @Autowired
    private Candles5minRepository candles5minRepository;

    @Autowired
    private CandlesHourRepository candlesHourRepository;

    @Autowired
    private InstrumentsService instrumentsService;

    @Transactional
    public void update5minCandles() {
        //logger.debug("update5minCandles " + OffsetDateTime.now());
        Set<Instrument> instruments = instrumentsService.getActiveInstruments();
        List<Candle5min> toDelete5min = new TreeList<>();
        for (Instrument instrument : instruments) {
            OffsetDateTime now = OffsetDateTime.now();
            List<Candle5min> last5min = instrument.getLast5MinCandles();
            OffsetDateTime lastCandleTime;
            List<Candle5min> new5min = new TreeList<>();

            if (last5min.size() < 3) {
                lastCandleTime = now.minusHours(2);
            } else {
                lastCandleTime = last5min.get(2).getTime();
                new5min.addAll(last5min);
                toDelete5min.add(new5min.get(0));
                toDelete5min.add(new5min.get(1));
                new5min.remove(0);
                new5min.remove(0);

            }
            if (lastCandleTime.plusHours(2).isBefore(now)) {
                lastCandleTime = now.minusHours(2);
            }
            List<Candle> historicalCandles = api.getHistoricalCandles(instrument.getFigi(), lastCandleTime, now, CandleResolution._5MIN);
            for (Candle candle : historicalCandles) {
                Candle5min newCandle = null;

                if (new5min.size() == 0) {
                    newCandle = new Candle5min(candle, instrument);
                } else {
                    if (candle.getTime().isAfter(new5min.get(0).getTime())) {
                        newCandle = new Candle5min(candle, new5min.get(0), instrument);
                    }
                }

                if (newCandle != null) {
                    new5min.add(0, newCandle);
                }
            }
            candles5minRepository.saveAll(new5min);
            instrument.setLast5MinCandles(new5min);
        }
        instrumentsService.saveAll();
        candles5minRepository.deleteAll(toDelete5min);
    }

    @Transactional
    public void updateHourCandles() {
        //logger.debug("updateHourCandles " + OffsetDateTime.now());
        Set<Instrument> instruments = instrumentsService.getActiveInstruments();
        for (Instrument instrument : instruments) {
            OffsetDateTime now = OffsetDateTime.now();
            List<CandleHour> lastHour = instrument.getLastHourCandles();
            OffsetDateTime lastCandleTime;
            List<CandleHour> newHour = new TreeList<>();

            if (lastHour.size() < 2) {
                lastCandleTime = now.minusWeeks(1);
            } else {
                lastCandleTime = lastHour.get(0).getTime();
                newHour.addAll(lastHour);
            }

            //Берем свечи с последней полной, или за неделю
            List<Candle> historicalCandles = api.getHistoricalCandles(instrument.getFigi(), lastCandleTime, now, CandleResolution.HOUR);
            for (Candle candle : historicalCandles) {
                //Текущая незаполненная свеча
                if (now.minusHours(1).isBefore(candle.getTime())) {
                    continue;
                }
                CandleHour newCandle = null;
                if (newHour.size() == 0) {
                    newCandle = new CandleHour(candle, instrument);
                } else if (candle.getTime().isAfter(newHour.get(0).getTime())) {
                    newCandle = new CandleHour(candle, newHour.get(0), instrument);
                }
                if (newCandle != null) {
                    newHour.add(0, newCandle);
                }
            }
            candlesHourRepository.saveAll(newHour);
            instrument.setLastHourCandles(newHour);
        }
        instrumentsService.saveAll();
    }

    @Transactional
    public void cleanOldCandles() {
        //logger.debug("cleanOldCandles " + OffsetDateTime.now());
        Set<Instrument> instruments = instrumentsService.getActiveInstruments();
        for (Instrument instrument : instruments) {
            // 5mins clean
            OffsetDateTime minus2hours = OffsetDateTime.now().minusHours(2);
            List<Candle5min> candle5mins = instrument.getLast5MinCandles();
            List<Candle5min> new5min = new TreeList<>();
            List<Candle5min> toRemove5min = new TreeList<>();
            for (int i = 0; i < candle5mins.size(); i++) {
                if (candle5mins.get(i).getTime().isAfter(minus2hours)) {
                    new5min.add(candle5mins.get(i));
                } else {
                    toRemove5min.add(candle5mins.get(i));
                }
            }
            instrument.setLast5MinCandles(new5min);
            candles5minRepository.deleteAll(toRemove5min);
            candles5minRepository.deleteAllByTimeLessThan(minus2hours);
            // hours clean
            OffsetDateTime minus3days = OffsetDateTime.now().minusDays(3);
            List<CandleHour> candleHours = instrument.getLastHourCandles();
            List<CandleHour> newHours = new TreeList<>();
            List<CandleHour> toRemoveHours = new TreeList<>();
            for (int i = 0; i < candleHours.size(); i++) {
                if (candleHours.get(i).getTime().isAfter(minus3days)) {
                    newHours.add(candleHours.get(i));
                } else {
                    toRemoveHours.add(candleHours.get(i));
                }
            }
            instrument.setLastHourCandles(newHours);
            candlesHourRepository.deleteAll(toRemoveHours);
            candlesHourRepository.deleteByTimeLessThan(minus3days);
        }
        instrumentsService.saveAll();
    }


}
