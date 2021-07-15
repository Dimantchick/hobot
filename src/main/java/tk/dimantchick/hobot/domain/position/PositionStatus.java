package tk.dimantchick.hobot.domain.position;

/**
 * Статус позиции.
 */
public enum PositionStatus {
    /** Ожидание. Действия по позиции не производятся */
    WAITING("WAITING"),
    /** Готова к действиям. Обрабатываются стратегии покупки по позиции. */
    READY("READY"),
    /** Готова к покупке. Обрабатываются стратегии покупки по позиции.
     * Второе состояние на случай сложных стратегий, когда по условию
     * не требуется непосредственно покупка. Ожидание выполнения какого-то второго условия.*/
    NEED_TO_BUY("NEED_TO_BUY"),
    /** Производится покупка. */
    BUYING("BUYING"),
    /** Позиция сформирована. Проверяются стратегии продажи.
     * При получении команды по любой стратегии, выполняется продажа. */
    BUYED("BUYED"),
    /** Производится продажа. */
    SELLING("SELLING"),
    /** Позиция полностью закрыта. */
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
