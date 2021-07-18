package tk.dimantchick.hobot.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;
import tk.dimantchick.hobot.repository.InstrumentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class InstrumentsServiceTest {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Test
    void enable() {
        InstrumentsService instrumentsService = new InstrumentsService(instrumentRepository);
        // AANold Старый, неиспользуемый тикер.
        Instrument instrument = instrumentRepository.findByTickerIgnoreCase("AANold").get();
        if (instrumentsService.getActiveInstruments().contains(instrument)) {
            instrument.setStatus(InstrumentStatus.DISABLED);
            instrumentRepository.save(instrument);
        }
        instrumentsService.updateInstruments();
        Assert.assertFalse(instrumentsService.getActiveInstruments().contains(instrument));
        instrumentsService.enable(instrument);
        Assert.assertTrue(instrumentsService.getActiveInstruments().contains(instrument));
    }

    @Test
    void disable() {
        InstrumentsService instrumentsService = new InstrumentsService(instrumentRepository);
        // AANold Старый, неиспользуемый тикер.
        Instrument instrument = instrumentRepository.findByTickerIgnoreCase("AANold").get();
        if (!instrumentsService.getActiveInstruments().contains(instrument)) {
            instrument.setStatus(InstrumentStatus.ENABLED);
            instrumentRepository.save(instrument);
        }
        instrumentsService.updateInstruments();
        Assert.assertTrue(instrumentsService.getActiveInstruments().contains(instrument));
        instrumentsService.disable(instrument);
        Assert.assertFalse(instrumentsService.getActiveInstruments().contains(instrument));
    }
}