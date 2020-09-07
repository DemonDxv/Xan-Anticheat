package dev.demon.xan.base.check.impl.player.timer;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.math.RollingAverageDouble;

@CheckInfo(name = "Timer", type = "A")
public class TimerA extends Check {
    private long lastTimerMove, timerCheck;
    private RollingAverageDouble timerRate = new RollingAverageDouble(40, 50.0);

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            long now = System.currentTimeMillis();

            double diff = now - lastTimerMove;

            timerRate.add(diff);

            if (now - timerCheck >= 1000L) {
                timerCheck = now;

                double timerSpeed = 50.0 / timerRate.getAverage();

                if (timerSpeed >= 1.01) {
                    if (violation++ > 3) {
                        alert(user, "TS -> "+timerSpeed);
                    }
                }else violation -= Math.min(violation, 0.5);
            }
            lastTimerMove = now;
        }
    }
}
