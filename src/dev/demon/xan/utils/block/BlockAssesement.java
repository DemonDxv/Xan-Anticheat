package dev.demon.xan.utils.block;

import dev.demon.xan.Xan;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.box.BoundingBox;
import dev.demon.xan.utils.version.VersionUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.*;

import java.util.List;
import java.util.Objects;

/**
 * Created on 21/10/2019 Package me.jumba.util.block
 */
@Getter
@Setter
public class BlockAssesement {

    private User user;
    private BoundingBox boundingBox;
    private boolean enchantmentTable, testGround, ironBar, collidesHorizontallyStrick, presurePlate, enderFrame, bed, leaves, chest, trapDoor, hopper, lillyPad, anvil, collidedGround, door, halfGlass, liquidGround, onGround, collidesVertically, collidesHorizontally, soulSand, snow, onIce, onNearIce, blockAbove, stair, slab, pistion, climbale, groundSlime, web, chests, halfblock, liquid, wall, carpet, stairSlabs, slime, fence, rail;

    private int lastNoneNullblock = 0;

    public BlockAssesement(BoundingBox box, User user) {
        this.user = user;
        this.boundingBox = box;
    }


    public void update(BoundingBox bb, Block block, World world) {


        if (user.getMovementData().isChunkLoaded()) {



            if (block != null) {
                user.getMovementData().setLastBlockGroundTick(user.getConnectedTick());
            }

            if ((bb.collidesVertically(boundingBox.subtract(0, 0, 0, 0 , 0, 0)))) {
                testGround = true;
            }

            if ((bb.collidesVertically(boundingBox.subtract(0, 0.1f, 0, 0, 0, 0)) || bb.collidesVertically(boundingBox.subtract(0, 0.2f, 0, 0, 0, 0)) || bb.collidesVertically(boundingBox.subtract(0, 0.3f, 0, 0, 0, 0)) || bb.collidesVertically(boundingBox.subtract(0, 0.4f, 0, 0, 0, 0)) || bb.collidesVertically(boundingBox.subtract(0, 0.5f, 0, 0, 0, 0))) || (bb.collidesVertically(boundingBox.subtract(0, 0.12f, 0, 0, 1f, 0)))) {

                if (block != null && (block.getType() == Material.WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA || block.getType() == Material.STATIONARY_WATER)) {
                    liquidGround = true;
                }

                if (((Math.abs(user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY()) > 0.0) && collidesHorizontally && collidesVertically)) {
                    onGround = false;
                    collidedGround = true;
                } else {
                    onGround = true;
                }
            }

            if (bb.collidesHorizontally(boundingBox)) {
                collidesHorizontally = true;

                if (block != null && (block.getType() == Material.LONG_GRASS || block.getType() == Material.DOUBLE_PLANT)) {
                    collidesHorizontallyStrick = true;
                }
            }

            if (bb.collidesVertically(boundingBox)) {
                collidesVertically = true;
            }

            if (block != null) {

                user.totalBlockUpdates++;

            }
        }
    }

    public void updateBlocks(List<BlockEntry> data) {

        data.forEach(blockEntry -> {


            Block block = blockEntry.getBlock();

            BoundingBox bb = blockEntry.getBoundingBox();

            if ((block.getType() == Material.ICE || block.getType() == Material.PACKED_ICE) && boundingBox.subtract(-0.5F, 0.6f, -0.5F, 0, 0, 0).collidesVertically(bb)) {
                onIce = true;
            }

            if ((block.getType() == Material.ICE || block.getType() == Material.PACKED_ICE) && boundingBox.subtract(0, 1f, 0, 0, 0, 0).collidesVertically(bb)) {
                onNearIce = true;
            }

            if ((bb.getMaximum().getY()) >= boundingBox.getMaximum().getY() && bb.collidesVertically(boundingBox.add(0, 0, 0, 0, 0.35f, 0))) {
                blockAbove = true;
            }

            if (block.getType() == Material.WEB && boundingBox.collidesVertically(bb)) {
                web = true;
            }

            if (block.getType().getData() == PressurePlate.class) {
                presurePlate = true;
            }

            if (block.getType() == Material.ENDER_PORTAL_FRAME) {
                enderFrame = true;
            }

            if (block.getType() == Material.BED_BLOCK || block.getType() == Material.BED) {
                bed = true;
            }

            if (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES) {
                leaves = true;
            }

            if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.ENDER_CHEST) {
                chest = true;
            }

            if (block.getType().getData() == TrapDoor.class || block.getType().getData() == Gate.class) {
                trapDoor = true;
            }

            if (block.getType() == Material.HOPPER) {
                hopper = true;
            }

            if (block.getType() == Material.WATER_LILY) {
                lillyPad = true;
            }


            if (block.getType() == Material.WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA || block.getType() == Material.STATIONARY_WATER) {
                liquid = true;
                if (user.getBlockData().solidLiquidTicks < 50) user.getBlockData().solidLiquidTicks++;
            } else {
                user.getBlockData().solidLiquidTicks = 0;
                user.getBlockData().lastAnyBlockWithLiquid = System.currentTimeMillis();
            }

            if (block.getType() == Material.ANVIL) {
                anvil = true;
            }

            if (block.getType() == Material.SNOW) {
                snow = true;
            }

            if (block.getType().getData() == Door.class) {
                door = true;
            }

            if (BlockUtil.isGlassPane(block)) {
                halfGlass = true;
            }

            if (block.getType().getData() == Stairs.class) {
                stair = true;
            }

            if (BlockUtil.isFence(block)) {
                fence = true;
            }

            if (block.getType().getData() == Step.class || block.getType().getData() == WoodenStep.class) {
                slab = true;
            }

            if (block.getType() == Material.RAILS || block.getType() == Material.DETECTOR_RAIL || block.getType() == Material.ACTIVATOR_RAIL || block.getType() == Material.POWERED_RAIL) {
                rail = true;
            }

            if (block.getType() == Material.LADDER || Objects.requireNonNull(BlockUtil.getBlock(user.getPlayer().getLocation())).getType() == Material.LADDER || block.getType() == Material.VINE) {
                climbale = true;
            }

            if (block.getType().getData() == Chest.class) {
                chests = true;
            }
            if (BlockUtil.isHalfBlock(block)) {
                halfblock = true;
            }

            if (block.getType() == Material.COBBLE_WALL || block.getType().getId() == 85 || block.getType().getId() == 139 || block.getType().getId() == 113 || block.getTypeId() == 188 || block.getTypeId() == 189 || block.getTypeId() == 190 || block.getTypeId() == 191 || block.getTypeId() == 192) {
                wall = true;
            }

            if (block.getType() == Material.CARPET) carpet = true;

            if (block.getType().getData() == Stairs.class || block.getType().getData() == Step.class)
                stairSlabs = true;

            if (Xan.getInstance().getVersionUtil().getVersion() == VersionUtil.Version.V1_8 && block.getType() == Material.SLIME_BLOCK) {
                slime = true;
            }

            if (block.getType() == Material.SOUL_SAND) {
                soulSand = true;
            }

            if (block.getType() == Material.IRON_BARDING) {
                ironBar = true;
            }

            if (block.getType() == Material.ENCHANTMENT_TABLE) {
                enchantmentTable = true;
            }

            if (block.getType() == Material.PISTON_STICKY_BASE
                    || block.getType() == Material.PISTON_BASE
                    || block.getType() == Material.PISTON_EXTENSION
                    || block.getType() == Material.PISTON_MOVING_PIECE) {
                pistion = true;
            }
        });
    }
}