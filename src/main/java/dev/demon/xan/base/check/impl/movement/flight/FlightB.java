package dev.demon.xan.base.check.impl.movement.flight;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;

@CheckInfo(name = "Flight", type = "B")
public class FlightB extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            double deltaY = Math.abs(user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY());

            if (user.getVelocityData().getVelocityTicks() <= 20 || user.generalCancel()  || user.getBlockData().liquidTicks > 0 || user.getBlockData().climbableTicks > 0) {
                violation = 0;
                return;
            }

            if (deltaY > 0 && user.getMovementData().isClientGround()) {
                if (violation++ > 1) {
                    alert(user, "DY -> " + deltaY + " CG -> " + user.getMovementData().isClientGround());
                }
            }else violation -= Math.min(violation, 0.5);
        }
    }
}
