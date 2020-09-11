package dev.demon.xan.utils.processor;

import dev.demon.xan.Xan;
import dev.demon.xan.base.tinyprotocol.api.Packet;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.xan.base.user.User;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

@Setter
public class CombatProcessor {
    private User user;

    public void update(Object packet, String type) {
        if (user != null) {

            if (type.equalsIgnoreCase(Packet.Client.USE_ENTITY)) {

                WrappedInUseEntityPacket wrappedInUseEntityPacket = new WrappedInUseEntityPacket(packet, user.getPlayer());

                if (wrappedInUseEntityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK && wrappedInUseEntityPacket.getEntity() != null) {

                    if (wrappedInUseEntityPacket.getEntity() instanceof Player) {
                        User attackedUser = Xan.getInstance().getUserManager().getUser(wrappedInUseEntityPacket.getEntity().getUniqueId());
                        if (attackedUser != null) user.getCombatData().setTargetUser(attackedUser);
                    }

                    if (user.getCombatData().getLastEntityAttacked() != null) {
                        if (user.getCombatData().getLastEntityAttacked() != wrappedInUseEntityPacket.getEntity()) {
                            user.getCombatData().constantEntityTicks = 0;
                        } else {
                            user.getCombatData().constantEntityTicks++;
                        }
                        user.getCombatData().setLastUseEntityTick(user.getConnectedTick());
                    }

                    if (wrappedInUseEntityPacket.getEntity() instanceof Player || wrappedInUseEntityPacket.getEntity() instanceof Villager) {
                        user.getCombatData().setLastEntityAttacked(wrappedInUseEntityPacket.getEntity());
                        user.getCombatData().setLastUseEntityPacket(System.currentTimeMillis());
                    }
                }
            }
        }
    }
}