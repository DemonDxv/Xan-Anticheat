package dev.demon.xan.impl.checks.player.badpackets;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;

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
