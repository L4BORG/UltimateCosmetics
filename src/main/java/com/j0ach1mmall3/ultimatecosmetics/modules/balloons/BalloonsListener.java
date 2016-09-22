package com.j0ach1mmall3.ultimatecosmetics.modules.balloons;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class BalloonsListener implements Listener {
    private final BalloonsModule module;

    public BalloonsListener(BalloonsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            for(Balloon cosmetic : this.module.getParent().getApi().getCosmetics(owner, Balloon.class)) {
                cosmetic.give(this.module.getParent());
            }
            e.setCancelled(true);
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            for(Balloon cosmetic : this.module.getParent().getApi().getCosmetics(owner, Balloon.class)) {
                cosmetic.give(this.module.getParent());
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityUnleash(EntityUnleashEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            for(Balloon cosmetic : this.module.getParent().getApi().getCosmetics(owner, Balloon.class)) {
                cosmetic.give(this.module.getParent());
            }
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            for(Balloon cosmetic : this.module.getParent().getApi().getCosmetics(owner, Balloon.class)) {
                cosmetic.give(this.module.getParent());
            }
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            for(Balloon cosmetic : this.module.getParent().getApi().getCosmetics(owner, Balloon.class)) {
                cosmetic.give(this.module.getParent());
            }
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            for(Balloon cosmetic : this.module.getParent().getApi().getCosmetics(owner, Balloon.class)) {
                cosmetic.give(this.module.getParent());
            }
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            for(Balloon cosmetic : this.module.getParent().getApi().getCosmetics(owner, Balloon.class)) {
                cosmetic.give(this.module.getParent());
            }
            e.setCancelled(true);
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            for(Balloon cosmetic : this.module.getParent().getApi().getCosmetics(owner, Balloon.class)) {
                cosmetic.give(this.module.getParent());
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (this.module.getParent().getApi().hasCosmetics(p, Balloon.class) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().name().contains("FENCE")) e.setCancelled(true);
    }
}
