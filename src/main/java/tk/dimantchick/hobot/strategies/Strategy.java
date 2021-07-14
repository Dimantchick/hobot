package tk.dimantchick.hobot.strategies;


import tk.dimantchick.hobot.domain.position.HobotPosition;

public interface Strategy {

    boolean isTimeToDoAction(HobotPosition position);
}
