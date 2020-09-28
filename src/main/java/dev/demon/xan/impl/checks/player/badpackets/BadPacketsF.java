package dev.demon.xan.impl.checks.player.badpackets;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.api.user.User;
import dev.demon.xan.impl.events.BlockDigEvent;
import dev.demon.xan.impl.events.BlockSentEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.impl.events.UseEntityEvent;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "BadPackets", type = "F")
public class BadPacketsF extends Check {

    private long lastFlying;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {

            lastFlying = System.currentTimeMillis();

        } else if (e instanceof UseEntityEvent) {
            if ((System.currentTimeMillis() - lastFlying) < 5L) {
                if (violation++ > 3) {
                    alert(user, "F -> "+(System.currentTimeMillis() - lastFlying));
                }
            } else {
                violation = 0;
            }
        }
    }
}
