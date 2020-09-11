package dev.demon.xan.api.tinyprotocol.packet.in;

import dev.demon.xan.api.tinyprotocol.api.NMSObject;
import dev.demon.xan.api.tinyprotocol.api.ProtocolVersion;
import dev.demon.xan.api.tinyprotocol.reflection.FieldAccessor;
import lombok.Getter;
import org.bukkit.entity.Player;

public class WrappedInChatPacket extends NMSObject {
    private static String packet = Client.CHAT;

    private static FieldAccessor<String> messageAccessor = fetchField(packet, String.class, 0);

    public WrappedInChatPacket(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void updateObject() {

    }

    @Getter
    private String message;

    @Override
    public void process(Player player, ProtocolVersion version) {
        this.message = fetch(messageAccessor);
    }
}
