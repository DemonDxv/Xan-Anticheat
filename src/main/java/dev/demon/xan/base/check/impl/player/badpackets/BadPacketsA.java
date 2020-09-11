package dev.demon.xan.base.check.impl.player.badpackets;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;

@CheckInfo(name = "BadPackets", type = "A")
public class BadPacketsA extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            if (Math.abs(((FlyingEvent) e).getPitch()) > 90.0) {
                alert(user, ""+((FlyingEvent) e).getPitch());
            }
        }
    }
}
