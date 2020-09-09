package dev.demon.xan.base.check.impl.movement.speed;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.location.CustomLocation;
import dev.demon.xan.utils.math.MathUtil;
import org.bukkit.Bukkit;

@CheckInfo(name = "Speed", type = "B")
public class SpeedB extends Check {

    private double lastDeltaXZ;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent) {

            CustomLocation to = user.getMovementData().getTo(), from = user.getMovementData().getFrom();

            double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());

            boolean onGround = user.getMovementData().isClientGround(), lastOnGround = user.getMovementData().isLastClientGround();

            if (lastDeltaXZ < 0.005) {
                lastDeltaXZ = 0.0;
            }

            if (!onGround && !lastOnGround) {
                lastDeltaXZ *= 0.6F;
            }

            if (!onGround && lastOnGround) {
                lastDeltaXZ += 0.2;
            }

            lastDeltaXZ += MathUtil.moveFlying(user, to, lastOnGround);

            double prediction = Math.abs(deltaXZ - lastDeltaXZ);


            if (onGround && !lastOnGround || !onGround && lastOnGround) {
                if (prediction > 0.05) {
                    if (violation++ > 1) {
                        alert(user, "P -> "+prediction);
                    }
                } else violation -= Math.min(violation, 0.75);
            }



            lastDeltaXZ = deltaXZ * 0.91F;
        }
    }
}
