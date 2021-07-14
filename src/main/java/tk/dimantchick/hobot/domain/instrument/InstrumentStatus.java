package tk.dimantchick.hobot.domain.instrument;

public enum InstrumentStatus {
    DISABLED("DISABLED"),
    ENABLED("ENABLED");
/*    READY("READY"),
    READY_TO_BUY("READY_TO_BUY"),
    BUYING("BUYING"),
    BUYED("BUYED"),
    SELLING("SELLING");*/

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
