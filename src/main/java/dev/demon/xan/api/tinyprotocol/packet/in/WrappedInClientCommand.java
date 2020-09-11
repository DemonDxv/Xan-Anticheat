package dev.demon.xan.api.tinyprotocol.packet.in;

import dev.demon.xan.api.tinyprotocol.api.NMSObject;
import dev.demon.xan.api.tinyprotocol.api.ProtocolVersion;
import dev.demon.xan.api.tinyprotocol.reflection.FieldAccessor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class WrappedInClientCommand extends NMSObject {
    private static final String packet = Client.CLIENT_COMMAND;

    // Fields
    private static FieldAccessor<Enum> fieldCommand = fetchField(packet, Enum.class, 0);

    // Decoded data
    EnumClientCommand command;

    public WrappedInClientCommand(Object packet, Player player) {
        super(packet, player);
    }

    @Override
    public void updateObject() {

    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        command = EnumClientCommand.values()[fetch(fieldCommand).ordinal()];
    }

    public enum EnumClientCommand {
        PERFORM_RESPAWN,
        REQUEST_STATS,
        OPEN_INVENTORY_ACHIEVEMENT;

        private EnumClientCommand() {
        }
    }
}