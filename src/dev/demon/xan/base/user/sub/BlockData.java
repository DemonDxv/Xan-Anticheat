package dev.demon.xan.base.user.sub;

import dev.demon.xan.base.user.User;

public class BlockData {
    public int pistionTick, enchantmentTableTicks, ironBarTicks, lastHalfBlockTick, lastSlimeTick, lastChestTick, presurePlateTicks, lastPresurePlateTick, lastStairTicks, lastSlabTick, lastBlockAboveTick, lastIceTick, enderFrameTick, bedTicks, leaveTicks, chestTicks, trapDoorTicks, hopperTicks, lillyPadTicks, solidLiquidTicks, anvilTicks, wallTicks, doorTicks, glassPaneTicks, halfBlockTicks, soulSandTicks, snowTicks, webTicks, liquidTicks, blockAboveTicks, iceTicks, stairTicks, slabTicks, fenceTicks, railTicks, slimeTicks, climbableTicks;
    public long lastAnyBlockWithLiquid, lastBlockAbove, lastSoulSand, lastSline, lastIce;
    public boolean ice, climable, slime, isGroundWater;

    public BlockData(User user) {
        lastIceTick = lastBlockAboveTick = lastSlabTick = lastPresurePlateTick = lastChestTick = lastSlimeTick = lastHalfBlockTick = user.getConnectedTick();
    }
}
