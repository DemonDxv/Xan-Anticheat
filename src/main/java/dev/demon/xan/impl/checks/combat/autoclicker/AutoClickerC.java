package dev.demon.xan.impl.checks.combat.autoclicker;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.user.User;
import dev.demon.xan.impl.events.ArmAnimationEvent;
import dev.demon.xan.impl.events.FlyingEvent;
import dev.demon.xan.utils.math.MathUtil;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "AutoClicker", type = "C")
public class AutoClickerC extends Check {

    private int movements;
    private final List<Integer> delays = new ArrayList<>(500);

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof ArmAnimationEvent) {
            if (movements < 10) {
                delays.add(movements);

                if (delays.size() == 500) {
                    double kurtosis = MathUtil.getKurtosis(delays);

                    if (kurtosis < 0D) {
                        if (violation++ > 1) {
                            alert(user, "K -> "+kurtosis);
                        }
                    } else violation -= Math.min(violation, 0.125);

                    delays.clear();
                }
            }
            movements = 0;
        } else if (e instanceof FlyingEvent) {
            movements++;
        }
    }
}
