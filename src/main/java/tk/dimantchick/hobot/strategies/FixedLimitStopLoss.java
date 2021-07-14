package tk.dimantchick.hobot.strategies;

import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.position.HobotPosition;

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
