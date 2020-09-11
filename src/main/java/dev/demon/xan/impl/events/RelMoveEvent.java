package dev.demon.xan.impl.events;

import dev.demon.xan.api.event.AnticheatEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelMoveEvent extends AnticheatEvent {

    @Getter
    @Setter
    private double x, y, z;
    private float yaw, pitch;
    private boolean clientGround, pos, look;

    public RelMoveEvent(double x, double y, double z, float pitch, float yaw, boolean clientGround, boolean pos, boolean look) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.clientGround = clientGround;
        this.pos = pos;
        this.look = look;
    }
}
