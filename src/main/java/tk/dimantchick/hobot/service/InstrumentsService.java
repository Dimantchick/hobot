package tk.dimantchick.hobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.model.rest.Currency;
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
        Set<Instrument> instruments = activeInstruments;//getByCurrencyFromDB(Currency.USD);
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

    public Iterable<Instrument> getAllFromDB() {
        return instrumentRepository.findAll();
    }

    public Set<Instrument> getActiveFromDB() {
        return instrumentRepository.findByStatusNot(InstrumentStatus.DISABLED);
    }

    public Set<Instrument> getByCurrencyFromDB(Currency currency) {
        return instrumentRepository.findByCurrency(currency);
    }

    public List<Instrument> getByCurrencyFromDB(Currency currency, Pageable pageable) {
        return instrumentRepository.findByCurrency(currency, pageable);
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
        return instrumentRepository.findByTicker(ticker);
    }

    public long countByCurrency(Currency currency) {
        return instrumentRepository.countByCurrency(currency);
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
