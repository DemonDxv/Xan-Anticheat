package dev.demon.xan.impl.checks.movement.speed;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.location.CustomLocation;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "Speed", type = "A")
public class SpeedA extends Check {

    private double lastDeltaXZ;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {
            if (user.generalCancel()
                    || user.getBlockData().liquidTicks > 0
                    || user.getBlockData().climbableTicks > 0
                    || user.getMovementData().isExplode()
                    || user.generalCancel()
                    || TimeUtils.elapsed(user.getMovementData().getLastTeleport()) < 1000L) {
                return;
            }

            CustomLocation to = user.getMovementData().getTo(), from = user.getMovementData().getFrom();

            double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());

            float jumpMovementFactor = 0.02F;

            if (user.getMovementData().isLastSprint() || user.getMovementData().isSprinting()) {
                jumpMovementFactor = 0.025999999F;
            }

            double lastPredictedXZ = lastDeltaXZ * 0.91F + jumpMovementFactor;

            double predictedXZ = deltaXZ - lastPredictedXZ;

            if (user.getVelocityData().getVelocityTicks() <= 20) {
                lastPredictedXZ += user.getVelocityData().getHorizontalVelocityTrans();
            }

            if (deltaXZ > lastPredictedXZ && predictedXZ > 0.0 && deltaXZ > 0.15 && !user.getMovementData().isClientGround() && !user.getMovementData().isLastClientGround()) {
                alert(user, "PXZ -> " + predictedXZ);
            }

            lastDeltaXZ = deltaXZ;
        }
    }
}
