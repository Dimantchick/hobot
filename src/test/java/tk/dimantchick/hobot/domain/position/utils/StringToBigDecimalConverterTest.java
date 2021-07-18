package tk.dimantchick.hobot.domain.position.utils;

import org.junit.jupiter.api.Test;
import org.junit.Assert;

import java.math.BigDecimal;


class StringToBigDecimalConverterTest {

    @Test
    void convert() {
        StringToBigDecimalConverter stringToBigDecimalConverter = new StringToBigDecimalConverter();
        Assert.assertEquals(stringToBigDecimalConverter.convert("-1"), BigDecimal.ZERO);
        Assert.assertEquals(stringToBigDecimalConverter.convert("0"), BigDecimal.ZERO);
        Assert.assertEquals(stringToBigDecimalConverter.convert("1000001"), StringToBigDecimalConverter.MAX_BIG_DECIMAL);
        Assert.assertEquals(stringToBigDecimalConverter.convert("1000000"), StringToBigDecimalConverter.MAX_BIG_DECIMAL);
        Assert.assertEquals(stringToBigDecimalConverter.convert("101"), BigDecimal.valueOf(101));
    }
}