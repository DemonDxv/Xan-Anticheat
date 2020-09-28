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

@CheckInfo(name = "AutoClicker", type = "D")
public class AutoClickerD extends Check {

    private int movements;
    private final List<Integer> delays = new ArrayList<>(50);
    private double lastStDev = -1;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof ArmAnimationEvent) {
            if (movements < 10) {
                delays.add(movements);

                if (delays.size() == 50) {
                    double std = MathUtil.getStandardDeviation(delays);

                    if (Math.abs(std - lastStDev) < 0.05) {
                        if (violation++ > 5) {
                            alert(user, "STDD -> "+Math.abs(std - lastStDev));
                        }
                    } else violation -= Math.min(violation, 1);


                    delays.clear();
                    lastStDev = std;
                }
            }
            movements = 0;
        } else if (e instanceof FlyingEvent) {
            movements++;
        }
    }
}
