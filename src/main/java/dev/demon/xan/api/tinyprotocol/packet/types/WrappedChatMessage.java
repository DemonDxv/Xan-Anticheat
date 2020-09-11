package dev.demon.xan.api.tinyprotocol.packet.types;

import dev.demon.xan.api.tinyprotocol.api.NMSObject;
import dev.demon.xan.api.tinyprotocol.reflection.FieldAccessor;
import dev.demon.xan.utils.box.ReflectionUtil;
import lombok.Getter;

@Getter
public class WrappedChatMessage extends NMSObject {
    private static String type = Type.CHATMESSAGE;

    private String chatMessage;
    private Object[] objects;

    private static FieldAccessor<String> messageField = fetchField(type, String.class, 0);
    private static FieldAccessor<Object[]> objectsField = fetchField(type, Object[].class, 0);

    public WrappedChatMessage(String chatMessage, Object... object) {
        this.chatMessage = chatMessage;
        this.objects = object;
    }

    public WrappedChatMessage(String chatMessage) {
        this(chatMessage, new Object[]{});
    }

    public void setPacket(String packet, Object... args) {
        Class<?> chatMsgClass = ReflectionUtil.getClass(type);

        Object o = ReflectionUtil.newInstance(chatMsgClass, packet, args);

        setObject(o);
    }

    public WrappedChatMessage(Object object) {
        super(object);

        chatMessage = fetch(messageField);
        objects = fetch(objectsField);
    }

    @Override
    public void updateObject() {

    }
}
