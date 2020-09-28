package dev.demon.xan.impl.events;

import dev.demon.xan.api.event.AnticheatEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbilityOutEvent extends AnticheatEvent {

    private double flySpeed, walkSpeed;
    private boolean isFlying, allowedFlight, creativeMode, invulnerable;

    public AbilityOutEvent(boolean allowedFlight, boolean isFlying, boolean creativeMode, boolean invulnerable, double flySpeed, double walkSpeed) {
        this.allowedFlight = allowedFlight;
        this.isFlying = isFlying;
        this.creativeMode = creativeMode;
        this.invulnerable = invulnerable;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }
}
