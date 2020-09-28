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
import org.bukkit.Bukkit;

@CheckInfo(name = "BadPackets", type = "E")
public class BadPacketsE extends Check {

    private boolean block;
    private long lastInteractAt;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            if (TimeUtils.elapsed(user.getCombatData().getLastUseEntityPacket()) < 100L) {
                if (block && TimeUtils.elapsed(lastInteractAt) > 1000L) {
                    alert(user, block + " " + TimeUtils.elapsed(lastInteractAt));
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
            if (((UseEntityEvent) e).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.INTERACT_AT) {
                lastInteractAt = System.currentTimeMillis();
            }
        }
    }
}
