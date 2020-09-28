package dev.demon.xan.impl.checks.player.badpackets;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.impl.events.BlockDigEvent;
import dev.demon.xan.impl.events.BlockSentEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.utils.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Flying;

import java.sql.Time;

@CheckInfo(name = "BadPackets", type = "I")
public class BadPacketsI extends Check {

    private long lastDigging, lastBlocking;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof BlockDigEvent) {

            lastDigging = System.currentTimeMillis();

        }

        if (e instanceof BlockSentEvent) {

            lastBlocking = System.currentTimeMillis();

        }

        if (e instanceof FlyingEvent) {
            if (TimeUtils.elapsed(lastBlocking) > 1000 && TimeUtils.elapsed(lastDigging) < 100) {
                if (violation++ > 5) {
                    alert(user, "B -> " + TimeUtils.elapsed(lastBlocking) + " D -> " + TimeUtils.elapsed(lastDigging));
                }
            } else violation -= Math.min(violation, 0.5);
        }
    }
}
