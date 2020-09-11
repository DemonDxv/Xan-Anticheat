package dev.demon.xan.impl.listeners;

import dev.demon.xan.Xan;
import dev.demon.xan.api.user.User;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;

/**
 * Created on 06/01/2020 Package me.jumba.sparky.listener
 */
public class BukkitListeners implements Listener {


    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        event.getPlayer().setWalkSpeed(0.2f);
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            if (e.getNewGameMode() != GameMode.CREATIVE) {
                user.setMovementVerifyStage(0);
                user.setWaitingForMovementVerify(true);
            } else if (e.getNewGameMode() == GameMode.CREATIVE) {
                user.setMovementVerifyStage(0);
                user.setWaitingForMovementVerify(false);
            }
            user.getMiscData().setLastGamemodeSwitch(System.currentTimeMillis());
            user.getMiscData().setSwitchedGamemodes(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFlightToggle(PlayerToggleFlightEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            user.getMiscData().setSwitchedGamemodes(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            user.getCombatData().setRespawn(true);
            user.getCombatData().setLastRespawn(System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {


            if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN && Math.abs(user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY()) == 0.0 && user.getMovementData().isOnGround() && user.getMovementData().isClientGround()) {
                user.getMovementData().setDidUnknownTeleport(true);
                user.getMovementData().setUnknownTeleportTick(user.getConnectedTick());
            }

            if (e.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
                user.setWaitingForMovementVerify(true);
            }

            if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN && Math.abs(user.getMovementData().getTo().getY() - user.getMovementData().getFrom().getY()) == 0.0) {
                user.getMovementData().setLastUnknownTeleport(System.currentTimeMillis());
            }
            if (e.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
                user.getMovementData().setLastTeleport(System.currentTimeMillis());
                user.getMovementData().setLastTeleportTick(user.getConnectedTick());
            }
            user.getMovementData().setLastFullTeleport(System.currentTimeMillis());
        }
    }



    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            user.getMiscData().setLastBlockBreakCancel(System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            if (e.isCancelled()) {
                user.getMiscData().setLastBlockCancel(System.currentTimeMillis());
            }
            user.getMiscData().setLastBlockPlace(System.currentTimeMillis());
            user.getMiscData().setLastBlockPlaceTick(user.getConnectedTick());
        }
    }


    @EventHandler
    public void onProjectile(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {

            Player shooter = (Player) e.getEntity().getShooter();

            User user = Xan.getInstance().getUserManager().getUser(shooter.getUniqueId());
            if (user != null) {

             /*   if (e.getEntity() instanceof Arrow) {
                    Arrow arrow = (Arrow) e.getEntity();
                    user.getCombatData().setLastBowStrength(arrow.getKnockbackStrength());
                }*/

                if (e.getEntity() instanceof EnderPearl) {
                    user.getMovementData().setLastEnderpearl(System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            User user = Xan.getInstance().getUserManager().getUser(e.getEntity().getUniqueId());
            if (user != null) {
                if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
                    user.getCombatData().setLastRandomDamage(System.currentTimeMillis());
                }

                switch (e.getCause()) {

                    case ENTITY_ATTACK:
                        user.getCombatData().setLastAttackedTick(user.getConnectedTick());
                        user.getCombatData().setLastEntityDamage(System.currentTimeMillis());
                        break;

                    case FALL:
                        user.getMovementData().setLastFallDamage(System.currentTimeMillis());
                        break;

                    case FIRE:
                    case FIRE_TICK:
                        user.getCombatData().setLastFireDamage(System.currentTimeMillis());
                        break;

                    case PROJECTILE:
                        user.getCombatData().setLastBowDamage(System.currentTimeMillis());
                        user.getCombatData().setLastBowDamageTick(user.getConnectedTick());
                        break;

                    case ENTITY_EXPLOSION:
                        user.getMovementData().setLastExplode(System.currentTimeMillis());
                        if (!user.getMovementData().isExplode()) {
                            user.getMovementData().setExplode(true);
                        }
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onHostilieAttack(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            User user = Xan.getInstance().getUserManager().getUser(e.getEntity().getUniqueId());
            if (user != null) {

                if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
                    user.getCombatData().setLastRandomDamage(System.currentTimeMillis());
                }

                if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                    user.getCombatData().setLastEntityDamageAttack(System.currentTimeMillis());
                    user.getCombatData().setLastBowDamageTick(user.getConnectedTick());
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {


        if (e.getDamager() instanceof Arrow) {
            User user = Xan.getInstance().getUserManager().getUser(e.getEntity().getUniqueId());
            if (user != null) {
                Arrow arrow = (Arrow) e.getDamager();
                user.getCombatData().setLastBowDamage(System.currentTimeMillis());
                user.getCombatData().setLastBowStrength(arrow.getKnockbackStrength());
            }
        }

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            User user = Xan.getInstance().getUserManager().getUser(e.getDamager().getUniqueId());
            if (user != null) {


                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    int ticks = user.getCombatData().getCancelTicks();
                    if (e.isCancelled()) {
                        ticks += (ticks < 20 ? 1 : 0);
                    } else {
                        ticks -= (ticks > 0 ? 5 : 0);
                    }
                    user.getCombatData().setCancelTicks(ticks);
                }
            }

            User damageUser = Xan.getInstance().getUserManager().getUser(e.getEntity().getUniqueId());
            if (damageUser != null) {

                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    int ticks = damageUser.getCombatData().getNoDamageTicks();
                    if (e.isCancelled()) {
                        ticks += (ticks < 20 ? 1 : 0);
                    } else {
                        ticks -= (ticks > 0 ? 5 : 0);
                    }
                    damageUser.getCombatData().setNoDamageTicks(ticks);
                }

                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    damageUser.getCombatData().setLastAttackedTick(damageUser.getConnectedTick());
                    damageUser.getCombatData().setLastEntityDamageAttack(System.currentTimeMillis());
                }
                damageUser.getCombatData().setLastEntityDamage(System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getEntity().getUniqueId());
        if (user != null) {
            user.getCombatData().setLastDeath(System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.WOOD_BUTTON
                    || e.getClickedBlock().getType() == Material.STONE_BUTTON || e.getClickedBlock().getType() == Material.LEVER)) {
                user.getMovementData().setLastTelportInteractTick(user.getConnectedTick());
                user.getMovementData().setDidTeleportInteract(true);

            }
        }
    }

    @EventHandler
    public void onInteractRetarded(PlayerInteractEvent e) {
        try {
            if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) && e.getItem().getType() != Material.AIR) {
                User user = Xan.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
                if (user != null) {

                    //    if ((System.currentTimeMillis() - user.getPredictionProcessor().itemWaitPredict) < 100L) {
                    //    user.getPredictionProcessor().lastUseItem = System.currentTimeMillis();
                    //  }
                }
            }
        } catch (Exception ignored) {}
    }
}
