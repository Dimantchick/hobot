package tk.dimantchick.hobot.strategies;

import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.position.HobotPosition;

/**
 * Стратегия.
 * Продажа по фиксированной цене priceSL в позиции.
 */
@Component
public class FixedLimitStopLoss implements SellStrategy {

    @Override
    public boolean isTimeToDoAction(HobotPosition position) {
        if (position.getPriceSL().compareTo(position.getLastPrice()) >= 0) {
            return true;
        }
        return false;
    }
}
