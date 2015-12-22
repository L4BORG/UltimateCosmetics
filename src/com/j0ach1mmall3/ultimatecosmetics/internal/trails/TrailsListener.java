package com.j0ach1mmall3.ultimatecosmetics.internal.trails;

import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Trail;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by j0ach1mmall3 on 17:09 23/08/2015 using IntelliJ IDEA.
 */
public final class TrailsListener implements Listener {
    private final Collection<Entity> entitiesQueue = new ArrayList<>();

    public TrailsListener(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startScheduler(plugin);
    }

    private void startScheduler(final Main plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Trail trail : plugin.getApi().getTrails()) {
                    Player p = trail.getPlayer();
                    ItemStack ci = trail.getTrailStorage().getItem().clone();
                    ItemMeta im = ci.getItemMeta();
                    im.setDisplayName(String.valueOf(Random.getInt(100)));
                    ci.setItemMeta(im);
                    final Item i = p.getWorld().dropItem(p.getLocation(), ci);
                    i.setPickupDelay(Integer.MAX_VALUE);
                    TrailsListener.this.entitiesQueue.add(i);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            i.remove();
                            TrailsListener.this.entitiesQueue.remove(i);
                        }
                    }.runTaskLater(plugin,  plugin.getTrails().getRemoveDelay() * 20);
                }
            }
        }.runTaskTimer(plugin, 0, (long) (plugin.getTrails().getDropInterval() * 20.0));
    }

    public void cleanup() {
        for(Entity entity : this.entitiesQueue) {
            entity.remove();
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(20.0, 20.0, 20.0)) {
            if(isItem(ent)) ent.remove();
        }
    }

    @EventHandler
    public void onInventoryPickup(InventoryPickupItemEvent e) {
        if (isItem(e.getItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {
        if (isItem(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        if (isItem(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(20.0, 20.0, 20.0)) {
            if(isItem(ent)) ent.remove();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(20.0, 20.0, 20.0)) {
            if(isItem(ent)) ent.remove();
        }
    }

    private boolean isItem(Entity item) {
        for (Entity ent : this.entitiesQueue) {
            if (item.getUniqueId().equals(ent.getUniqueId())) return true;
        }
        return false;
    }
}
