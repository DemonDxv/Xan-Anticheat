package dev.demon.xan.impl.checks.combat.autoclicker;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.ArmAnimationEvent;
import dev.demon.xan.impl.events.BlockDigEvent;
import dev.demon.xan.impl.events.BlockSentEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.api.user.User;

@CheckInfo(name = "AutoClicker", type = "A")
public class AutoClickerA extends Check {
    private int ticks, cps;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            if (++ticks == 20) {
                if (cps >= 20) {
                    alert(user,"C -> " + cps);
                }
            }
        } else if (e instanceof ArmAnimationEvent) {
            cps++;
        }

        if (e instanceof BlockDigEvent || e instanceof BlockSentEvent) {
            ticks = 0;
            cps = 0;
        }
    }
}
