package tk.dimantchick.hobot.strategies;

import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.candles.CandleHour;
import tk.dimantchick.hobot.domain.position.HobotPosition;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MoveStopLossToEma5OnHour implements SellStrategy {

    @Override
    public boolean isTimeToDoAction(HobotPosition position) {
        List<CandleHour> lastHourCandles = position.getInstrument().getLastHourCandles();

        if (lastHourCandles == null || lastHourCandles.size() < 2) {
            return false;
        }
        CandleHour last = lastHourCandles.get(0);
        if (last.getClosePrice().compareTo(last.getEma5()) > 0
                && last.getEma5().compareTo(position.getPriceSL()) > 0) {
            position.setPriceSL(last.getEma5().multiply(BigDecimal.valueOf(0.9998)));
        }
        return false;
    }
}
