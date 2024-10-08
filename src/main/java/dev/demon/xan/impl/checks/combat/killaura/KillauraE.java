package dev.demon.xan.impl.checks.combat.killaura;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.location.CustomLocation;
import dev.demon.xan.utils.math.MathUtil;
import dev.demon.xan.utils.math.Verbose;

@CheckInfo(name = "Killaura", type = "E")
public class KillauraE extends Check {

    private Verbose verbose = new Verbose();

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            CustomLocation to = user.getMovementData().getTo(), from = user.getMovementData().getFrom();
            double pitch = Math.abs(to.getPitch() - from.getPitch());

            if (MathUtil.round(pitch, 1) == pitch && pitch > 0) {
                if (verbose.flag(5, 950L)) {
                    alert(user, "P -> " + pitch);
                }
            }
        }
    }
}
