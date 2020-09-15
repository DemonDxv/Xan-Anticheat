package dev.demon.xan.impl.checks.movement.speed;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "Speed", type = "C")
public class SpeedC extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {
            if (user.generalCancel() || user.getMovementData().isExplode()
                    || user.getLagProcessor().isReallySpiking()
                    || user.getBlockData().liquidTicks > 0
                    || user.getBlockData().climbableTicks > 0
                    || TimeUtils.elapsed(user.getMovementData().getLastTeleport()) < 1000L) {
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

            if (TimeUtils.elapsed(user.getMovementData().getLastBlockJump()) < 1000L) {
                maxSpeed = 0.642;
            }

            if (user.getBlockData().halfBlockTicks > 0) {
                maxSpeed += 0.3;
            }

            if (user.getBlockData().blockAboveTicks > 0) {
                maxSpeed += 0.2;
            }

            if (user.getMiscData().getSpeedPotionTicks() > 0) {
                maxSpeed += user.getMiscData().getSpeedPotionEffectLevel() * 0.06;
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
