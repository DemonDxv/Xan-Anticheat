package dev.demon.xan.base.check.impl.player.badpackets;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.BlockDigEvent;
import dev.demon.xan.base.event.events.BlockSentEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.event.events.UseEntityEvent;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.base.user.User;
import org.bukkit.event.block.BlockEvent;

@CheckInfo(name = "BadPackets", type = "B")
public class BadPacketsB extends Check {

    private boolean block;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            block = false;
        }
        if (e instanceof BlockDigEvent || e instanceof BlockSentEvent) {
            block = true;
        }

        if (e instanceof UseEntityEvent) {
            if (((UseEntityEvent) e).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                if (block) {
                    alert(user, "B -> "+block);
                }
            }
        }
    }
}
