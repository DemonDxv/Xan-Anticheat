package dev.demon.xan.impl.checks.movement.speed;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.utils.location.CustomLocation;

@CheckInfo(name = "Speed", type = "A")
public class SpeedA extends Check {

    private double lastDeltaXZ;
    private float friction, getAIMoveSpeed;

    /** Detecting speeds via Air Friction change, and Ground Friction change **/

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent  && user.getConnectedTick() > 100) {

            if (user.generalCancel()) {
                return;
            }

            CustomLocation to = user.getMovementData().getTo(), from = user.getMovementData().getFrom();
            double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());

            double prediction = lastDeltaXZ * 0.91F;

            if (user.getMovementData().getClientGroundTicks() > 4) {
                prediction *= 0.6F;
            }
            if (!user.getMovementData().isClientGround() && user.getMovementData().isLastClientGround()) {
                prediction += 0.2;
            }

            if (user.getVelocityData().getVelocityTicks() <= 20) {
                prediction += user.getVelocityData().getHorizontalVelocityTrans();
            }

            if (user.getBlockData().liquidTicks > 0) {
                prediction += 0.021;
            }

            float strafe = 0.98F, forward = 0.98F;
            float f = strafe * strafe + forward * forward;

            float var3 = (0.6F * 0.91F);
            getAIMoveSpeed = 0.1F;

            if (user.getMovementData().isSprinting()) {
                getAIMoveSpeed = 0.13000001F;
            }

            float var4 = 0.16277136F / (var3 * var3 * var3);

            if (user.getMovementData().isLastClientGround()) {
                friction = getAIMoveSpeed * var4;
            } else {
                friction = 0.026F;
            }

            if (f >= 1.0E-4F) {
                f = (float) Math.sqrt(f);
                if (f < 1.0F) {
                    f = 1.0F;
                }
                f = friction / f;
                strafe = strafe * f;
                forward = forward * f;
                float f1 = (float) Math.sin(to.getYaw() * (float) Math.PI / 180.0F);
                float f2 = (float) Math.cos(to.getYaw() * (float) Math.PI / 180.0F);
                float motionXAdd = (strafe * f2 - forward * f1);
                float motionZAdd = (forward * f2 + strafe * f1);
                prediction += Math.hypot(motionXAdd, motionZAdd);
            }

            double difference = Math.abs(deltaXZ - prediction);

            if (deltaXZ > prediction && deltaXZ > 0.15 && difference >= 0.02) {
                if (violation++ > 1.0) {
                    alert(user, "DXZ -> " + deltaXZ + " P -> " + prediction + " DF -> " + difference);
                }
            }else violation -= Math.min(violation, 0.125);




            lastDeltaXZ = deltaXZ;
        }
    }
}
