package dev.demon.xan.base.check.impl.player.badpackets;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.ArmAnimationEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.event.events.UseEntityEvent;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.base.user.User;

@CheckInfo(name = "BadPackets", type = "D")
public class BadPacketsD extends Check {

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {

        }
    }
}
