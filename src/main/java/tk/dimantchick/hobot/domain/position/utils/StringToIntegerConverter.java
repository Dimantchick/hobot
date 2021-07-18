package tk.dimantchick.hobot.domain.position.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Конвертер для получения Integer по строке.
 * Создан для подмены нечисловых значений на 0.
 */
@Component
public class StringToIntegerConverter implements Converter<String, Integer> {

    private static final int MAX_INT = Integer.MAX_VALUE;

    @Override
    public Integer convert(String s) {
        try {
            Integer value = Integer.valueOf(s);
            if (value < 0) {
                value = 0;
            }
            if (value > MAX_INT) {
                value = MAX_INT;
            }
            return value;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
