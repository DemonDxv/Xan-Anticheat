package dev.demon.xan.base.check.impl.movement.flight;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;

@CheckInfo(name = "Flight", type = "F")
public class FlightF extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {

            if (user.generalCancel()) {
                return;
            }


            double deltaY = user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY();

            if (deltaY < -0.08 && user.getMovementData().isLastClientGround()) {
                alert(user, "DY -> "+deltaY + " CG -> "+user.getMovementData().isLastClientGround());
            }
        }
    }
}
