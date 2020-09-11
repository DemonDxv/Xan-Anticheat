package dev.demon.xan.impl.checks.combat.killaura;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.ArmAnimationEvent;
import dev.demon.xan.impl.events.UseEntityEvent;
import dev.demon.xan.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.api.user.User;

@CheckInfo(name = "Killaura", type = "A")
public class KillauraA extends Check {

    private int hits, swings;

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof ArmAnimationEvent) {
            swings++;

            double pitch = user.getMovementData().getTo().getPitch() - user.getMovementData().getFrom().getPitch();
            double yaw = user.getMovementData().getTo().getYaw() - user.getMovementData().getFrom().getYaw();

            if (swings >= 100 && yaw > 2.5 && pitch > 0.0) {
                if (hits >= 75) {
                    if (violation++ > 5) {
                        alert(user, "S -> " + swings + " H -> " + hits);
                    }
                }else violation -= Math.min(violation, 0.25);
                swings = hits = 0;
            }
        }else if (e instanceof UseEntityEvent) {
            if (((UseEntityEvent) e).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                hits++;
            }
        }
    }
}

