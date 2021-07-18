package tk.dimantchick.hobot.strategies;

import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.candles.Candle5min;
import tk.dimantchick.hobot.domain.position.HobotPosition;

import java.math.BigDecimal;

/**
 * Стратегия.
 * ToDo описание алгоритма.
 */
@Component
public class BuyAt5minReverse implements BuyStrategy {
    @Override
    public boolean isTimeToDoAction(HobotPosition position) {
        // Недостаточно данных
        if (position.getInstrument().getLast5MinCandles().size() < 10) {
            return false;
        }
        Candle5min lastFilled = position.getInstrument().getLast5MinCandles().get(1);
        if (lastFilled.getClosePrice().compareTo(position.getPriceToBuy()) < 0
                && lastFilled.isHAGreen()) {
            position.setPriceSL(lastFilled.getClosePrice().multiply(BigDecimal.valueOf(0.98)));
            return true;
        }
        return false;
    }
}
