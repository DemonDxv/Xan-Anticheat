package dev.demon.xan.base.user.sub;

import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.location.CustomLocation;
import dev.demon.xan.utils.location.PlayerLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Queue;

@Getter
@Setter
public class MoveData {
    private User user;
    private Block serverBlockBelow;
    private Location bukkitTo, bukkitFrom;
    private CustomLocation lastGroundLocation, lastSlimeLocation = new CustomLocation(0, 0, 0), to = new CustomLocation(0, 0, 0), from = to, fromFrom = from;
    private boolean lastSprint, testGround, breakingOrPlacingBlock, didUnknownTeleport, isExplode, clientGround, lastClientGround, collidedGround, nearBoat, lastCollidedVertically, lastCollidedHorizontally, didTeleportInteract, chunkLoaded, onGround, worldLoaded, collidesHorizontally, collidesVertically, lastOnGround, sprinting, jumpPad, sneaking;
    private int lastTeleportTick, boatTicks, lastServerPostionFull, lastTelportInteractTick, commandBlockTeleportTicks, sprintTicks, lastServerPostion, clientAirTicks, clientGroundTicks, lastJumpPadUpdateTick, lastCheckBlockTick, lastBlockGroundTick, velocityTicks, mouseDeltaX, mouseDeltaY, unknownTeleportTick, afkMovementTotalBlocks, totalSlimeBlocksMoved, collidedGroundTicks, airTicks, groundTicks;
    private long lastBlockJump, lastPos, lastBlockFall, lastCollidedGround, lastFullBlockMoved, LastJunpPadUpdate, lastJumpPadSet, breakingOrPlacingTime, lastUnknownTeleport, lastTeleport, lastFullTeleport, lastExplode, lastEnderpearl, lastFallDamage, lastNearBoat;
    private double deltaXZ, lastGroundPrediction, lastFallJumpPrediction, groundYPredict, walkSpeed, movementSpeed;
    public PlayerLocation location, previousLocation, previousPreviousLocation;

    public MoveData(User user) {
        this.user = user;
    }
}