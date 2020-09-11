package dev.demon.xan.base.check.impl.combat.killaura;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.math.MathUtil;

@CheckInfo(name = "Killaura", type = "B")
public class KillauraB extends Check {

    private double offset = Math.pow(2.0, 24.0);
    private double lastDiff;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {
            if (((FlyingEvent) e).isLook()) {

                if ((System.currentTimeMillis() - user.getCombatData().getLastUseEntityPacket() > 2000L)) {
                    violation = 0;
                }

                double diff = Math.abs(user.getMovementData().getTo().getPitch() - user.getMovementData().getFrom().getPitch());

                long gcd = MathUtil.gcd((long) (diff * offset), (long) (lastDiff * offset));

                if (user.getMovementData().getTo().getYaw() != user.getMovementData().getFrom().getYaw()
                        && user.getMovementData().getTo().getPitch() != user.getMovementData().getFrom().getPitch()) {
                    if (diff > 0 && Math.abs(user.getMovementData().getTo().getPitch()) != 90.0) {
                        if ((System.currentTimeMillis() - user.getCombatData().getLastUseEntityPacket() < 720L)) {
                            if (gcd < 131072L) {
                                if (violation < 25) violation+=2;
                            }else {
                                if (violation > 0) violation--;
                            }
                        }
                    }
                }
                if (violation > 20) {
                    alert(user, "GCD -> "+gcd);
                }

                lastDiff = diff;
            }
        }
    }
}