package dev.demon.xan.impl.checks.player.badpackets;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.ArmAnimationEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.impl.events.UseEntityEvent;
import dev.demon.xan.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.api.user.User;

@CheckInfo(name = "BadPackets", type = "C")
public class BadPacketsC extends Check {

    private boolean swing;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            swing = false;
        }
        if (e instanceof ArmAnimationEvent) {
            swing = true;
        }

        if (e instanceof UseEntityEvent) {
            if (((UseEntityEvent) e).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                if (!swing && !user.getLagProcessor().isLagging()) {
                    alert(user, "S -> " + swing);
                }
            }
        }
    }
}
