package tk.dimantchick.hobot.domain.position.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Конвертер для получения BigDecimal по строке.
 * Создан для подмены нечисловых значений на 0.
 */
@Component
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

    public static final BigDecimal MAX_BIG_DECIMAL = BigDecimal.valueOf(1000000);

    @Override
    public BigDecimal convert(String s) {
        try {
            BigDecimal value = new BigDecimal(s);
            if (value.compareTo(BigDecimal.ZERO) < 0) {
                value = BigDecimal.ZERO;
            }
            if (value.compareTo(MAX_BIG_DECIMAL) > 0) {
                value = MAX_BIG_DECIMAL;
            }
            return value;
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
