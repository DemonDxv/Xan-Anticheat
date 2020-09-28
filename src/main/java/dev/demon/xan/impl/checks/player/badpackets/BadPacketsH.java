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

@CheckInfo(name = "BadPackets", type = "H")
public class BadPacketsH extends Check {

    private long lastFlying;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {

            lastFlying = System.currentTimeMillis();

        } else if (e instanceof BlockDigEvent) {

            if (TimeUtils.elapsed(lastFlying) < 5L) {
                alert(user, "F -> " + TimeUtils.elapsed(lastFlying));
            }
        }
    }
}
