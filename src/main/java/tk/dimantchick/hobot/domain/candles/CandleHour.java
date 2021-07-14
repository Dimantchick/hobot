package tk.dimantchick.hobot.domain.candles;

import ru.tinkoff.invest.openapi.model.rest.Candle;
import ru.tinkoff.invest.openapi.model.rest.CandleResolution;
import tk.dimantchick.hobot.domain.instrument.Instrument;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Entity
@Table(name = "CANDLES_HOUR")
public class CandleHour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name="instrument_id")
    private Instrument instrument;
    private OffsetDateTime time;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal lowestPrice;
    private BigDecimal highestPrice;
    private BigDecimal HAopenPrice;
    private BigDecimal HAclosePrice;
    private BigDecimal HAlowestPrice;
    private BigDecimal HAhighestPrice;
    private int volume;
    private BigDecimal ema5;
    private BigDecimal ema10;
    private BigDecimal ema20;
    private BigDecimal ema50;
    private BigDecimal ema100;
    private BigDecimal ema200;

    //public BigDecimal stochK;

    private CandleResolution resolution;

    public static final BigDecimal ema5A = new BigDecimal(2D/(5D + 1D));
    public static final BigDecimal ema10A = new BigDecimal(2D/(10D + 1D));
    public static final BigDecimal ema20A = new BigDecimal(2D/(20D + 1D));
    public static final BigDecimal ema50A = new BigDecimal(2D/(50D + 1D));
    public static final BigDecimal ema100A = new BigDecimal(2D/(100D + 1D));
    public static final BigDecimal ema200A = new BigDecimal(2D/(200D + 1D));


    public CandleHour(Candle candle, Instrument instrument) {
        this.time = candle.getTime();
        this.openPrice = candle.getO();
        this.closePrice = candle.getC();
        this.lowestPrice = candle.getL();
        this.highestPrice = candle.getH();
        this.instrument = instrument;
        this.volume = candle.getV();
        ema5 = closePrice;
        ema10 = closePrice;
        ema20 = closePrice;
        ema50 = closePrice;
        ema100 = closePrice;
        ema200 = closePrice;
        HAopenPrice = openPrice;
        HAclosePrice = closePrice;
        HAlowestPrice = lowestPrice;
        HAhighestPrice = highestPrice;
        this.resolution = candle.getInterval();
    }

    public CandleHour(Candle candle, CandleHour last, Instrument instrument) {
        this.time = candle.getTime();
        this.openPrice = candle.getO();
        this.closePrice = candle.getC();
        this.lowestPrice = candle.getL();
        this.highestPrice = candle.getH();
        this.instrument = instrument;
        this.volume = candle.getV();
        ema5 = closePrice.multiply(ema5A).add(BigDecimal.ONE.subtract(ema5A).multiply(last.ema5)).setScale(4, RoundingMode.HALF_UP);
        ema10 = closePrice.multiply(ema10A).add(BigDecimal.ONE.subtract(ema10A).multiply(last.ema10)).setScale(4, RoundingMode.HALF_UP);
        ema20 = closePrice.multiply(ema20A).add(BigDecimal.ONE.subtract(ema20A).multiply(last.ema20)).setScale(4, RoundingMode.HALF_UP);
        ema50 = closePrice.multiply(ema50A).add(BigDecimal.ONE.subtract(ema50A).multiply(last.ema50)).setScale(4, RoundingMode.HALF_UP);
        ema100 = closePrice.multiply(ema100A).add(BigDecimal.ONE.subtract(ema100A).multiply(last.ema100)).setScale(4, RoundingMode.HALF_UP);
        ema200 = closePrice.multiply(ema200A).add(BigDecimal.ONE.subtract(ema200A).multiply(last.ema200)).setScale(4, RoundingMode.HALF_UP);
        HAopenPrice = (last.HAopenPrice.add(last.HAclosePrice)).divide(new BigDecimal(2)).setScale(10, RoundingMode.HALF_UP);
        HAclosePrice = (openPrice.add(closePrice).add(lowestPrice).add(highestPrice)).divide(new BigDecimal(4));
        HAlowestPrice = lowestPrice.min(HAopenPrice).min(HAclosePrice);
        HAhighestPrice = highestPrice.max(HAopenPrice).max(HAclosePrice);
        this.resolution = candle.getInterval();
    }

    @Override
    public String toString() {
        return "tk.dimantchick.speculant.candles.CandleWithHA{" +
                "instrument=" + instrument +
                ", time=" + time +
                ", resolution=" + resolution +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", lowestPrice=" + lowestPrice +
                ", highestPrice=" + highestPrice +
                ", HAopenPrice=" + HAopenPrice +
                ", HAclosePrice=" + HAclosePrice +
                ", HAlowestPrice=" + HAlowestPrice +
                ", HAhighestPrice=" + HAhighestPrice +
                ", volume=" + volume +
                ", ema5=" + ema5 +
                ", ema10=" + ema10 +
                ", ema20=" + ema20 +
                ", ema50=" + ema50 +
                ", ema100=" + ema100 +
                ", ema200=" + ema200 +
                '}';
    }

    public CandleHour() {
    }

    public boolean isHAGreen() {

        return HAopenPrice.compareTo(HAclosePrice) < 0;
    }
    public boolean isHARed() {
        return HAopenPrice.compareTo(HAclosePrice) > 0;
    }
    public double HAoc() {
        return Math.abs(HAopenPrice.doubleValue() - HAclosePrice.doubleValue());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

   /* public BigDecimal getStochK() {
        return stochK;
    }

    public void setStochK(BigDecimal stochK) {
        this.stochK = stochK;
    }*/

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice) {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getHAopenPrice() {
        return HAopenPrice;
    }

    public void setHAopenPrice(BigDecimal HAopenPrice) {
        this.HAopenPrice = HAopenPrice;
    }

    public BigDecimal getHAclosePrice() {
        return HAclosePrice;
    }

    public void setHAclosePrice(BigDecimal HAclosePrice) {
        this.HAclosePrice = HAclosePrice;
    }

    public BigDecimal getHAlowestPrice() {
        return HAlowestPrice;
    }

    public void setHAlowestPrice(BigDecimal HAlowestPrice) {
        this.HAlowestPrice = HAlowestPrice;
    }

    public BigDecimal getHAhighestPrice() {
        return HAhighestPrice;
    }

    public void setHAhighestPrice(BigDecimal HAhighestPrice) {
        this.HAhighestPrice = HAhighestPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public BigDecimal getEma5() {
        return ema5;
    }

    public void setEma5(BigDecimal ema5) {
        this.ema5 = ema5;
    }

    public BigDecimal getEma10() {
        return ema10;
    }

    public void setEma10(BigDecimal ema10) {
        this.ema10 = ema10;
    }

    public BigDecimal getEma20() {
        return ema20;
    }

    public void setEma20(BigDecimal ema20) {
        this.ema20 = ema20;
    }

    public BigDecimal getEma50() {
        return ema50;
    }

    public void setEma50(BigDecimal ema50) {
        this.ema50 = ema50;
    }

    public BigDecimal getEma100() {
        return ema100;
    }

    public void setEma100(BigDecimal ema100) {
        this.ema100 = ema100;
    }

    public BigDecimal getEma200() {
        return ema200;
    }

    public void setEma200(BigDecimal ema200) {
        this.ema200 = ema200;
    }

    public CandleResolution getResolution() {
        return resolution;
    }

    public void setResolution(CandleResolution resolution) {
        this.resolution = resolution;
    }

    public static BigDecimal getEma5A() {
        return ema5A;
    }

    public static BigDecimal getEma10A() {
        return ema10A;
    }

    public static BigDecimal getEma20A() {
        return ema20A;
    }

    public static BigDecimal getEma50A() {
        return ema50A;
    }

    public static BigDecimal getEma100A() {
        return ema100A;
    }

    public static BigDecimal getEma200A() {
        return ema200A;
    }
}
