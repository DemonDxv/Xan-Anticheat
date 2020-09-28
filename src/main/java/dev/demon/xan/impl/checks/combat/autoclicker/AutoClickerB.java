package dev.demon.xan.impl.checks.combat.autoclicker;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.impl.events.ArmAnimationEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.utils.math.MathUtil;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "AutoClicker", type = "B")
public class AutoClickerB extends Check {

    private int movements;
    private final List<Integer> delays = new ArrayList<>();

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof ArmAnimationEvent) {
            if (movements < 10) {
                delays.add(movements);

                if (delays.size() == 100) {
                    double std = MathUtil.getStandardDeviation(delays);

                    if (std < 0.45) {
                        if (violation++ > 2) {
                            alert(user, "STD -> "+std);
                        }
                    } else violation -= Math.min(violation, 0.25);

                    delays.clear();
                }
            }
            movements = 0;
        } else if (e instanceof FlyingEvent) {
            movements++;
        }
    }
}
