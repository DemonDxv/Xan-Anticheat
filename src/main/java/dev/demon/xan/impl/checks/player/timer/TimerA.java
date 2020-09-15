package dev.demon.xan.impl.checks.player.timer;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.math.RollingAverageDouble;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "Timer", type = "A")
public class TimerA extends Check {
    private long lastTimerMove, timerCheck;
    private RollingAverageDouble timerRate = new RollingAverageDouble(40, 50.0);

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {

            if ((user.generalCancel() || TimeUtils.elapsed(user.getMovementData().getLastTeleport()) < 1000L)) {
                violation = 0;
                return;
            }

            long now = System.currentTimeMillis();

            double diff = now - lastTimerMove;

            timerRate.add(diff);

            if (now - timerCheck >= 1000L) {
                timerCheck = now;

                double timerSpeed = 50.0 / timerRate.getAverage();

                double max = TimeUtils.elapsed(user.getMovementData().getLastEnderpearl()) < 1000L ? 1.04 : 1.01;

                if (timerSpeed >= max) {
                    if (violation++ > 3) {
                        alert(user, "TS -> " + timerSpeed);
                    }
                } else violation -= Math.min(violation, 0.5);
            }
            lastTimerMove = now;
        }
    }
}
