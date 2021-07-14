package tk.dimantchick.hobot.strategies;

import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.candles.CandleHour;
import tk.dimantchick.hobot.domain.position.HobotPosition;

import java.util.List;

@Component
public class SellAtEma20CrossEma50OnHour implements SellStrategy {

    @Override
    public boolean isTimeToDoAction(HobotPosition position) {
        List<CandleHour> lastHourCandles = position.getInstrument().getLastHourCandles();

        if (lastHourCandles == null || lastHourCandles.size() < 2) {
            return false;
        }
        CandleHour lastHour = lastHourCandles.get(0);
        CandleHour preLastHour = lastHourCandles.get(1);
        //CandleWithEmaHA pre2LastHour = lastHourCandles.get(2);
        if (lastHour.getEma20().compareTo(lastHour.getEma50()) < 0
        && preLastHour.getEma20().compareTo(preLastHour.getEma50()) >= 0) {
            //System.out.println("SellAtEma20CrossEma50OnHour");
            return true;
        }
        return false;
    }
}
