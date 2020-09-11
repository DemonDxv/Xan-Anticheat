package dev.demon.xan.base.user.sub;

import dev.demon.xan.base.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class VelocityData {
    private User user;

    private int lastVelocityTicks, velocityTicks;
    private long lastVelocity;
    private double velocityX, velocityY, velocityZ, horizontalVelocity, verticalVelocity, horizontalVelocityTrans, verticalVelocityTrans;
    private HashMap<Double, Short> lastVelocityVertical = new HashMap(), lastVelocityHorizontal = new HashMap();

    public VelocityData(User user) {
        this.user = user;
    }
}