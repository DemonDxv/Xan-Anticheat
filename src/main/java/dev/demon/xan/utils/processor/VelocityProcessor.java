package dev.demon.xan.utils.processor;

import dev.demon.xan.api.tinyprotocol.api.Packet;
import dev.demon.xan.api.tinyprotocol.api.TinyProtocolHandler;
import dev.demon.xan.api.tinyprotocol.packet.in.WrappedInTransactionPacket;
import dev.demon.xan.api.tinyprotocol.packet.outgoing.WrappedOutTransaction;
import dev.demon.xan.api.tinyprotocol.packet.outgoing.WrappedOutVelocityPacket;
import dev.demon.xan.api.user.User;
import dev.demon.xan.utils.math.MathUtil;
import dev.demon.xan.utils.time.TimeUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VelocityProcessor {
    private User user;

    public void update(Object packet, String type) {
        if (user != null) {
            if (type.equalsIgnoreCase(Packet.Server.ENTITY_VELOCITY)) {
                WrappedOutVelocityPacket wrappedOutVelocityPacket = new WrappedOutVelocityPacket(packet, user.getPlayer());

                if (wrappedOutVelocityPacket.getId() == user.getPlayer().getEntityId()) {

                    user.getLagProcessor().setHitTime(System.currentTimeMillis());


                    WrappedOutTransaction wrappedOutTransaction = new WrappedOutTransaction(0, user.getMiscData().getTransactionIDVelocity(), false);
                    TinyProtocolHandler.getInstance().getChannel().sendPacket(user.getPlayer(), wrappedOutTransaction.getObject());

                    user.getVelocityData().setLastVelocity(System.currentTimeMillis());

                    double velocityX = wrappedOutVelocityPacket.getX();
                    double velocityY = wrappedOutVelocityPacket.getY();
                    double velocityZ = wrappedOutVelocityPacket.getZ();

                    user.getVelocityData().setVelocityX(velocityX);
                    user.getVelocityData().setVelocityY(velocityY);
                    user.getVelocityData().setVelocityZ(velocityZ);

                    //user.getVelocityData().setVelocityX(user.getVelocityData().getVelocityX() * 0.6D);
                    //user.getVelocityData().setVelocityZ(user.getVelocityData().getVelocityZ() * 0.6D);

                    double horizontal = MathUtil.hypot(user.getVelocityData().getVelocityX(), user.getVelocityData().getVelocityZ());
                    double vertical = Math.pow(user.getVelocityData().getVelocityY() + 2.0, 2.0) * 5.0;

                    user.getVelocityData().setHorizontalVelocity(horizontal);
                    user.getVelocityData().setVerticalVelocity(vertical);


                    if (user.getMovementData().isOnGround() && user.getPlayer().getLocation().getY() % 1.0 == 0.0) {
                        user.getVelocityData().setVerticalVelocity(velocityY);
                    }

                    user.getCombatData().setLastVelocitySqr((Math.sqrt(velocityX * velocityX + velocityZ * velocityZ) * 0.2));



                }
            }

            if (type.equalsIgnoreCase(Packet.Client.FLYING) || type.equalsIgnoreCase(Packet.Client.POSITION) || type.equalsIgnoreCase(Packet.Client.POSITION_LOOK) || type.equalsIgnoreCase(Packet.Client.LOOK)) {
                if (TimeUtils.elapsed(user.getCombatData().getLastUseEntityPacket()) < 250
                        || user.getMovementData().isLastSprint()) {

                    // user.getVelocityData().setVelocityX(user.getVelocityData().getVelocityX() * 0.6D);
                    //   user.getVelocityData().setVelocityZ(user.getVelocityData().getVelocityZ() * 0.6D);
                    //user.getVelocityData().setHorizontalVelocityTrans(user.getVelocityData().getHorizontalVelocityTrans() * 0.6);

                }
            }

            if (type.equalsIgnoreCase(Packet.Client.TRANSACTION)) {
                WrappedInTransactionPacket wrappedInTransactionPacket = new WrappedInTransactionPacket(packet, user.getPlayer());

                short id = wrappedInTransactionPacket.getAction();
                short currentIDVelocity = user.getMiscData().getTransactionIDVelocity();


                if (id == currentIDVelocity) {
                    user.getLagProcessor().setVelocityPing((System.currentTimeMillis() - user.getLagProcessor().getHitTime()));
                    user.getVelocityData().getLastVelocityHorizontal().put(user.getVelocityData().getHorizontalVelocity(), currentIDVelocity);
                    user.getVelocityData().getLastVelocityVertical().put(user.getVelocityData().getVerticalVelocity(), currentIDVelocity);
                    user.getVelocityData().setVelocityTicks(0);
                }
            }
        }
    }
}
