package tk.dimantchick.hobot.strategies;

import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.domain.candles.CandleHour;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;
import tk.dimantchick.hobot.domain.position.HobotPosition;
import tk.dimantchick.hobot.domain.position.PositionStatus;

@Component
public class BuyAtEMA50OnHour implements BuyStrategy {
    @Override
    public boolean isTimeToDoAction(HobotPosition position) {
        // Недостаточно данных
        if (position.getInstrument().getLastHourCandles().size() < 2) {
            return false;
        }
        CandleHour lastHour = position.getInstrument().getLastHourCandles().get(0);
        Instrument instrument = position.getInstrument();
        if (position.getStatus() == PositionStatus.READY){
            if (lastHour.getEma5().compareTo(lastHour.getEma50()) > 0) {
                position.setStatus(PositionStatus.NEED_TO_BUY);
                position.setPriceToBuy(lastHour.getEma50());
            }
        }
        else if (position.getStatus() == PositionStatus.NEED_TO_BUY) {
            boolean isReady = false;
            for (int i = 1; i < Math.min(position.getInstrument().getLastHourCandles().size(), 15); i++) {
                CandleHour candle = position.getInstrument().getLastHourCandles().get(i);
                if (candle.getEma5().compareTo(candle.getEma50()) < 0) {
                    isReady = true;
                }
            }
            if (isReady) {
                position.setPriceToBuy(lastHour.getEma50());
                return true;
            }
            else {
                position.setStatus(PositionStatus.READY);
                return false;
            }
        }
        return false;
    }
}
