package dev.demon.xan.impl.checks.combat.velocity;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.math.MathUtil;
import dev.demon.xan.utils.time.TimeUtils;
import lombok.val;

import java.util.Map;

@CheckInfo(name = "Velocity", type = "D")
public class VelocityD extends Check {

    private double lastPlayerSpeed = 1, lastLastPlayerSpeed = 1;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (user != null) {
            if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {
                if (((FlyingEvent) e).isPos() || ((FlyingEvent) e).isLook()) {
                    if (user.getMovementData().isCollidesHorizontally()
                            || user.getBlockData().fenceTicks > 0
                            || user.getMovementData().isExplode()
                            || user.getBlockData().cactusTicks > 0) {

                        return;
                    }

                    if (user.getVelocityData().getVelocityTicks() < 4 && user.getVelocityData().getVelocityTicks() > 1) {
                        for (Map.Entry<Double, Short> doubleShortEntry : user.getVelocityData().getLastVelocityHorizontal().entrySet()) {
                            if (user.getMiscData().getTransactionIDVelocity() == (short) ((Map.Entry) doubleShortEntry).getValue()) {
                                user.getVelocityData().setHorizontalVelocityTrans((Double) ((Map.Entry) doubleShortEntry).getKey());
                                user.getVelocityData().getLastVelocityHorizontal().clear();
                            }
                        }

                        val yDelta = user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY();
                        if (yDelta < 0.42f) {

                            double playerSpeed = MathUtil.hypot(user.getMovementData().getTo().getX() - user.getMovementData().getFrom().getX(),
                                    user.getMovementData().getTo().getZ() - user.getMovementData().getFrom().getZ());

                            if (playerSpeed > 0) {
                                playerSpeed += MathUtil.moveFlying(user, user.getMovementData().getTo(), user.getMovementData().isLastClientGround());
                            }

                            if (TimeUtils.elapsed(user.getCombatData().getLastUseEntityPacket()) < 250
                                    || user.getMovementData().isLastSprint()) {
                                user.getVelocityData().setVelocityX(user.getVelocityData().getVelocityX() * 0.6D);
                                user.getVelocityData().setVelocityZ(user.getVelocityData().getVelocityZ() * 0.6D);
                                user.getVelocityData().setHorizontalVelocityTrans(user.getVelocityData().getHorizontalVelocityTrans() * 0.6);
                            }

                            double totalVelocity = Math.abs((playerSpeed + lastPlayerSpeed + lastLastPlayerSpeed) / user.getVelocityData().getHorizontalVelocityTrans());

                            if (user.getMiscData().getSpeedPotionTicks() > 0) {
                                totalVelocity += user.getMiscData().getSpeedPotionEffectLevel() * 0.06;
                            }

                            if (totalVelocity < 1) {
                                alert(user, "V -> " + totalVelocity);
                            }

                            lastPlayerSpeed = playerSpeed;
                            lastLastPlayerSpeed = lastPlayerSpeed;
                        }
                    }
                }
            }
        }
    }
}
