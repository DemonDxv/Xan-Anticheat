package dev.demon.xan.base.check.impl.movement.speed;

import dev.demon.xan.base.check.Check;
import dev.demon.xan.base.check.CheckInfo;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.events.FlyingEvent;
import dev.demon.xan.base.user.User;

@CheckInfo(name = "Speed", type = "C")
public class SpeedC extends Check {
    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingEvent && user.getConnectedTick() > 100) {
            if (user.generalCancel() || user.getBlockData().liquidTicks > 0 || user.getBlockData().climbableTicks > 0) {
                return;
            }
            if (user.getMovementData().getClientGroundTicks() > 9 && user.getMovementData().isClientGround()) {
                if (user.getMovementData().getDeltaXZ() > 0.2873D) {
                    alert(user, "DXZ -> "+user.getMovementData().getDeltaXZ());
                }
            }
        }
    }
}
