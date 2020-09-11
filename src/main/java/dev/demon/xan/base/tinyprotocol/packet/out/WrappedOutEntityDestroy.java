package dev.demon.xan.base.tinyprotocol.packet.out;

import dev.demon.xan.base.tinyprotocol.api.NMSObject;
import lombok.Getter;

@Getter
public class WrappedOutEntityDestroy extends NMSObject {
    private static final String packet = Server.ENTITY_DESTROY;

    public WrappedOutEntityDestroy(int[] ids) {
        setPacket(packet, ids);
    }

    @Override
    public void updateObject() {

    }
}