package dev.demon.xan.base.tinyprotocol.packet;

import dev.demon.xan.base.tinyprotocol.api.packets.reflections.types.WrappedField;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PacketField<T> {
    private WrappedField field;
    private T value;
}
