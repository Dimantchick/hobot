package tk.dimantchick.hobot.strategies;


import tk.dimantchick.hobot.domain.position.HobotPosition;

/**
 * Общий интерфейс стратегий.
 */
public interface Strategy {

    boolean isTimeToDoAction(HobotPosition position);
}
