package dev.demon.xan.impl.events;

import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.tinyprotocol.packet.in.WrappedInEntityActionPacket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerActionEvent extends AnticheatEvent {

    WrappedInEntityActionPacket.EnumPlayerAction action;

    public PlayerActionEvent(WrappedInEntityActionPacket.EnumPlayerAction action) {
        this.action = action;
    }
}
