package tk.dimantchick.hobot.domain.instrument.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.dimantchick.hobot.service.InstrumentsService;

@RunWith(SpringRunner.class)
@SpringBootTest
class StringToInstrumentConverterTest {

    @Autowired
    private InstrumentsService instrumentsService;

    @Test
    void convert() {
        StringToInstrumentConverter stringToInstrumentConverter = new StringToInstrumentConverter(instrumentsService);
        Assert.assertNull(null);
        Assert.assertEquals(stringToInstrumentConverter.convert("aanold").getTicker(), instrumentsService.getByTicker("AANold").get().getTicker());
        Assert.assertEquals(stringToInstrumentConverter.convert("AANOLD").getTicker(), instrumentsService.getByTicker("AANold").get().getTicker());
    }
}