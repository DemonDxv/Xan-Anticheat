package dev.demon.xan.impl.checks.combat.velocity;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "Velocity", type = "B")
public class VelocityB extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {
            if (user.getBlockData().climbableTicks > 0 || TimeUtils.elapsed(user.getMovementData().getLastFallDamage()) < 1000L || user.getBlockData().blockAboveTicks > 0 || TimeUtils.elapsed(user.getMovementData().getLastTeleport()) < 1000L || user.getBlockData().webTicks > 0) {
                return;
            }
            if (user.getVelocityData().getVelocityTicks() < 3 && user.getConnectedTick() > 100) {

                double deltaY = Math.abs(user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY());
                double velocity = user.getVelocityData().getVelocityY();
                velocity -= 0.08;
                velocity *= 0.98;

                double ratio = deltaY / velocity;

                if (ratio <= 0.9999 && ratio > 0) {
                    alert(user, "VV -> " + (deltaY / velocity));
                }
            }
        }
    }
}
