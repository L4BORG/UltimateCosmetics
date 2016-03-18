package com.j0ach1mmall3.ultimatecosmetics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class EntityListener implements Listener {

    public EntityListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) e.setCancelled(false);
    }
}
