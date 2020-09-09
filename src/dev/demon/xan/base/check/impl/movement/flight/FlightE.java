package dev.demon.xan.base.check.impl.movement.flight;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "Flight", type = "E")
public class FlightE extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {


            double deltaY = user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY();

            if ((System.currentTimeMillis() - user.getMovementData().getLastBlockJump()) < 1000L
                    && (deltaY <= 0.404445 || deltaY > 0.404444) || user.getVelocityData().getVelocityTicks() <= 20) {
                return;
            }

            double limit = 0.41999998688697815F;

            if (user.getMiscData().getJumpPotionTicks() > 0 && user.getMiscData().isHasJumpPotion()) {

                limit = (limit + user.getMiscData().getJumpPotionMultiplyer() * 0.1F);

            }

            if (!user.getMovementData().isClientGround() && user.getMovementData().isLastClientGround()) {
                if (deltaY < limit && deltaY > 0) {
                    alert(user, "DY -> " + deltaY + " P -> " + limit);
                }
            }
        }
    }
}
