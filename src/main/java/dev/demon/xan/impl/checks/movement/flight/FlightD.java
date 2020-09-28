package dev.demon.xan.impl.checks.movement.flight;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "Flight", type = "D")
public class FlightD extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {

            double deltaY = user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY();

            if ((System.currentTimeMillis() - user.getMovementData().getLastBlockJump()) < 1000L
                    && (deltaY <= 0.404445 || deltaY > 0.404444)
                    || user.getVelocityData().getVelocityTicks() <= 20
                    || user.getBlockData().liquidTicks > 0
                    || user.generalCancel()
                    || user.getBlockData().climbableTicks > 0
                    || user.getBlockData().blockAboveTicks > 0
                    || TimeUtils.elapsed(user.getMovementData().getLastTeleport()) < 1000L
                    || user.getBlockData().slimeTicks > 0) {
                return;
            }

            double limit = 0.41999998688697815F;

            if (user.getMiscData().getJumpPotionTicks() > 0 && user.getMiscData().isHasJumpPotion()) {
                limit = (limit + user.getMiscData().getJumpPotionMultiplyer() * 0.1F);
            }

            if (user.getBlockData().stairTicks > 0 || user.getBlockData().slabTicks > 0 || user.getBlockData().wallTicks > 0 || user.getBlockData().fenceTicks > 0) {
                limit = 0.5;
            }

            if (!user.getMovementData().isClientGround() && user.getMovementData().isLastClientGround()) {
                if (deltaY > limit) {
                    alert(user, "DY -> " + deltaY + " P -> " + limit);
                }
            }
        }
    }
}
