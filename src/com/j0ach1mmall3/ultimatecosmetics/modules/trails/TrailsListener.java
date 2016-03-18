package com.j0ach1mmall3.ultimatecosmetics.modules.trails;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class TrailsListener implements Listener {
    private final TrailsModule module;

    public TrailsListener(TrailsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(20.0, 20.0, 20.0)) {
            if(this.isItem(ent)) ent.remove();
        }
    }

    @EventHandler
    public void onInventoryPickup(InventoryPickupItemEvent e) {
        if (this.isItem(e.getItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {
        if (this.isItem(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        if (this.isItem(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(20.0, 20.0, 20.0)) {
            if(this.isItem(ent)) ent.remove();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(20.0, 20.0, 20.0)) {
            if(this.isItem(ent)) ent.remove();
        }
    }

    private boolean isItem(Entity item) {
        for (Entity ent : ((Main) this.module.getParent()).getEntitiesQueue()) {
            if (item.getUniqueId().equals(ent.getUniqueId())) return true;
        }
        return false;
    }
}
