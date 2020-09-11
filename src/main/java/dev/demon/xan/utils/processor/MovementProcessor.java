package dev.demon.xan.utils.processor;

import dev.demon.xan.Xan;
import dev.demon.xan.base.tinyprotocol.api.Packet;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInBlockDigPacket;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInEntityActionPacket;
import dev.demon.xan.base.tinyprotocol.packet.in.WrappedInFlyingPacket;
import dev.demon.xan.base.tinyprotocol.packet.out.WrappedOutPositionPacket;
import dev.demon.xan.base.tinyprotocol.packet.out.WrappedOutVelocityPacket;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.box.BoundingBox;
import dev.demon.xan.utils.block.BlockAssesement;
import dev.demon.xan.utils.block.BlockEntry;
import dev.demon.xan.utils.block.BlockUtil;
import dev.demon.xan.utils.location.CustomLocation;
import dev.demon.xan.utils.location.PlayerLocation;
import dev.demon.xan.utils.math.MathUtil;
import dev.demon.xan.utils.math.evicting.EvictingList;
import dev.demon.xan.utils.time.TickTimer;
import dev.demon.xan.utils.time.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 05/01/2020 Package me.jumba.sparky.util.processor
 */
@Setter
@Getter
public class MovementProcessor {
    private User user;

    private double offset = Math.pow(2.0, 24.0);

    private double pitchDelta, yawDelta, lastDeltaYaw, lastDeltaPitch, pitchMode, yawMode, sensXPercent, deltaX, deltaY, sensYPercent, sensitivityX, sensitivityY, lastDeltaX, lastDeltaY;

    public long pitchGCD, yawGCD;

    public List<Double> pitchGcdList = new EvictingList(40), yawGcdList = new EvictingList(40);

    private TickTimer timer = new TickTimer(5);

