package tk.dimantchick.hobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;
import tk.dimantchick.hobot.domain.instrument.InstrumentsFilter;
import tk.dimantchick.hobot.repository.InstrumentRepository;
import tk.dimantchick.hobot.repository.specifications.InstrumentsSpecification;

import java.util.*;

/**
 * Сервис, отвечающий за инструменты.
 * Держит в памяти актуальный список активных инструментов.
 */

@Service
public class InstrumentsService {

    private Logger logger = LoggerFactory.getLogger(InstrumentsService.class);

    private InstrumentRepository instrumentRepository;

    private Set<Instrument> activeInstruments;

    private Map<String, Instrument> all;

    public InstrumentsService(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
        updateInstruments();
        logger.debug("fill all instruments");
        all = new HashMap<>();
        //ToDo На время написания стоит активные инструменты. А так должен брать все. (долгая загрузка)
        Set<Instrument> instruments = activeInstruments;//getAll();
        for (Instrument instrument : instruments) {
            all.put(instrument.getTicker(), instrument);
        }
    }

    public synchronized Set<Instrument> getActiveInstruments() {
        return activeInstruments;
    }

    public synchronized void updateInstruments() {
        logger.debug("load Active instruments");
        activeInstruments = instrumentRepository.findByStatusNot(InstrumentStatus.DISABLED);
    }

    public synchronized void enable(Instrument instrument) {
        logger.debug("Enable instrument " + instrument.getTicker());
        instrument.setStatus(InstrumentStatus.ENABLED);
        instrumentRepository.save(instrument);
        updateInstruments();
    }

    public synchronized void disable(Instrument instrument) {
        logger.debug("Disable instrument " + instrument.getTicker());
        instrument.setStatus(InstrumentStatus.DISABLED);
        instrumentRepository.save(instrument);
        updateInstruments();
    }

    public synchronized void saveAll() {
        instrumentRepository.saveAll(activeInstruments);
    }

    public synchronized void saveAll(Collection<Instrument> instruments) {
        instrumentRepository.saveAll(instruments);
    }

    public Map<String, Instrument> getTickers() {
        return all;
    }

    public Optional<Instrument> getByTicker(String ticker) {
        return instrumentRepository.findByTickerIgnoreCase(ticker);
    }

    public Page<Instrument> getByFilter(InstrumentsFilter filter, Pageable pageable) {
        return instrumentRepository.findAll(
                InstrumentsSpecification.byFilter(filter),
                pageable
        );
    }

    public long countByFilter(InstrumentsFilter filter) {
        return instrumentRepository.count(
                InstrumentsSpecification.byFilter(filter)
        );
    }

}
