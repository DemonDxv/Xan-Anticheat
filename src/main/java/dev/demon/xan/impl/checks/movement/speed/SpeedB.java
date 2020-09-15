package dev.demon.xan.impl.checks.movement.speed;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.location.CustomLocation;
import dev.demon.xan.utils.math.MathUtil;

@CheckInfo(name = "Speed", type = "B")
public class SpeedB extends Check {

    private double lastDeltaXZ;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {

            if (user.generalCancel()
                    || user.getBlockData().liquidTicks > 0
                    || user.getBlockData().climbableTicks > 0) {
                return;
            }

            CustomLocation to = user.getMovementData().getTo(), from = user.getMovementData().getFrom();

            double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());

            boolean onGround = user.getMovementData().isClientGround(), lastOnGround = user.getMovementData().isLastClientGround();

            if (lastDeltaXZ < 0.005) {
                lastDeltaXZ = 0.0;
            }

            if (!onGround && !lastOnGround) {
                lastDeltaXZ *= 0.6F;
            }

            if (!onGround && lastOnGround) {
                lastDeltaXZ += 0.2;
            }

            lastDeltaXZ += MathUtil.moveFlying(user, to, lastOnGround);

            double prediction = Math.abs(deltaXZ - lastDeltaXZ);

            if (user.getVelocityData().getVelocityTicks() <= 20) {
                deltaXZ -= user.getVelocityData().getHorizontalVelocityTrans();
            }

            if (onGround && !lastOnGround || !onGround && lastOnGround) {
                if (deltaXZ > (lastDeltaXZ + 0.001) && prediction > 0.0) {
                    alert(user, "P -> " + prediction + " LDXZ -> " + lastDeltaXZ + " DXZ -> " + deltaXZ);
                }
            }

            lastDeltaXZ = deltaXZ * 0.91F;
        }
    }
}
