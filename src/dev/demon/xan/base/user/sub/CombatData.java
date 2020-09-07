package dev.demon.xan.base.user.sub;

import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.time.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

@Getter
@Setter
public class CombatData {
    private long lastDeath, lastRandomDamage, lastUseEntityPacket, lastEntityDamage, lastEntityDamageAttack, lastRespawn, lastFireDamage, lastBowDamage;
    private Entity lastEntityAttacked;
    public int lastBowDamageTick, lastAttackedTick, hitDelay, lastUseEntityTick, cps, lastBowStrength, noDamageTicks, cancelTicks, constantEntityTicks;
    private boolean breakingBlock, respawn;
    private User targetUser;
    private double lastVelocitySqr;

    private User user;

    public CombatData(User user) {
        this.user = user;
    }

    public boolean hasBowBoosted() {
        return (lastBowStrength > 0 && TimeUtils.secondsFromLong(lastBowDamage) < 3L);
    }

    public boolean didAttack(int tick) {
        return user.getConnectedTick() > tick && Math.abs(user.getConnectedTick() - this.lastUseEntityTick) < tick;
    }

    public boolean wasAttacked(int tick) {
        return user.getConnectedTick() > tick && Math.abs(user.getConnectedTick() - this.lastUseEntityTick) < tick;
    }

    public boolean isInCombo() {
        return hitDelay != 20;
    }
}
