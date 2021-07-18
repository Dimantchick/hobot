package tk.dimantchick.hobot.domain.position.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Конвертер для получения Integer по строке.
 * Создан для подмены нечисловых значений на 0.
 */
@Component
public class StringToIntegerConverter implements Converter<String, Integer> {

    public static final Integer MAX_INT = 1000000;

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
