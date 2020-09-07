package dev.demon.xan.base.tinyprotocol.packet;

import dev.demon.xan.base.tinyprotocol.api.GeneralField;
import dev.demon.xan.base.tinyprotocol.api.packets.reflections.types.WrappedClass;
import dev.demon.xan.base.tinyprotocol.api.packets.reflections.types.WrappedMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* This class can be used for packets that do not have dedicated wrappers or change constantly between versions. */
@Getter
@RequiredArgsConstructor
public class GeneralWrapper {
    private List<GeneralField> fields = new ArrayList<>();
    private List<WrappedMethod> methods;
    private final WrappedClass objectClass;

    public GeneralWrapper(Object object) {
        objectClass = new WrappedClass(object.getClass());
        methods = objectClass.getMethods();

        objectClass.getFields().forEach(field -> fields.add(new GeneralField(field, field.get(object))));
    }

    public <T> T build(Object... args) {
        val classes = Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);

        return objectClass.getConstructor(classes).newInstance(args);
    }

}
