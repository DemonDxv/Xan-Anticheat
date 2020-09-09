package dev.demon.xan.base.check.impl.movement.flight;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;

@CheckInfo(name = "Flight", type = "C")
public class FlightC extends Check {

    private double lastDeltaY;


    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {

            if (user.getVelocityData().getVelocityTicks() <= 20) {
                violation = 0;
                return;
            }

            double deltaY = user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY();

            double predictedDist = (lastDeltaY - 0.08D) * 0.9800000190734863D;

            boolean clientGround = user.getMovementData().isOnGround(), lastClientGround = user.getMovementData().isLastOnGround();

            if (Math.abs(predictedDist) <= 0.005D) {
                predictedDist = 0;
            }

            double prediction = 1E-12;
            if (user.getBlockData().blockAboveTicks > 0) {
                prediction = 0.3;
            }

            if (!clientGround && !lastClientGround) {
                if (Math.abs(deltaY - predictedDist) > prediction) {
                    if (violation++ > 7) {
                        alert(user, "P -> " + predictedDist);
                    }
                } else violation -= Math.min(violation, 0.5);
            }

            lastDeltaY = deltaY;
        }
    }
}
