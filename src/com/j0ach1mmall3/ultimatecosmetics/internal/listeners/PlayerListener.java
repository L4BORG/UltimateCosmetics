package com.j0ach1mmall3.ultimatecosmetics.internal.listeners;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.integration.updatechecker.AsyncUpdateChecker;
import com.j0ach1mmall3.jlib.integration.updatechecker.UpdateCheckerResult;
import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Morph;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.DoubleJumpStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import com.j0ach1mmall3.ultimatecosmetics.internal.data.CosmeticsQueue;
import com.j0ach1mmall3.ultimatecosmetics.internal.data.DataLoader;
import com.j0ach1mmall3.ultimatecosmetics.internal.gui.CosmeticsGuiHandler;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by j0ach1mmall3 on 1:53 21/08/2015 using IntelliJ IDEA.
 */
public final class PlayerListener implements Listener {
    private final Main plugin;

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDoubleJump(PlayerToggleFlightEvent e) {
        if (this.plugin.getMisc().isDoubleJumpEnabled()) {
            Player p = e.getPlayer();
            if (this.plugin.getApi().hasMorph(p)) {
                Morph morph = this.plugin.getApi().getMorph(p);
                String type = morph.getMorphStorage().getMorphType().toLowerCase();
                if ("bat".equalsIgnoreCase(type) || "blaze".equalsIgnoreCase(type) || "ghast".equalsIgnoreCase(type) || "ender_dragon".equalsIgnoreCase(type))
                    return;
            }
            for (DoubleJumpStorage group : this.plugin.getMisc().getDoubleJumpGroups()) {
                if (Methods.hasPermission(p, group.getPermission()) && p.getGameMode() != GameMode.CREATIVE && p.getVehicle() == null) {
                    e.setCancelled(true);
                    p.setAllowFlight(false);
                    p.setFlying(false);
                    p.setVelocity(p.getVelocity().multiply(group.getMultiplier()));
                    Sounds.broadcastSound(Sound.FIREWORK_BLAST, p.getLocation());
                    p.setFallDistance(-100.0F);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (this.plugin.getMisc().isDoubleJumpEnabled()) {
            Player p = e.getPlayer();
            for (DoubleJumpStorage group : this.plugin.getMisc().getDoubleJumpGroups()) {
                if (Methods.hasPermission(p, group.getPermission()) && p.getGameMode() != GameMode.CREATIVE && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR && p.getVehicle() == null) {
                    if (this.plugin.getApi().hasMorph(p)) {
                        Morph morph = this.plugin.getApi().getMorph(p);
                        String type = morph.getMorphStorage().getMorphType().toLowerCase();
                        if ("bat".equalsIgnoreCase(type) || "blaze".equalsIgnoreCase(type) || "ghast".equalsIgnoreCase(type) || "ender_dragon".equalsIgnoreCase(type))
                            return;
                    }
                    p.setAllowFlight(true);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractWithEntity(final PlayerInteractEntityEvent e) {
        final Player p = e.getPlayer();
        if (this.plugin.getMisc().isStackerEnabled()) {
            this.plugin.getDataLoader().getStacker(p, new CallbackHandler<Boolean>() {
                @Override
                public void callback(Boolean b) {
                    if(b) {
                        if (Methods.hasPermission(p, PlayerListener.this.plugin.getMisc().getStackerPermission())) {
                            if (e.getRightClicked() instanceof Player) {
                                final Player clicked = (Player) e.getRightClicked();
                                PlayerListener.this.plugin.getDataLoader().getStacker(clicked, new CallbackHandler<Boolean>() {
                                    @Override
                                    public void callback(Boolean b1) {
                                        if (b1) {
                                            e.setCancelled(true);
                                            if (p.getVehicle() != null) p.getVehicle().remove();
                                            if (clicked.getVehicle() != null) return;
                                            p.setPassenger(clicked);
                                            p.sendMessage(Placeholders.parse(PlayerListener.this.plugin.getMisc().getStackerPrefix(), p) + Placeholders.parse(PlayerListener.this.plugin.getLang().getStackedPlayer(), p).replace("%target%", clicked.getName()));
                                            clicked.sendMessage(Placeholders.parse(PlayerListener.this.plugin.getMisc().getStackerPrefix(), clicked) + Placeholders.parse(PlayerListener.this.plugin.getLang().getStackedByPlayer(), clicked).replace("%stacker%", p.getName()));
                                        } else PlayerListener.this.plugin.informPlayerNoPermission(p, Placeholders.parse(PlayerListener.this.plugin.getLang().getStackedNotEnabled().replace("{stacked}", clicked.getName()), p));
                                    }
                                });
                            } else if (!PlayerListener.this.plugin.getMisc().isStackerStackPlayersOnly() && e.getRightClicked() instanceof Creature && e.getRightClicked().getCustomName() == null) {
                                e.setCancelled(true);
                                p.setPassenger(e.getRightClicked());
                                p.sendMessage(Placeholders.parse(PlayerListener.this.plugin.getMisc().getStackerPrefix(), p) + Placeholders.parse(PlayerListener.this.plugin.getLang().getStackedPlayer(), p).replace("%target%", e.getRightClicked().getType().name().replace("_", " ").toLowerCase()));
                            }
                        }
                    }
                }
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (e.getEntity().getVehicle() != null) {
                if (e.getEntity().getVehicle() instanceof Player) {
                    AnimalTamer veh = (AnimalTamer) e.getEntity().getVehicle();
                    if (p.getName().equals(veh.getName())) {
                        e.setCancelled(true);
                        e.getEntity().leaveVehicle();
                        e.getEntity().setVelocity(p.getLocation().getDirection().multiply(2));
                        e.getEntity().setFallDistance(-100.0F);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final String uuid = p.getUniqueId().toString();
        final DataLoader loader = this.plugin.getDataLoader();
        new BukkitRunnable() {
            @Override
            public void run() {
                loader.loadAmmo(uuid);
                loader.createStacker(p);
                loader.createQueue(p);
            }
        }.runTaskAsynchronously(this.plugin);
        if (this.plugin.getBabies().isGiveItemOnJoin()) p.getInventory().setItem(this.plugin.getBabies().getJoinItemSlot(), this.plugin.getBabies().getJoinItem());
        if (this.plugin.getBabies().isUpdateChecker() && p.hasPermission("uc.reload")) {
            AsyncUpdateChecker checker = new AsyncUpdateChecker(this.plugin, 5885, this.plugin.getDescription().getVersion());
            checker.checkUpdate(new CallbackHandler<UpdateCheckerResult>() {
                @Override
                public void callback(UpdateCheckerResult updateCheckerResult) {
                    if(updateCheckerResult.getType() == UpdateCheckerResult.ResultType.NEW_UPDATE) {
                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + '[' + ChatColor.DARK_RED + "UltimateCosmetics" + ChatColor.RED + ChatColor.BOLD + ']' + ChatColor.GOLD + "A new update is available!");
                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + '[' + ChatColor.DARK_RED + "UltimateCosmetics" + ChatColor.RED + ChatColor.BOLD + ']' + ChatColor.GOLD + "Version " + updateCheckerResult.getNewVersion());
                    }
                }
            });
        }
        if (!this.plugin.getBabies().isRemoveCosmeticsOnLogOut()) this.plugin.getDataLoader().giveBackQueue(p);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.PHYSICAL) {
            Player p = e.getPlayer();
            if (this.plugin.getBabies().getJoinItem().isSimilar(p.getItemInHand())) {
                e.setCancelled(true);
                if (this.plugin.getBabies().getGuiOpenSound() != null)
                    Sounds.playSound(p, this.plugin.getBabies().getGuiOpenSound());
                CosmeticsGuiHandler.open(p);
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        CosmeticsAPI api = this.plugin.getApi();
        if (this.plugin.getBabies().isRemoveCosmeticsOnWorldChange()) Methods.removeCosmetics(p, this.plugin);
        else {
            if (api.hasBalloon(p)) api.getBalloon(p).give();
            if (api.hasPet(p)) api.getPet(p).give();
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final CosmeticsQueue queue =  new CosmeticsQueue(this.plugin, p);
        Methods.removeCosmetics(p, this.plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerListener.this.plugin.getDataLoader().updateQueue(p, queue);
                PlayerListener.this.plugin.getDataLoader().unloadAmmo(p.getUniqueId().toString());
            }
        }.runTaskAsynchronously(this.plugin);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        final Player p = e.getPlayer();
        final CosmeticsQueue queue =  new CosmeticsQueue(this.plugin, p);
        Methods.removeCosmetics(p, this.plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerListener.this.plugin.getDataLoader().updateQueue(p, queue);
                PlayerListener.this.plugin.getDataLoader().unloadAmmo(p.getUniqueId().toString());
            }
        }.runTaskAsynchronously(this.plugin);
    }
}
