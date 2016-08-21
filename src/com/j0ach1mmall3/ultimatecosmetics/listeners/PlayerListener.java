package com.j0ach1mmall3.ultimatecosmetics.listeners;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.integration.updatechecker.AsyncUpdateChecker;
import com.j0ach1mmall3.jlib.integration.updatechecker.UpdateCheckerResult;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.jlib.player.JLibPlayer;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.unique.DoubleJumpStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.Config;
import com.j0ach1mmall3.ultimatecosmetics.data.CosmeticsQueue;
import com.j0ach1mmall3.ultimatecosmetics.data.DataLoader;
import com.j0ach1mmall3.ultimatecosmetics.modules.balloons.Balloon;
import com.j0ach1mmall3.ultimatecosmetics.modules.blockpets.BlockPet;
import com.j0ach1mmall3.ultimatecosmetics.modules.pets.Pet;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
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
            for (DoubleJumpStorage group : this.plugin.getMisc().getDoubleJumpGroups()) {
                if (new JLibPlayer(p).hasCustomPermission(group.getPermission()) && p.getGameMode() != GameMode.CREATIVE && p.getVehicle() == null && p.getPassenger() == null) {
                    e.setCancelled(true);
                    p.setAllowFlight(false);
                    p.setFlying(false);
                    p.setVelocity(p.getVelocity().multiply(group.getMultiplier()));
                    Sounds.broadcastSound(group.getSound(), p.getLocation());
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
                if (new JLibPlayer(p).hasCustomPermission(group.getPermission()) && p.getGameMode() != GameMode.CREATIVE && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR && p.getVehicle() == null && p.getPassenger() == null) {
                    p.setAllowFlight(true);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
        final Player p = e.getPlayer();
        if (this.plugin.getMisc().isStackerEnabled()) {
            this.plugin.getDataLoader().getStacker(p, new CallbackHandler<Boolean>() {
                @Override
                public void callback(Boolean o) {
                    if(o && new JLibPlayer(p).hasCustomPermission(PlayerListener.this.plugin.getMisc().getStackerPermission())) {
                        if (e.getRightClicked() instanceof Player) {
                            final Player clicked = (Player) e.getRightClicked();
                            PlayerListener.this.plugin.getDataLoader().getStacker(clicked, new CallbackHandler<Boolean>() {
                                @Override
                                public void callback(Boolean o1) {
                                    if (o1) {
                                        e.setCancelled(true);

                                        if (p.getPassenger() instanceof ArmorStand) return;
                                        if (clicked.getVehicle() != null) return;
                                        if (p.getPassenger() != null)  p.getPassenger().leaveVehicle();
                                        setPassenger(p, null);
                                        setPassenger(p, e.getRightClicked());

                                        String stackedPlayer = PlayerListener.this.plugin.getLang().getStackedPlayer();
                                        if(!stackedPlayer.isEmpty()) p.sendMessage(Placeholders.parse(PlayerListener.this.plugin.getMisc().getStackerPrefix(), p) + Placeholders.parse(stackedPlayer, p).replace("%target%", clicked.getName()));
                                        String stackedByPlayer = PlayerListener.this.plugin.getLang().getStackedByPlayer();
                                        if(!stackedByPlayer.isEmpty()) clicked.sendMessage(Placeholders.parse(PlayerListener.this.plugin.getMisc().getStackerPrefix(), clicked) + Placeholders.parse(stackedByPlayer, clicked).replace("%stacker%", p.getName()));
                                    } else Methods.informPlayerNoPermission(p, Placeholders.parse(PlayerListener.this.plugin.getLang().getStackedNotEnabled().replace("%stacked%", clicked.getName()), p));
                                }
                            });
                        } else if (!PlayerListener.this.plugin.getMisc().isStackerStackPlayersOnly() && e.getRightClicked() instanceof Creature && e.getRightClicked().getCustomName() == null && e.getRightClicked().getPassenger() == null) {
                            e.setCancelled(true);

                            if (p.getPassenger() instanceof ArmorStand) return;
                            if (e.getRightClicked().getVehicle() != null) return;
                            if (p.getPassenger() != null)  p.getPassenger().leaveVehicle();
                            setPassenger(p, null);
                            setPassenger(p, e.getRightClicked());

                            String stackedPlayer = PlayerListener.this.plugin.getLang().getStackedPlayer();
                            if(!stackedPlayer.isEmpty()) p.sendMessage(Placeholders.parse(PlayerListener.this.plugin.getMisc().getStackerPrefix(), p) + Placeholders.parse(stackedPlayer, p).replace("%target%", e.getRightClicked().getType().name().replace("_", " ").toLowerCase()));
                        }
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (e.getEntity().getVehicle() != null && p.getName().equals(e.getEntity().getVehicle().getName())) {
                e.setCancelled(true);
                p.getPassenger().leaveVehicle();
                setPassenger(p, null);
                e.getEntity().setVelocity(p.getLocation().getDirection().multiply(2));
                e.getEntity().setFallDistance(-100.0F);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final DataLoader loader = this.plugin.getDataLoader();
        Config config = this.plugin.getBabies();
        new BukkitRunnable() {
            @Override
            public void run() {
                loader.getAmmoCache().load(p);
                loader.createStacker(p);
                loader.createPetName(p);
                loader.createQueue(p);
            }
        }.runTaskAsynchronously(this.plugin);
        if (config.isGiveItemOnJoin()) p.getInventory().setItem(config.getJoinItem().getPosition(), config.getJoinItem().getItem());
        if (config.isUpdateChecker() && p.hasPermission("uc.reload")) {
            AsyncUpdateChecker checker = new AsyncUpdateChecker(this.plugin, 5885, this.plugin.getDescription().getVersion());
            checker.checkUpdate(new CallbackHandler<UpdateCheckerResult>() {
                @Override
                public void callback(UpdateCheckerResult o) {
                    if(o.getType() == UpdateCheckerResult.ResultType.NEW_UPDATE) {
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + '[' + ChatColor.DARK_RED + "UltimateCosmetics" + ChatColor.RED + ChatColor.BOLD + ']' + ChatColor.GOLD + "A new update is available!");
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + '[' + ChatColor.DARK_RED + "UltimateCosmetics" + ChatColor.RED + ChatColor.BOLD + ']' + ChatColor.GOLD + "Version " + o.getNewVersion());
                    }
                }
            });
        }
        if (!config.isRemoveCosmeticsOnLogOut()) this.plugin.getDataLoader().giveBackQueue(p);
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.PHYSICAL) {
            Player p = e.getPlayer();
            if (General.areSimilar(this.plugin.getBabies().getJoinItem().getItem(), p.getItemInHand())) {
                e.setCancelled(true);
                this.plugin.getGuiHandler().openMainGui(p);
                if(this.plugin.getBabies().getGuiOpenSound() != null) Sounds.playSound(p, this.plugin.getBabies().getGuiOpenSound());
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        CosmeticsAPI api = this.plugin.getApi();
        if (this.plugin.getBabies().isRemoveCosmeticsOnWorldChange()) {
            for(Cosmetic cosmetic : this.plugin.getApi().getCosmetics(p)) {
                cosmetic.remove();
            }
        }
        else {
            for(Balloon cosmetic : api.getCosmetics(p, Balloon.class)) {
                cosmetic.give();
            }
            for(BlockPet cosmetic : api.getCosmetics(p, BlockPet.class)) {
                cosmetic.give();
            }
            for(Pet cosmetic : api.getCosmetics(p, Pet.class)) {
                cosmetic.give();
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final CosmeticsQueue queue =  new CosmeticsQueue(this.plugin, p);
        for(Cosmetic cosmetic : this.plugin.getApi().getCosmetics(p)) {
            cosmetic.remove();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerListener.this.plugin.getDataLoader().updateQueue(p.getUniqueId().toString(), queue);
                PlayerListener.this.plugin.getDataLoader().getAmmoCache().unload(p);
            }
        }.runTaskAsynchronously(this.plugin);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        final Player p = e.getPlayer();
        final CosmeticsQueue queue =  new CosmeticsQueue(this.plugin, p);
        for(Cosmetic cosmetic : this.plugin.getApi().getCosmetics(p)) {
            cosmetic.remove();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerListener.this.plugin.getDataLoader().updateQueue(p.getUniqueId().toString(), queue);
                PlayerListener.this.plugin.getDataLoader().getAmmoCache().unload(p);
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public static void setPassenger(Player p, Entity entity) {
        p.setPassenger(entity);
        try {
            Object packetPlayOutMount = ReflectionAPI.getNmsClass("PacketPlayOutMount").getConstructor(ReflectionAPI.getNmsClass("Entity")).newInstance(ReflectionAPI.getHandle((Object) p));
            ReflectionAPI.sendPacket(p, packetPlayOutMount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
