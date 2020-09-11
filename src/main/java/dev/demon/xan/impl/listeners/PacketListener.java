package dev.demon.xan.impl.listeners;


import dev.demon.xan.Xan;
import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.event.AnticheatListener;
import dev.demon.xan.api.event.Listen;
import dev.demon.xan.api.tinyprotocol.api.NMSObject;
import dev.demon.xan.api.tinyprotocol.api.Packet;
import dev.demon.xan.api.tinyprotocol.packet.in.*;
import dev.demon.xan.api.tinyprotocol.packet.out.WrappedOutRelativePosition;
import dev.demon.xan.api.tinyprotocol.packet.out.WrappedOutVelocityPacket;
import dev.demon.xan.api.user.User;
import dev.demon.xan.impl.events.*;

public class PacketListener implements AnticheatListener {

    @Listen
    public void onPacket(PacketEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {

            user.getMovementProcessor().update(e.getPacket(), e.getType());

            user.getCombatProcessor().update(e.getPacket(), e.getType());

            user.getLagProcessor().update(e.getPacket(), e.getType());

            user.getVelocityProcessor().update(e.getPacket(), e.getType());

            //user.getOtherProcessor().update(e.getPacket(), e.getType());
            //user.getPredictionProcessor().update(e);

            AnticheatEvent event = e;
            if (e.isPacketMovement()) {
                WrappedInFlyingPacket packet = new WrappedInFlyingPacket(e.getPacket(), e.getPlayer());
                event = new FlyingEvent(packet.getX(), packet.getY(), packet.getZ(), packet.getPitch(), packet.getYaw(), packet.isGround(), packet.isPos(), packet.isLook());
            }else if (e.getType().equalsIgnoreCase(Packet.Client.KEEP_ALIVE)) {
                event = new KeepAliveEvent();
            }else if (e.getType().equalsIgnoreCase(Packet.Client.USE_ENTITY)) {
                WrappedInUseEntityPacket packet = new WrappedInUseEntityPacket(e.getPacket(), e.getPlayer());
                event = new UseEntityEvent(packet.getEntity(), packet.getAction());
            }else if (e.getType().equalsIgnoreCase(Packet.Client.TRANSACTION)) {
                WrappedInTransactionPacket packet = new WrappedInTransactionPacket(e.getPacket(), e.getPlayer());
                event = new TransactionEvent(packet.getId(), packet.getAction(), packet.isAccept());
            }else if (e.getType().equalsIgnoreCase(Packet.Client.ARM_ANIMATION)) {
                event = new ArmAnimationEvent();
            }else if (e.getType().equalsIgnoreCase(Packet.Client.ENTITY_ACTION)) {
                WrappedInEntityActionPacket packet = new WrappedInEntityActionPacket(e.getPacket(), e.getPlayer());
                event = new PlayerActionEvent(packet.getAction());
            }else if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {
                WrappedInBlockPlacePacket packet = new WrappedInBlockPlacePacket(e.getPacket(), e.getPlayer());
                event = new BlockSentEvent(packet.getVecX(), packet.getVecY(), packet.getVecZ(), packet.getFace(), packet.getPosition(), packet.getItemStack());
            }else if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_DIG)) {
                WrappedInBlockDigPacket packet = new WrappedInBlockDigPacket(e.getPacket(), e.getPlayer());
                event = new BlockDigEvent(packet.getAction(), packet.getDirection(), packet.getPosition());
            }else if (e.getType().equalsIgnoreCase(Packet.Server.ENTITY_VELOCITY)) {
                WrappedOutVelocityPacket packet = new WrappedOutVelocityPacket(e.getPacket(), e.getPlayer());
                event = new VelocityEvent(packet.getId(), packet.getX(), packet.getY(), packet.getZ());
            }else if (e.getType().contains(NMSObject.Server.ENTITY) || e.getType().contains(NMSObject.Server.REL_LOOK) || e.getType().contains(NMSObject.Server.REL_POSITION_LOOK) || e.getType().contains(NMSObject.Server.REL_POSITION)) {
                WrappedOutRelativePosition packet = new WrappedOutRelativePosition(e.getPacket(), e.getPlayer());
                event = new RelMoveEvent(packet.getX(), packet.getY(), packet.getZ(), packet.getPitch(), packet.getYaw(), packet.isGround(), packet.isPos(), packet.isLook());
            }

            AnticheatEvent finalEvent = event;
            user.checks.stream().filter(check -> check.enabled).forEach(check -> check.onHandle(user, finalEvent));
        }
    }
}
