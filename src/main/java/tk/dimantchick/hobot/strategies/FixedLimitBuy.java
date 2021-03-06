package tk.dimantchick.hobot.strategies;


import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.position.HobotPosition;

/**
 * Стратегия.
 * Покупка по фиксированной цене priceToBuy позиции.
 */
@Component
public class FixedLimitBuy implements BuyStrategy {

    @Override
    public boolean isTimeToDoAction(HobotPosition position) {
        if (position.getPriceToBuy().compareTo(position.getLastPrice()) >= 0) {
            return true;
        }
        return false;
    }
}
