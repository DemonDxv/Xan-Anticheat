package dev.demon.xan.impl.checks.movement.flight;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "Flight", type = "F")
public class FlightF extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {
            if (user.generalCancel()
                    || TimeUtils.elapsed(user.getMovementData().getLastTeleport()) < 1000L) {
                return;
            }

            double deltaY = user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY();

            if (deltaY < -0.08 && user.getMovementData().isLastClientGround()) {
                alert(user, "DY -> " + deltaY + " CG -> " + user.getMovementData().isLastClientGround());
            }
        }
    }
}
