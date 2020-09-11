package dev.demon.xan.impl.events;

import dev.demon.xan.api.event.AnticheatEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VelocityEvent extends AnticheatEvent {

    private int id;
    private double x, y, z;

    public VelocityEvent(int id, double x, double y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