    public void update(Object packet, String type) {
        if (user != null) {

            if (type.equalsIgnoreCase(Packet.Server.POSITION)) {

                WrappedOutPositionPacket wrappedOutSpawnEntityPacket = new WrappedOutPositionPacket(packet, user.getPlayer());

                //user.debug("" + wrappedOutSpawnEntityPacket.getY());

                if (wrappedOutSpawnEntityPacket.getY() > 0.0) {

                    if (user.getMovementData().isDidTeleportInteract()) {
                        user.getMovementData().setLastTelportInteractTick(user.getConnectedTick());
                        user.getMovementData().setDidTeleportInteract(false);
                        user.getMovementData().setCommandBlockTeleportTicks(100);
                    }

                    user.getMovementData().setLastCheckBlockTick(user.getConnectedTick());


                    user.getMovementData().setLastServerPostion(user.getConnectedTick());
                } else {
                    user.getMovementData().setLastServerPostionFull(user.getConnectedTick());
                }
            }

            if (type.equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {

                user.getMovementData().setLastCheckBlockTick(user.getConnectedTick());


                user.getMovementData().setBreakingOrPlacingBlock(true);
                user.getMovementData().setBreakingOrPlacingTime(System.currentTimeMillis());
            }

            if (type.equalsIgnoreCase(Packet.Client.BLOCK_DIG)) {

                user.getMovementData().setLastCheckBlockTick(user.getConnectedTick());

                WrappedInBlockDigPacket wrappedInBlockDigPacket = new WrappedInBlockDigPacket(packet, user.getPlayer());

                if (wrappedInBlockDigPacket.getAction() == WrappedInBlockDigPacket.EnumPlayerDigType.START_DESTROY_BLOCK) {
                    user.getMovementData().setBreakingOrPlacingBlock(true);
                    user.getMovementData().setBreakingOrPlacingTime(System.currentTimeMillis());
                } else if (wrappedInBlockDigPacket.getAction() == WrappedInBlockDigPacket.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                    user.getMovementData().setBreakingOrPlacingBlock(false);
                } else if (wrappedInBlockDigPacket.getAction() == WrappedInBlockDigPacket.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                    user.getMovementData().setBreakingOrPlacingBlock(false);
                }
            }

            if (type.equalsIgnoreCase(Packet.Server.ENTITY_VELOCITY)) {

                WrappedOutVelocityPacket velocityPacket = new WrappedOutVelocityPacket(packet, user.getPlayer());
                if (velocityPacket.getId() == user.getPlayer().getEntityId()) {

                    user.getMovementData().setLastCheckBlockTick(user.getConnectedTick());


                    if (user.getMovementData().isJumpPad() && (System.currentTimeMillis() - user.getMovementData().getLastFallDamage()) < 1000L) {
                        user.getMovementData().setJumpPad(false);
                        user.getMovementData().setLastJunpPadUpdate(0);
                        return;
                    }

                    if (!user.getMovementData().isJumpPad() && (System.currentTimeMillis() - user.getMovementData().getLastFallDamage()) > 1000L && user.getMovementData().isOnGround() && (System.currentTimeMillis() - user.getCombatData().getLastEntityDamageAttack()) > 20L) {
                        user.getMovementData().setJumpPad(true);
                        user.getMovementData().setLastJumpPadSet(System.currentTimeMillis());
                    }
                }

                if (user.getMovementData().isJumpPad() && user.getMovementData().isOnGround() && user.getMovementData().getGroundTicks() > 15 && (System.currentTimeMillis() - user.getMovementData().getLastJumpPadSet()) > 1000L) {
                    user.getMovementData().setJumpPad(false);
                }
            }

            if (type.equalsIgnoreCase(Packet.Client.ENTITY_ACTION)) {
                user.getMovementData().setLastSprint(user.getMovementData().isSprinting());
                WrappedInEntityActionPacket wrappedInEntityActionPacket = new WrappedInEntityActionPacket(packet, user.getPlayer());
                if (wrappedInEntityActionPacket.getAction() == WrappedInEntityActionPacket.EnumPlayerAction.START_SPRINTING) {
                    user.getMovementData().setSprinting(true);
                } else if (wrappedInEntityActionPacket.getAction() == WrappedInEntityActionPacket.EnumPlayerAction.STOP_SPRINTING) {
                    user.getMovementData().setSprinting(false);
                }

                if (wrappedInEntityActionPacket.getAction() == WrappedInEntityActionPacket.EnumPlayerAction.START_SNEAKING) {
                    user.getMovementData().setSneaking(true);
                } else if (wrappedInEntityActionPacket.getAction() == WrappedInEntityActionPacket.EnumPlayerAction.STOP_SPRINTING) {
                    user.getMovementData().setSneaking(false);
                }
            }

            if (type.equalsIgnoreCase(Packet.Client.POSITION) || type.equalsIgnoreCase(Packet.Client.POSITION_LOOK) || type.equalsIgnoreCase(Packet.Client.LOOK) || type.equalsIgnoreCase(Packet.Client.FLYING)) {
                WrappedInFlyingPacket wrappedInFlyingPacket = new WrappedInFlyingPacket(packet, user.getPlayer());


                if (user.getPlayer().isDead()) {
                    user.getMiscData().setDead(true);
                    user.getMiscData().setLastDeadTick(user.getConnectedTick());
                } else {
                    user.getMiscData().setDead(false);
                }


                if (user.getMovementData().isDidUnknownTeleport()) {

                    if (Math.abs(user.getConnectedTick() - user.getMovementData().getUnknownTeleportTick()) > 20) {

                        if ((user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY() >= 0.0)) {


                            if (user.getMovementData().getMovementSpeed() > 0.66 && (System.currentTimeMillis() - user.getCombatData().getLastEntityDamage()) > 1000L && (System.currentTimeMillis() - user.getCombatData().getLastBowDamage()) > 1000L) {
                                user.getMovementData().setDidUnknownTeleport(false);
                            }
                        }

                        if (Math.abs(user.getConnectedTick() - user.getMovementData().getUnknownTeleportTick()) > 5 && (user.getMovementData().isOnGround() || user.getMovementData().isClientGround())) {
                            user.getMovementData().setDidUnknownTeleport(false);
                        }
                    }
                }

                if ((System.currentTimeMillis() - user.getMovementData().getLastExplode()) > 1000L && user.getMovementData().isExplode() && user.getMovementData().isOnGround() && user.getMovementData().isLastOnGround()) {
                    user.getMovementData().setExplode(false);
                }

                if (user.isWaitingForMovementVerify()) {

                    if (user.movementVerifyBlocks > 5) {
                        user.movementVerifyBlocks = 0;
                        user.setWaitingForMovementVerify(false);
                    }

                    double x = Math.floor(user.getMovementData().getFrom().getX());
                    double z = Math.floor(user.getMovementData().getFrom().getZ());
                    if (Math.floor(user.getMovementData().getTo().getX()) != x || Math.floor(user.getMovementData().getTo().getZ()) != z) {
                        user.movementVerifyBlocks++;
                    }
                }


                if (user.getFlyingTick() < 20) {
                    user.setFlyingTick(user.getFlyingTick() + 1);
                } else if (user.getFlyingTick() >= 20) {
                    user.setFlyingTick(0);
                }

                user.setConnectedTick(user.getConnectedTick() + 1);
                user.getVelocityData().setLastVelocityTicks(user.getVelocityData().getVelocityTicks());
                user.getVelocityData().setVelocityTicks(user.getVelocityData().getVelocityTicks() + 1);

                if (user.getMovementData().isJumpPad()) {
                    if ((System.currentTimeMillis() - user.getMovementData().getLastJumpPadSet()) > 230L && user.getMovementData().isOnGround() && user.getMovementData().isLastOnGround()) {
                        user.getMovementData().setJumpPad(false);
                    }
                    user.getMovementData().setLastJunpPadUpdate(System.currentTimeMillis());
                }

                user.setSafe(TimeUtils.secondsFromLong(user.getTimestamp()) > 2L || user.isHasVerify());

                if (!user.isHasVerify()) user.setHasVerify(user.isSafe());


                if (user.isSafe()) {

                    if (!user.getMiscData().isAfkMovement() && ((System.currentTimeMillis() - user.getMovementData().getLastFullBlockMoved()) > 700L || (System.currentTimeMillis() - user.getCombatData().getLastEntityDamage()) < 1000L || (System.currentTimeMillis() - user.getCombatData().getLastBowDamage() < 1000L || (System.currentTimeMillis() - user.getCombatData().getLastRandomDamage()) < 1000L)) && (user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY()) < -0.55) {
                        user.getMiscData().setAfkMovement(true);
                    } else if (user.getMiscData().isAfkMovement()) {
                        if (user.getMovementData().getAfkMovementTotalBlocks() > 3) {
                            user.getMovementData().setAfkMovementTotalBlocks(0);
                            user.getMiscData().setAfkMovement(false);
                        }
                    }

                    user.getMovementData().setLastClientGround(user.getMovementData().isClientGround());
                    user.getMovementData().setClientGround(wrappedInFlyingPacket.isGround());


                    if (user.getMovementData().getFrom() != null) {
                        user.getMovementData().setFromFrom(user.getMovementData().getFrom().clone());
                    }
                    if (user.getMovementData().getTo() != null) {
                        user.getMovementData().setFrom(user.getMovementData().getTo().clone());
                    }


                    PlayerLocation lastLocation = user.getMovementData().getLocation();
                    PlayerLocation lastLocation2 = user.getMovementData().getPreviousLocation();

                    if (user.isSafe() && user.getBoundingBox() != null) {

                        this.updateBlockCheck();
                    } else {
                        user.getMovementData().setOnGround(wrappedInFlyingPacket.isGround());
                    }

                    if (wrappedInFlyingPacket.isPos()) {

                        user.getMovementData().getTo().setX(wrappedInFlyingPacket.getX());
                        user.getMovementData().getTo().setY(wrappedInFlyingPacket.getY());
                        user.getMovementData().getTo().setZ(wrappedInFlyingPacket.getZ());
                        user.getMovementData().getTo().setClientGround(wrappedInFlyingPacket.isGround());
                        user.getMovementData().setLastPos(System.currentTimeMillis());

                        user.getMovementData().setLastOnGround(user.getMovementData().isOnGround());

                        //    user.getMovementData().locationDataQueue.add(new DevLocation(wrappedInFlyingPacket.getX(), wrappedInFlyingPacket.getY(), wrappedInFlyingPacket.getZ(), wrappedInFlyingPacket.getYaw(), wrappedInFlyingPacket.getPitch()));
                        //  user.getMovementData().setLocation(new DevLocation(wrappedInFlyingPacket.getX(), wrappedInFlyingPacket.getY(), wrappedInFlyingPacket.getZ(),
                        //          wrappedInFlyingPacket.isLook() ? wrappedInFlyingPacket.getYaw() : lastLocation.getYaw(),
                        //          wrappedInFlyingPacket.isLook() ? wrappedInFlyingPacket.getPitch() : lastLocation.getPitch()));


                        CustomLocation customLocation = new CustomLocation(wrappedInFlyingPacket.getX(), wrappedInFlyingPacket.getY(), wrappedInFlyingPacket.getZ());

                        if (user.getMovementData().getLastGroundLocation() != null && user.getMovementData().isOnGround()) {

                            if (Math.abs((user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY())) > 0.0f && user.getMovementData().getTo().getY() < user.getMovementData().getFrom().getY()) {
                                double totalPrediction = MathUtil.round(user.getMovementData().getTo().getY(), 0) + user.getMovementData().getGroundYPredict();

                                if (totalPrediction < user.getMovementData().getLastFallJumpPrediction()) {
                                    user.getMovementData().setLastBlockFall(System.currentTimeMillis());
                                }

                                user.getMovementData().setLastFallJumpPrediction(totalPrediction);
                            }

                            if ((user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY()) > 0.4f && user.getMovementData().getTo().getY() > user.getMovementData().getFrom().getY()) {
                                double totalPrediction = MathUtil.round(user.getMovementData().getTo().getY(), 0) + user.getMovementData().getGroundYPredict();

                                if (totalPrediction > user.getMovementData().getLastGroundPrediction()) {
                                    user.getMovementData().setLastBlockJump(System.currentTimeMillis());
                                }

                                user.getMovementData().setLastGroundPrediction(totalPrediction);
                            }
                        }

                        if (user.getMovementData().isOnGround() && user.getMovementData().getTo() != null && user.getMovementData().getFrom() != null) {
                            if (user.getMovementData().isLastOnGround()) {
                                user.getMovementData().setGroundYPredict(user.getMovementData().getTo().getY());
                            }
                            user.getMovementData().setLastGroundLocation(customLocation);
                        }


                    }

                    user.getMovementData().setDeltaXZ((Math.hypot(user.getMovementData().getTo().getX() - user.getMovementData().getFrom().getX(), user.getMovementData().getTo().getZ() - user.getMovementData().getFrom().getZ())));

                    boolean badVector = Math.abs(user.getMovementData().getTo().toVector().length() - user.getMovementData().getFrom().toVector().length()) >= 1;

                    user.setBoundingBox(new BoundingBox((badVector ? user.getMovementData().getTo().toVector() : user.getMovementData().getFrom().toVector()), user.getMovementData().getTo().toVector()).grow(0.3f, 0, 0.3f).add(0, 0, 0, 0, 1.84f, 0));


                    if (wrappedInFlyingPacket.isLook()) {
                        if (lastLocation != null) {
                            //  lastLocation.setYaw(wrappedInFlyingPacket.getYaw());
                            //  lastLocation.setPitch(wrappedInFlyingPacket.getPitch());
                            //  user.getMovementData().setPreviousLocation(lastLocation);
                        }
                        if (lastLocation2 != null) {
                            //   user.getMovementData().setPreviousPreviousLocation(lastLocation2);
                        }


                        user.getMovementData().getTo().setPitch(wrappedInFlyingPacket.getPitch());
                        user.getMovementData().getTo().setYaw(wrappedInFlyingPacket.getYaw());
                    }

                    if (user.getMovementData().getTo() != null && user.getMovementData().getFrom() != null) {

                        if (Math.abs(user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY()) > 0.0f) {
                            user.getMovementData().setLastCheckBlockTick(user.getConnectedTick());
                        }

                        double x = Math.floor(user.getMovementData().getFrom().getX());
                        double z = Math.floor(user.getMovementData().getFrom().getZ());

                        if (Math.floor(user.getMovementData().getTo().getX()) != x || Math.floor(user.getMovementData().getTo().getZ()) != z) {

                            user.getMovementData().setLastCheckBlockTick(user.getConnectedTick());

                            if (user.totalBlocksCheck < 100) user.totalBlocksCheck++;

                            user.getMovementData().setLastFullBlockMoved(System.currentTimeMillis());

                            if (user.getCombatData().isRespawn() && (System.currentTimeMillis() - user.getCombatData().getLastRespawn()) > 1000L) {
                                user.getCombatData().setRespawn(false);
                            }

                            if (user.getMiscData().isAfkMovement()) {
                                user.getMovementData().setAfkMovementTotalBlocks(user.getMovementData().getAfkMovementTotalBlocks() + 1);
                            } else {
                                user.getMovementData().setAfkMovementTotalBlocks(0);
                            }
                        }
                    }

                    if (user.getMiscData().isSwitchedGamemodes() && user.getMovementData().isOnGround()) {
                        user.getMiscData().setSwitchedGamemodes(false);
                    }

                    if (user.getMovementData().getTo() != null && user.getMovementData().getFrom() != null) {
                        user.getMovementData().setBukkitTo(user.getMovementData().getTo().toLocation(user.getPlayer().getWorld()));
                        user.getMovementData().setBukkitFrom(user.getMovementData().getFrom().toLocation(user.getPlayer().getWorld()));

                        double x = Math.abs(Math.abs(user.getMovementData().getTo().getX()) - Math.abs(user.getMovementData().getFrom().getX()));
                        double z = Math.abs(Math.abs(user.getMovementData().getTo().getZ()) - Math.abs(user.getMovementData().getFrom().getZ()));
                        user.getMovementData().setMovementSpeed(Math.sqrt(x * x + z * z));
                    }
                }
            }
        }
    }
    private void updateBlockCheck() {

        boolean work = true;

        if (work) {

            //user.debug(""+user.getPlayer().getMaximumNoDamageTicks());

            BlockAssesement blockAssesement = new BlockAssesement(user.getBoundingBox(), user);

            List<BoundingBox> boxes = Xan.getInstance().getBlockBoxManager().getBlockBox().getCollidingBoxes(user.getPlayer().getWorld(), user.getBoundingBox().grow(0.3f, 0.35f, 0.3f));

            List<BlockEntry> blockEntries = Collections.synchronizedList(new ArrayList<>());

            user.getMovementData().setChunkLoaded(BlockUtil.isChunkLoaded(user.getMovementData().getTo().toLocation(user.getPlayer().getWorld())));

            if (user.getMovementData().isChunkLoaded()) {
                boxes.parallelStream().forEach(boundingBox -> {
                    Block block = BlockUtil.getBlock(boundingBox.getMinimum().toLocation(user.getPlayer().getWorld()));

                    if (block != null) {

                        BlockEntry blockEntry = new BlockEntry(block, boundingBox);

                        blockAssesement.update(blockEntry.getBoundingBox(), blockEntry.getBlock(), user.getPlayer().getWorld());

                        blockEntries.add(blockEntry);
                    }
                });
            }

            blockAssesement.updateBlocks(blockEntries);

            blockEntries.clear();

            boxes.clear();

            user.getMovementData().setOnGround(blockAssesement.isOnGround());

            user.getMovementData().setTestGround(blockAssesement.isTestGround());

            user.getMovementData().setCollidedGround(blockAssesement.isCollidedGround());

            if (blockAssesement.isCollidedGround()) {
                user.getMovementData().setLastCollidedGround(System.currentTimeMillis());
            }

            user.getBlockData().isGroundWater = blockAssesement.isLiquidGround();
            user.update(blockAssesement);
        }
    }
}