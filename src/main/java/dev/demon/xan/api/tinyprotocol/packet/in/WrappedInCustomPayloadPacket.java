package dev.demon.xan.api.tinyprotocol.packet.in;

import dev.demon.xan.api.tinyprotocol.api.NMSObject;
import dev.demon.xan.api.tinyprotocol.api.ProtocolVersion;
import dev.demon.xan.api.tinyprotocol.reflection.FieldAccessor;
import lombok.Getter;
import org.bukkit.entity.Player;

public class WrappedInCustomPayloadPacket extends NMSObject {
    private static String packet = NMSObject.Client.CUSTOM_PAYLOAD;

    private static FieldAccessor<String> channelAccessor;
    private static FieldAccessor<Object> dataAccessor;

    public WrappedInCustomPayloadPacket(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void updateObject() {

    }

    @Getter
    private String channel;

    @Getter
    private Object data;

    @Override
    public void process(Player player, ProtocolVersion version) {
        channelAccessor = fetchField(packet, String.class, 0);
        dataAccessor = fetchField(packet, Object.class, 1);
        this.channel = fetch(channelAccessor);
        this.data = fetch(dataAccessor);
    }
}
