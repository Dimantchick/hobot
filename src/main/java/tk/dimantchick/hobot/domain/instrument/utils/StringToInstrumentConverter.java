package tk.dimantchick.hobot.domain.instrument.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.service.InstrumentsService;

import java.util.Optional;

/**
 * Конвертер для получения инструмента по тикеру.
 */
@Component
public class StringToInstrumentConverter implements Converter<String, Instrument> {

    private final InstrumentsService instrumentsService;

    public StringToInstrumentConverter(InstrumentsService instrumentsService) {
        this.instrumentsService = instrumentsService;
    }

    @Override
    public Instrument convert(String s) {
        Optional<Instrument> byTicker = instrumentsService.getByTicker(s);
        if (byTicker.isPresent()) {
            return byTicker.get();
        }
        return null;
    }
}
