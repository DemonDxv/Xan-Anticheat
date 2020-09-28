package dev.demon.xan.impl.checks.player.badpackets;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.impl.events.*;
import dev.demon.xan.utils.time.TimeUtils;

@CheckInfo(name = "BadPackets", type = "K")
public class BadPacketsK extends Check {

    private boolean flying, flyingSpoof;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof AbilityInEvent) {

            flyingSpoof = ((AbilityInEvent) e).isFlying()
                    || ((AbilityInEvent) e).isAllowedFlight()
                    || ((AbilityInEvent) e).isInvulnerable()
                    || ((AbilityInEvent) e).isCreativeMode();

        }

        if (e instanceof AbilityOutEvent) {

            flying = ((AbilityOutEvent) e).isFlying()
                    || ((AbilityOutEvent) e).isAllowedFlight()
                    || ((AbilityOutEvent) e).isInvulnerable()
                    || ((AbilityOutEvent) e).isCreativeMode();

        }

        if (e instanceof FlyingEvent) {

            if (flying != flyingSpoof) {
          //      alert(user, "A (OUT) != A (IN)");
            }
        }
    }
}
