package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2015
 */
public final class BannerCapesListener implements Listener {
    private final BannerCapesModule module;

    public BannerCapesListener(BannerCapesModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(e.getEntityType() == EntityType.ARMOR_STAND && !((ArmorStand) e.getEntity()).isVisible()) e.setCancelled(true);
    }
}
