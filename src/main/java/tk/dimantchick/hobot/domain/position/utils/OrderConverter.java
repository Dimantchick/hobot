package tk.dimantchick.hobot.domain.position.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.invest.openapi.model.rest.PlacedLimitOrder;

import javax.persistence.AttributeConverter;

/**
 * Конвертирует выставленный ордер в JSON для сохранения в поле позиции.
 */
@Component
public class OrderConverter implements AttributeConverter<PlacedLimitOrder, String> {
    @Autowired
    private ObjectMapper mapper;
    @Override
    public String convertToDatabaseColumn(PlacedLimitOrder placedLimitOrder) {
        try {
            return mapper.writeValueAsString(placedLimitOrder);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PlacedLimitOrder convertToEntityAttribute(String s) {
        try {
            return mapper.readValue(s, PlacedLimitOrder.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
