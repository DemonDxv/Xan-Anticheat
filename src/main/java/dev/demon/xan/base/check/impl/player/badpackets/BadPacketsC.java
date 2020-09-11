package dev.demon.xan.base.check.impl.player.badpackets;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.*;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.base.user.User;

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
                if (!swing) {
                    alert(user, "S -> "+swing);
                }
            }
        }
    }
}
