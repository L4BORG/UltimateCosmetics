package com.j0ach1mmall3.ultimatecosmetics.internal.balloons;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Balloon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;

public final class BalloonsListener implements Listener {
    private final Main plugin;

    public BalloonsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startScheduler();
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
            e.setCancelled(true);
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityUnleash(EntityUnleashEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if(ent.hasMetadata("Balloon") || ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
            e.setCancelled(true);
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (this.plugin.getApi().hasBalloon(p)) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType().name().contains("FENCE")) e.setCancelled(true);
            }
        }
    }

    private void startScheduler() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Balloon balloon : new ArrayList<>(BalloonsListener.this.plugin.getApi().getBalloons())) {
                    Bat bat = balloon.getBat();
                    Player p = balloon.getPlayer();
                    if (bat != null && (!bat.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || bat.getLocation().distance(p.getLocation()) >= BalloonsListener.this.plugin.getBalloons().getTeleportDistance())) balloon.give();
                }
            }
        }.runTaskTimer(this.plugin, 0L, this.plugin.getBalloons().getTeleportInterval() * 20);
    }
}
