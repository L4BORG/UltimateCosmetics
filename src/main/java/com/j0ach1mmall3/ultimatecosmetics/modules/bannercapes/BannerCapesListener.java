package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2015
 */
public final class BannerCapesListener implements Listener {
    public BannerCapesListener(BannerCapesModule module) {
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(e.getEntity().hasMetadata("BannerCape")) e.setCancelled(true);
    }
}
