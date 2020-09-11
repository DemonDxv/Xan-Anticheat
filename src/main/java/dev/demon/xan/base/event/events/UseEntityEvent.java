package dev.demon.xan.base.event.events;

import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

@Getter
@Setter
public class UseEntityEvent extends AnticheatEvent {


    private Entity entity;
    private WrappedInUseEntityPacket.EnumEntityUseAction action;
    public UseEntityEvent(Entity entity, WrappedInUseEntityPacket.EnumEntityUseAction action) {

        this.entity = entity;
        this.action = action;
    }
}
