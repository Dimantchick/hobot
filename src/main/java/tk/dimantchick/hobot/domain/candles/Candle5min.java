package tk.dimantchick.hobot.domain.candles;

import ru.tinkoff.invest.openapi.model.rest.Candle;
import ru.tinkoff.invest.openapi.model.rest.CandleResolution;
import tk.dimantchick.hobot.domain.instrument.Instrument;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Entity
@Table(name = "CANDLES_5MIN")
public class Candle5min {

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
    private CandleResolution resolution;

    public Candle5min(Candle candle, Instrument instrument) {
        this.time = candle.getTime();
        this.openPrice = candle.getO();
        this.closePrice = candle.getC();
        this.lowestPrice = candle.getL();
        this.highestPrice = candle.getH();
        this.instrument = instrument;
        this.volume = candle.getV();
        HAopenPrice = openPrice;
        HAclosePrice = closePrice;
        HAlowestPrice = lowestPrice;
        HAhighestPrice = highestPrice;
        this.resolution = candle.getInterval();
    }

    public Candle5min(Candle candle, Candle5min last, Instrument instrument) {
        this.time = candle.getTime();
        this.openPrice = candle.getO();
        this.closePrice = candle.getC();
        this.lowestPrice = candle.getL();
        this.highestPrice = candle.getH();
        this.instrument = instrument;
        this.volume = candle.getV();
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
                '}';
    }

    public Candle5min() {
    }

    public boolean isHAGreen() {

        return HAopenPrice.compareTo(HAclosePrice) < 0;
    }

    public boolean isLowThin() {
        BigDecimal min = HAclosePrice.min(HAopenPrice);
        return HAlowestPrice.compareTo(min) < 0;
    }
    public boolean isHighThin() {
        BigDecimal max = HAclosePrice.max(HAopenPrice);
        return HAhighestPrice.compareTo(max) > 0;
    }


    public boolean isHARed() {
        return HAopenPrice.compareTo(HAclosePrice) > 0;
    }

    public double HAoc() {
        return Math.abs(HAopenPrice.doubleValue() - HAclosePrice.doubleValue());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public CandleResolution getResolution() {
        return resolution;
    }

    public void setResolution(CandleResolution resolution) {
        this.resolution = resolution;
    }
}
