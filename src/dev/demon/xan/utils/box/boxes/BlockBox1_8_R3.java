package dev.demon.xan.utils.box.boxes;

import dev.demon.xan.utils.block.BlockUtil;
import dev.demon.xan.utils.box.BlockBox;
import dev.demon.xan.utils.box.BoundingBox;
import dev.demon.xan.utils.math.MathUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockBox1_8_R3 implements BlockBox {

   /* @Override
    public List<BoundingBox> getCollidingBoxes(World world, BoundingBox box) {

        Sparky.getInstance().getBaseProfiler().start("blockbox-1.8");

        int minX = MathUtil.floor(box.minX);
        int maxX = MathUtil.floor(box.maxX + 1);
        int minY = MathUtil.floor(box.minY);
        int maxY = MathUtil.floor(box.maxY + 1);
        int minZ = MathUtil.floor(box.minZ);
        int maxZ = MathUtil.floor(box.maxZ + 1);

        List<CustomLocation> locs = new ArrayList<>();

        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = minY - 1; y < maxY; y++) {
                    locs.add(new CustomLocation(world, x, y, z));
                }
            }
        }


        List<BoundingBox> boxes = Collections.synchronizedList(new ArrayList<>());

        boolean chunkLoaded = isChunkLoaded(box.getMinimum().toLocation(world));

      //  if(chunkLoaded) {
        Location locc = box.getMinimum().toLocation(world);

        if (locc.getWorld().isChunkLoaded(locc.getBlockX() >> 4, locc.getBlockZ() >> 4)) {

            locs.parallelStream().forEach(loc -> {

                Location location = loc.toLocation(world);

                org.bukkit.block.Block block = location.getBlock();

                if (block != null) {

                    if (!block.getType().equals(Material.AIR)) {

                        if (BlockUtil.collisionBoundingBoxes.containsKey(block.getType())) {
                            BoundingBox box2 = BlockUtil.collisionBoundingBoxes.get(block.getType()).add(block.getLocation().toVector());
                            boxes.add(box2);

                        } else {
                            int x = block.getX(), y = block.getY(), z = block.getZ();

                            BlockPosition pos = new BlockPosition(x, y, z);


                            net.minecraft.server.v1_8_R3.World nmsWorld = ((CraftWorld) world).getHandle();

                            IBlockData nmsiBlockData = ((CraftWorld) world).getHandle().getType(pos);

                            Block nmsBlock = nmsiBlockData.getBlock();

                            List<AxisAlignedBB> preBoxes = new ArrayList<>();

                            nmsBlock.updateShape(nmsWorld, pos);
                            nmsBlock.a(nmsWorld, pos, nmsiBlockData, (AxisAlignedBB) box.toAxisAlignedBB(), preBoxes, null);

                            if (preBoxes.size() > 0) {
                                for (AxisAlignedBB aabb : preBoxes) {
                                    BoundingBox bb = new BoundingBox(
                                            (float) aabb.a,
                                            (float) aabb.b,
                                            (float) aabb.c,
                                            (float) aabb.d,
                                            (float) aabb.e,
                                            (float) aabb.f);

                                    if (bb.collides(box)) {
                                        boxes.add(bb);
                                    }
                                }

                            } else {
                                BoundingBox bb = new BoundingBox(
                                        (float) nmsBlock.B(),
                                        (float) nmsBlock.D(),
                                        (float) nmsBlock.F(),
                                        (float) nmsBlock.C(),
                                        (float) nmsBlock.E(),
                                        (float) nmsBlock.G())
                                        .add(x, y, z, x, y, z);

                                if (bb.collides(box)) {
                                    boxes.add(bb);
                                }
                            }
                        }
                    }
                }
            });
            //   }
        }

        Sparky.getInstance().getBaseProfiler().stop("blockbox-1.8");
        return boxes;
    }*/

    @Override
    public List<BoundingBox> getCollidingBoxes(org.bukkit.World world, BoundingBox box) {

        int minX = MathUtil.floor(box.minX);
        int maxX = MathUtil.floor(box.maxX + 1);
        int minY = MathUtil.floor(box.minY);
        int maxY = MathUtil.floor(box.maxY + 1);
        int minZ = MathUtil.floor(box.minZ);
        int maxZ = MathUtil.floor(box.maxZ + 1);

        List<Location> locs = new ArrayList<>();

        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = minY - 1; y < maxY; y++) {
                    Location loc = new Location(world, x, y, z);
                    locs.add(loc);
                }
            }
        }

        List<BoundingBox> boxes = Collections.synchronizedList(new ArrayList<>());


        if(BlockUtil.isChunkLoaded(box.getMinimum().toLocation(world))) {

            locs.parallelStream().forEach(loc -> {
                org.bukkit.block.Block block = BlockUtil.getBlock(loc);
                if (block != null && !block.getType().equals(Material.AIR)) {
                    if(BlockUtil.collisionBoundingBoxes.containsKey(block.getType())) {
                        BoundingBox box2 = BlockUtil.collisionBoundingBoxes.get(block.getType()).add(block.getLocation().toVector());
                        boxes.add(box2);
                    } else {
                        int x = block.getX(), y = block.getY(), z = block.getZ();

                        BlockPosition pos = new BlockPosition(x, y, z);

                        net.minecraft.server.v1_8_R3.World nmsWorld = ((CraftWorld) world).getHandle();

                        IBlockData nmsiBlockData = ((CraftWorld) world).getHandle().getType(pos);

                        Block nmsBlock = nmsiBlockData.getBlock();

                        List<AxisAlignedBB> preBoxes = new ArrayList<>();

                        nmsBlock.updateShape(nmsWorld, pos);
                        nmsBlock.a(nmsWorld,
                                pos,
                                nmsiBlockData,
                                (AxisAlignedBB) box.toAxisAlignedBB(),
                                preBoxes,
                                null);

                        if (preBoxes.size() > 0) {
                            for (AxisAlignedBB aabb : preBoxes) {
                                BoundingBox bb = new BoundingBox(
                                        (float)aabb.a,
                                        (float)aabb.b,
                                        (float)aabb.c,
                                        (float)aabb.d,
                                        (float)aabb.e,
                                        (float)aabb.f);

                                if(bb.collides(box)) {
                                    boxes.add(bb);
                                }
                            }

                        } else {
                            BoundingBox bb = new BoundingBox(
                                    (float)nmsBlock.B(),
                                    (float)nmsBlock.D(),
                                    (float)nmsBlock.F(),
                                    (float)nmsBlock.C(),
                                    (float)nmsBlock.E(),
                                    (float)nmsBlock.G())
                                    .add(x, y, z, x, y, z);

                            if(bb.collides(box)) {
                                boxes.add(bb);
                            }
                        }
                    }
                }
            });
        }

        return boxes;
    }


    @Override
    public List<BoundingBox> getSpecificBox(Location loc) {
        return getCollidingBoxes(loc.getWorld(), new BoundingBox(loc.toVector(), loc.toVector()));
    }

    @Override
    public boolean isChunkLoaded(Location loc) {

        net.minecraft.server.v1_8_R3.World world = ((CraftWorld) loc.getWorld()).getHandle();

        return !world.isClientSide && world.isLoaded(new BlockPosition(loc.getBlockX(), 0, loc.getBlockZ())) && world.getChunkAtWorldCoords(new BlockPosition(loc.getBlockX(), 0, loc.getBlockZ())).o();
    }

    @Override
    public boolean isUsingItem(Player player) {
        EntityHuman entity = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity) player).getHandle();
        return entity.bS() && entity.bZ() != null && entity.bZ().getItem().e(entity.bZ()) != EnumAnimation.NONE;
    }

    @Override
    public boolean isRiptiding(LivingEntity entity) {
        return false;
    }

    @Override
    public float getMovementFactor(Player player) {
        return (float) ((CraftPlayer) player).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
    }

    @Override
    public int getTrackerId(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        EntityTrackerEntry entry = ((WorldServer) entityPlayer.getWorld()).tracker.trackedEntities.get(entityPlayer.getId());
        return entry.tracker.getId();
    }

    @Override
    public float getAiSpeed(Player player) {
        return ((CraftPlayer) player).getHandle().bI();
    }
}