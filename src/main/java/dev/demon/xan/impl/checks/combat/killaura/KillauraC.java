package dev.demon.xan.impl.checks.combat.killaura;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.location.CustomLocation;

@CheckInfo(name = "Killaura", type = "C")
public class KillauraC extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {

            CustomLocation to = user.getMovementData().getTo(), from = user.getMovementData().getFrom();

            double yaw = Math.abs(to.getYaw() - from.getYaw());
            double pitch = Math.abs(to.getPitch() - from.getPitch());

            if (pitch % 1.0 == 0 && pitch % 1.5 != 0.0 && yaw > 2.0) {
                alert(user, "P -> "+pitch);
            }
        }
    }
}
