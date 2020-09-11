package dev.demon.xan.base.check.impl.combat.velocity;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.math.MathUtil;
import dev.demon.xan.utils.time.TimeUtils;
import lombok.val;

import java.util.Map;

@CheckInfo(name = "Velocity", type = "C")
public class VelocityC extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (user != null) {

            if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {
                if (((FlyingEvent) e).isPos() || ((FlyingEvent) e).isLook()) {

                    if (user.getMovementData().isCollidesHorizontally() || user.getBlockData().fenceTicks > 0) {
                        violation = 0;
                        return;
                    }

                    //For when the player blocks there movement is multiplied by 0.2F
                    if (user.getPlayer().isBlocking()) {
                        user.getVelocityData().setVelocityX(user.getVelocityData().getVelocityX() * 0.2F);
                        user.getVelocityData().setVelocityZ(user.getVelocityData().getVelocityZ() * 0.2F);
                    }


                    //When the client receives a transaction packet after taking velocity, it is equal to 1
                    if (user.getVelocityData().getVelocityTicks() == 1) {

                        ///This is setting the players horizontal velocity if they send back a transaction packet
                        for (Map.Entry<Double, Short> doubleShortEntry : user.getVelocityData().getLastVelocityHorizontal().entrySet()) {
                            if (user.getMiscData().getTransactionIDVelocity() == (short) ((Map.Entry) doubleShortEntry).getValue()) {
                                user.getVelocityData().setHorizontalVelocityTrans((Double) ((Map.Entry) doubleShortEntry).getKey());
                                user.getVelocityData().getLastVelocityHorizontal().clear();
                            }
                        }


                        //Checking to make sure the player is not on ground and their y difference is greater than 0
                        val yDelta = user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY();
                        if (yDelta > 0.0 && !user.getMovementData().isClientGround() && user.getMovementData().isLastClientGround() && yDelta < 0.42f) {

                            //Calculating the players movement speed using X, pastX, Z, and pastZ
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


                            //Calculating the players total player velocity by dividing the 2 movement speeds by the transaction horizontal velocity
                            double totalVelocity = Math.abs(playerSpeed / user.getVelocityData().getHorizontalVelocityTrans());

                            if (user.getMiscData().getSpeedPotionTicks() > 0) {
                                totalVelocity += user.getMiscData().getSpeedPotionEffectLevel() * 0.06;
                            }


                            if (totalVelocity < 1) {
                                alert(user, "V -> " + totalVelocity);
                            }
                        }
                    }
                }
            }
        }
    }
}
