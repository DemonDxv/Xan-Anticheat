package dev.demon.xan.api.tinyprotocol.packet.outgoing;

import dev.demon.xan.api.tinyprotocol.api.NMSObject;
import dev.demon.xan.api.tinyprotocol.api.ProtocolVersion;
import dev.demon.xan.api.tinyprotocol.reflection.FieldAccessor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class WrappedOutHeldItemSlot extends NMSObject {
    private static String packet = Server.HELD_ITEM;
    private FieldAccessor<Integer> slotField = fetchField(packet, int.class, 0);

    private int slot;

    public WrappedOutHeldItemSlot(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void updateObject() {

    }

    public WrappedOutHeldItemSlot(int slot) {
        this.slot = slot;

        setObject(construct(packet, slot));
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        slot = fetch(slotField);
    }

    @Override
    public Object getObject() {
        return super.getObject();
    }
}
