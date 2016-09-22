package com.j0ach1mmall3.ultimatecosmetics.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity ent = e.getEntity();
        if(ent instanceof ArmorStand && !((ArmorStand) ent).isVisible() && damager instanceof Player) {
            e.setCancelled(true);
            Player p = (Player) e.getDamager();
            Block b = p.getTargetBlock(EnumSet.noneOf(Material.class), 3);
            PlayerInteractEvent event = new PlayerInteractEvent(p, b == null ? Action.LEFT_CLICK_AIR : Action.LEFT_CLICK_BLOCK, p.getItemInHand(), b, null);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        Entity ent = e.getRightClicked();
        if(ent instanceof ArmorStand && !((ArmorStand) ent).isVisible()) {
            e.setCancelled(true);
            Block b = p.getTargetBlock(EnumSet.noneOf(Material.class), 3);
            PlayerInteractEvent event = new PlayerInteractEvent(p, b == null ? Action.RIGHT_CLICK_AIR : Action.RIGHT_CLICK_BLOCK, p.getItemInHand(), b, null, e.getHand());
            Bukkit.getPluginManager().callEvent(event);
        }
    }
}
