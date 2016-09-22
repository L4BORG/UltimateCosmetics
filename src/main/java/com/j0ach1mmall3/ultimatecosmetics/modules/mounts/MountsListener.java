package com.j0ach1mmall3.ultimatecosmetics.modules.mounts;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.HorseInventory;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class MountsListener implements Listener {
    private final MountsModule module;

    public MountsListener(MountsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        for(Entity entity : e.getEntity().getNearbyEntities(1, 1, 1)) {
            if(entity.hasMetadata("Mount")) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        for(Mount cosmetic : this.module.getParent().getApi().getCosmetics(p, Mount.class)) {
            cosmetic.remove(this.module.getParent());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
        if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if(this.module.getParent().getApi().hasCosmetics(p, Mount.class)) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory() instanceof HorseInventory) {
            Player p = (Player) e.getPlayer();
            if(this.module.getParent().getApi().hasCosmetics(p, Mount.class)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByMount(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("Mount")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        if (e.getDuration() != Integer.MAX_VALUE && e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getReason() != EntityTargetEvent.TargetReason.CUSTOM && e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        for(Mount cosmetic : this.module.getParent().getApi().getCosmetics(p, Mount.class)) {
            cosmetic.remove(this.module.getParent());
        }
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }
}
