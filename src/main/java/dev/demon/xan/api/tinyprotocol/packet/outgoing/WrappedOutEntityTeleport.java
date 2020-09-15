package dev.demon.xan.api.tinyprotocol.packet.outgoing;


import dev.demon.xan.api.tinyprotocol.api.NMSObject;
import dev.demon.xan.api.tinyprotocol.reflection.FieldAccessor;
import lombok.Getter;

@Getter
public class WrappedOutEntityTeleport extends NMSObject {
    private static final String packet = Server.ENTITY_TELEPORT;

    // Fields
    private static FieldAccessor<Integer> fieldId = fetchField(packet, int.class, 0);
    private static FieldAccessor<Integer> fieldX = fetchField(packet, int.class, 1);
    private static FieldAccessor<Integer> fieldY = fetchField(packet, int.class, 2);
    private static FieldAccessor<Integer> fieldZ = fetchField(packet, int.class, 3);
    private static FieldAccessor<Byte> fieldYaw = fetchField(packet, byte.class, 0);
    private static FieldAccessor<Byte> fieldPitch = fetchField(packet, byte.class, 1);
    private static FieldAccessor<Boolean> fieldGround = fetchField(packet, Boolean.class, 0);

    // Decoded data
    private int id, x, y, z;
    private byte yaw, pitch;

    public WrappedOutEntityTeleport(Object packet) {
        super(packet);
    }

    @Override
    public void updateObject() {

    }

    public void setId(int id) {
        set(fieldId, id);
    }
}