package tk.dimantchick.hobot.domain.instrument;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import tk.dimantchick.hobot.domain.candles.Candle5min;
import tk.dimantchick.hobot.domain.candles.CandleHour;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "INSTRUMENTS")
public class Instrument {

    private String figi;
    @Id
    private String ticker;
    private String name;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private InstrumentStatus status;
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable
    @OrderBy("time DESC")
    private List<Candle5min> last5MinCandles;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable
    @OrderBy("time DESC")
    private List<CandleHour> lastHourCandles;

    public Instrument() {
    }

    public Instrument(String figi, String ticker, Currency currency, InstrumentStatus status) {
        this.figi = figi;
        this.ticker = ticker;
        this.currency = currency;
        this.status = status;
    }

    public String getFigi() {
        return figi;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public InstrumentStatus getStatus() {
        return status;
    }

    public void setStatus(InstrumentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                ", figi='" + figi + '\'' +
                ", ticker='" + ticker + '\'' +
                ", currency=" + currency +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instrument that = (Instrument) o;
        return ticker.equals(that.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker);
    }

    public synchronized List<Candle5min> getLast5MinCandles() {
        return last5MinCandles;
    }

    public synchronized BigDecimal getLastPrice() {
        return last5MinCandles.isEmpty() ? BigDecimal.ZERO : last5MinCandles.get(0).getClosePrice();
    }

    public synchronized List<CandleHour> getLastHourCandles() {
        return lastHourCandles;
    }

    public synchronized void setLast5MinCandles(List<Candle5min> last5MinCandles) {
        this.last5MinCandles = last5MinCandles;
    }

    public synchronized void setLastHourCandles(List<CandleHour> lastHourCandles) {
        this.lastHourCandles = lastHourCandles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
