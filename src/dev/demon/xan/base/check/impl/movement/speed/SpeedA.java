package dev.demon.xan.base.check.impl.movement.speed;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.location.CustomLocation;

@CheckInfo(name = "Speed", type = "A")
public class SpeedA extends Check {

    private double lastDeltaXZ;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {

            CustomLocation to = user.getMovementData().getTo(), from = user.getMovementData().getFrom();

            double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());

            float jumpMovementFactor = 0.02F;
            if (user.getMovementData().isLastSprint() || user.getMovementData().isSprinting()) {
                jumpMovementFactor = 0.025999999F;
            }

            double lastPredictedXZ = lastDeltaXZ * 0.91F + jumpMovementFactor;

            double predictedXZ = deltaXZ - lastPredictedXZ;


            if (deltaXZ > lastPredictedXZ && !user.getMovementData().isClientGround() && !user.getMovementData().isLastClientGround()) {
                alert(user, "PXZ -> "+predictedXZ);
            }

            lastDeltaXZ = deltaXZ;


        }
    }
}
