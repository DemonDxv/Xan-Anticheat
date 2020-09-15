package dev.demon.xan.impl.checks.movement.speed;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;

@CheckInfo(name = "Speed", type = "C")
public class SpeedC extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {
            if (user.generalCancel() || user.getMovementData().isExplode()
                    || user.getLagProcessor().isLagging()
                    || user.getLagProcessor().isReallySpiking()
                    || user.getBlockData().liquidTicks > 0
                    || user.getBlockData().climbableTicks > 0) {
                return;
            }

            double maxSpeed = 0.36;

            if (user.getMovementData().isClientGround() && user.getMovementData().isLastClientGround()) {
                maxSpeed = 0.4873D;
            }

            if (user.getMovementData().getClientGroundTicks() > 9) {
                maxSpeed = 0.2873D;
            }

            if (!user.getMovementData().isClientGround() && user.getMovementData().isLastClientGround()) {
                maxSpeed += 0.26;
            }

            if (user.getMovementData().isClientGround() && !user.getMovementData().isLastClientGround()) {
                maxSpeed += 0.26;
            }

            if (user.getBlockData().iceTicks > 0) {
                maxSpeed += 0.2;
            }

            if (user.getVelocityData().getVelocityTicks() <= 20) {
                maxSpeed += user.getVelocityData().getHorizontalVelocityTrans();
            }

            if (user.getMovementData().getDeltaXZ() > maxSpeed) {
                alert(user, "S -> " + user.getMovementData().getDeltaXZ() + " MS -> " + maxSpeed);
            }
        }
    }
}
