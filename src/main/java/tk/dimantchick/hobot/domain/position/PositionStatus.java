package tk.dimantchick.hobot.domain.position;

public enum PositionStatus {

    WAITING("WAITING"),
    READY("READY"),
    NEED_TO_BUY("NEED_TO_BUY"),
    BUYING("BUYING"),
    BUYED("BUYED"),
    SELLING("SELLING"),
    CLOSED("CLOSED");

    private String value;

    PositionStatus(String value) {
        this.value = value;
    }

    public static PositionStatus fromValue(String text) {
        for (PositionStatus b : PositionStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
