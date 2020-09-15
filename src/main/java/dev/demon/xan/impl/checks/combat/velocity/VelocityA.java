package dev.demon.xan.impl.checks.combat.velocity;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.time.TimeUtils;

import java.util.Map;

@CheckInfo(name = "Velocity", type = "A")
public class VelocityA extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            if (user.getBlockData().climbableTicks > 0
                    || TimeUtils.elapsed(user.getMovementData().getLastFallDamage()) < 1000L
                    || user.getBlockData().blockAboveTicks > 0
                    || TimeUtils.elapsed(user.getMovementData().getLastTeleport()) < 1000L
                    || user.getBlockData().webTicks > 0
                    || user.getMovementData().isExplode()
                    || user.getBlockData().cactusTicks > 0) {
                return;
            }

            if (user.getVelocityData().getVelocityTicks() == 1) {
                for (Map.Entry<Double, Short> doubleShortEntry : user.getVelocityData().getLastVelocityVertical().entrySet()) {
                    if (user.getMiscData().getTransactionIDVelocity() == doubleShortEntry.getValue()) {
                        user.getVelocityData().setVerticalVelocityTrans(doubleShortEntry.getKey());
                        user.getVelocityData().getLastVelocityVertical().clear();
                    }
                }

                double deltaY = Math.abs(user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY());

                if (deltaY >= 0 && deltaY < (user.getVelocityData().getVerticalVelocityTrans() * 0.9995) && user.getVelocityData().getVerticalVelocity() < 1) {
                    alert(user, "VV -> " + (deltaY / user.getVelocityData().getVerticalVelocityTrans()));
                }
            }
        }
    }
}
