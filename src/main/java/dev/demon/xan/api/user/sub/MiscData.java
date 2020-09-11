package dev.demon.xan.api.user.sub;

import dev.demon.xan.api.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class MiscData {

    private int lastDeadTick, blockingSpeedTicks, lastBlockPlaceTick, speedPotionTicks, mountedTicks, boatTicks, jumpPotionTicks;
    private boolean debugMode, hasLagged, usingChunkBuster, dead, afkMovement, hasSetClientSensitivity, inventoryOpen, hasJumpPotion, hasSpeedPotion, switchedGamemodes, isNearBoat;
    private float speedPotionEffectLevel, jumpPotionMultiplyer;
    private long lastEjectVechielEject, lastBlockBreakCancel, lastBlockCancel, lastNearBoat, lastMount, lastGamemodeSwitch, lastMoutUpdate, lastBlockPlace;
    private double clientSensitivity, clientSensitivity2;
    private short transactionID = randomTransactionID(), transactionIDVelocity = randomTransactionIDVelocity(), transactionFastID = randomTransactionID();

    private User user;

    public MiscData(User user) {
        this.user = user;
    }

    public short randomTransactionID() {
        return (short) ThreadLocalRandom.current().nextInt(999999999);
    }
    public short randomTransactionIDVelocity() {
        return (short) ThreadLocalRandom.current().nextInt(999999999);
    }
}
