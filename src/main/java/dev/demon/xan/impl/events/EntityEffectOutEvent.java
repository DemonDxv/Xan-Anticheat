package dev.demon.xan.impl.events;

import dev.demon.xan.api.event.AnticheatEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityEffectOutEvent extends AnticheatEvent {
    private byte effectID;

    public EntityEffectOutEvent(byte effectID) {
        this.effectID = effectID;
    }
}