package tk.dimantchick.hobot.domain.instrument;
/**
 * Состояние инструмента.
 * Для инструмента в состоянии ENABLED получаются свечи задаче по расписанию.
 */
public enum InstrumentStatus {
    DISABLED("DISABLED"),
    ENABLED("ENABLED");

    private String value;

    InstrumentStatus(String value) {
        this.value = value;
    }

    public static InstrumentStatus fromValue(String text) {
        for (InstrumentStatus b : InstrumentStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
