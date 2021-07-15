package tk.dimantchick.hobot.strategies;

import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.candles.CandleHour;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.position.HobotPosition;
import tk.dimantchick.hobot.domain.position.PositionStatus;


import java.math.BigDecimal;

/**
 * Стратегия.
 * ToDo описание алгоритма.
 */
@Component
public class BuyAtEMA20CrossEma50OnHour implements BuyStrategy {
    @Override
    public boolean isTimeToDoAction(HobotPosition position) {
        // Недостаточно данных
        if (position.getInstrument().getLastHourCandles().size() < 2) {
            return false;
        }
        CandleHour lastHour = position.getInstrument().getLastHourCandles().get(0);
        CandleHour preLastHour = position.getInstrument().getLastHourCandles().get(1);

        if (position.getStatus() == PositionStatus.READY) {
            if (preLastHour.getEma20().compareTo(preLastHour.getEma50()) < 0 &&
                    lastHour.getEma20().compareTo(lastHour.getEma50()) > 0) {
                position.setStatus(PositionStatus.NEED_TO_BUY);
                position.setReadyToBuyTime(lastHour.getTime());
                return false;
            }
        } else if (position.getStatus() == PositionStatus.NEED_TO_BUY) {
            if (position.getReadyToBuyTime().plusHours(22).isBefore(lastHour.getTime())) {
                position.setStatus(PositionStatus.READY);
                return false;
            }
            if (position.getLastPrice().compareTo(lastHour.getEma20()) < 0
                    && lastHour.getEma50().compareTo(preLastHour.getEma50()) > 0) {
                position.setPriceSL(position.getLastPrice().multiply(BigDecimal.valueOf(0.95D)));
                return true;
            }
        }
        return false;
    }
}
