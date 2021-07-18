package tk.dimantchick.hobot.domain.position.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StringToIntegerConverterTest {

    @Test
    void convert() {
        StringToIntegerConverter stringToIntegerConverter = new StringToIntegerConverter();
        Assert.assertEquals(stringToIntegerConverter.convert("-1"), Integer.valueOf(0));
        Assert.assertEquals(stringToIntegerConverter.convert("0"), Integer.valueOf(0));
        Assert.assertEquals(stringToIntegerConverter.convert("1000001"), StringToIntegerConverter.MAX_INT);
        Assert.assertEquals(stringToIntegerConverter.convert("1000000"), StringToIntegerConverter.MAX_INT);
        Assert.assertEquals(stringToIntegerConverter.convert("101"), Integer.valueOf(101));
    }
}