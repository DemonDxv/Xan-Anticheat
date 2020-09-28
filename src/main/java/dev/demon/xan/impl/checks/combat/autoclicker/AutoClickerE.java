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

@CheckInfo(name = "AutoClicker", type = "E")
public class AutoClickerE extends Check {

    private int movements;
    private final List<Integer> delays = new ArrayList<>(1000);


    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof ArmAnimationEvent) {
            if (movements < 10) {
                delays.add(movements);

                if (delays.size() == 1000) {

                    int outliers = (int) delays.stream()
                            .filter(delay -> delay > 3)
                            .count();

                    if (outliers < 8) {
                        if (violation++ > 4) {
                            alert(user, "O -> "+outliers);
                        }
                    } else violation -= Math.min(violation, 0.5);

                    delays.clear();
                }
            }
            movements = 0;
        } else if (e instanceof FlyingEvent) {
            movements++;
        }
    }
}
