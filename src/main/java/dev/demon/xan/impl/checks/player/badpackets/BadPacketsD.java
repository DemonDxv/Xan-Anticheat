package dev.demon.xan.impl.checks.player.badpackets;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.impl.events.BlockDigEvent;
import dev.demon.xan.impl.events.BlockSentEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.impl.events.UseEntityEvent;
import dev.demon.xan.utils.time.TimeUtils;
import org.bukkit.Bukkit;

@CheckInfo(name = "BadPackets", type = "D")
public class BadPacketsD extends Check {

    private boolean block;
    private long lastInteract;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            if (TimeUtils.elapsed(user.getCombatData().getLastUseEntityPacket()) < 100L) {
                if (block && TimeUtils.elapsed(lastInteract) > 1000L && !user.getLagProcessor().isLagging()) {
                    alert(user, block + " " + TimeUtils.elapsed(lastInteract));
                }
            }
        }

        if (e instanceof BlockDigEvent) {
            block = false;
        }

        if (e instanceof BlockSentEvent) {
            block = true;
        }

        if (e instanceof UseEntityEvent) {
            if (((UseEntityEvent) e).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.INTERACT) {
                lastInteract = System.currentTimeMillis();
            }
        }
    }
}
