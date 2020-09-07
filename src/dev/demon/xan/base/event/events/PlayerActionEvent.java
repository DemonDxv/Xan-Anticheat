package dev.demon.xan.base.event.events;

import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInEntityActionPacket;
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
