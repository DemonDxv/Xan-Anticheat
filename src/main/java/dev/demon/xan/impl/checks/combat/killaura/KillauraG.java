package dev.demon.xan.impl.checks.combat.killaura;

import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.check.CheckInfo;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.impl.events.UseEntityEvent;
import dev.demon.xan.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.api.user.User;

import java.util.LinkedList;

@CheckInfo(name = "Killaura", type = "G")
public class KillauraG extends Check {

    public LinkedList<Double> pitchSameCount = new LinkedList<>();

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof UseEntityEvent) {
            if (((UseEntityEvent) e).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                double pitch = Math.abs(user.getMovementData().getTo().getPitch() - user.getMovementData().getFrom().getPitch());
                double yaw = Math.abs(user.getMovementData().getTo().getYaw() - user.getMovementData().getFrom().getYaw());


                if (yaw > 2 && pitch < 0.2) {
                    pitchSameCount.add(pitch);
                }

                if (pitchSameCount.size() == 25) {

                    double average = 0.0;

                    for (double i : pitchSameCount) {
                        average += i;
                    }

                    average /= pitchSameCount.size();

                    double stdDev = 0.0;

                    for (double j : pitchSameCount) {
                        stdDev += Math.pow(j - average, 2.0);
                    }

                    stdDev /= pitchSameCount.size();
                    stdDev = Math.sqrt(stdDev);

                    if (stdDev < 0.1) {
                        alert(user, "D -> "+stdDev);
                    }

                    pitchSameCount.clear();
                }
            }
        }
    }
}