package dev.demon.xan.api.tinyprotocol.api;

import dev.demon.xan.api.tinyprotocol.api.packets.reflections.types.WrappedField;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GeneralField {
    public final WrappedField field;
    private final Object object;

    public <T> T getObject() {
        return (T) object;
    }
}