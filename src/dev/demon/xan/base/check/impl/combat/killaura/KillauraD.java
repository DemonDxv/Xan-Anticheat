package dev.demon.xan.base.check.impl.combat.killaura;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.location.CustomLocation;

@CheckInfo(name = "Killaura", type = "D")
public class KillauraD extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {

            CustomLocation to = user.getMovementData().getTo(), from = user.getMovementData().getFrom();

            double yaw = Math.abs(to.getYaw() - from.getYaw());
            double pitch = Math.abs(to.getPitch() - from.getPitch());

            if (yaw % 1.0 == 0 && yaw % 1.5 != 0.0 && yaw > 2.0) {
                alert(user, "Y -> "+pitch);
            }
        }
    }
}
