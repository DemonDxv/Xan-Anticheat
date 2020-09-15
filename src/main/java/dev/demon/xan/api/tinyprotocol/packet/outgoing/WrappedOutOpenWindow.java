package dev.demon.xan.api.tinyprotocol.packet.outgoing;

import dev.demon.xan.api.tinyprotocol.api.NMSObject;
import dev.demon.xan.api.tinyprotocol.api.ProtocolVersion;
import dev.demon.xan.api.tinyprotocol.packet.types.WrappedChatMessage;
import dev.demon.xan.api.tinyprotocol.reflection.FieldAccessor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class WrappedOutOpenWindow extends NMSObject {

    private static String packet = Server.OPEN_WINDOW;

    public WrappedOutOpenWindow(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void updateObject() {

    }

    public WrappedOutOpenWindow(int id, String name, WrappedChatMessage msg, int size) {
        setPacket(packet, id, name, msg.getObject(), size);
    }

    private static FieldAccessor<Integer> idField = fetchField(packet, int.class, 0);
    private static FieldAccessor<String> nameField = fetchField(packet, String.class, 0);
    private static FieldAccessor<Object> chatCompField = fetchField(packet, Object.class, 2);
    private static FieldAccessor<Integer> inventorySize = fetchField(packet, int.class, 1);

    private int id;
    private String name;
    private WrappedChatMessage chatComponent;
    private int size;

    @Override
    public void process(Player player, ProtocolVersion version) {
        id = fetch(idField);
        name = fetch(nameField);
        chatComponent = new WrappedChatMessage(fetch(chatCompField));
        size = fetch(inventorySize);
    }
}
