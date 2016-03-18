package com.j0ach1mmall3.ultimatecosmetics.modules.bowtrails;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 31/10/2015
 */
public final class BowTrailsListener implements Listener {
    private final BowTrailsModule module;

    public BowTrailsListener(BowTrailsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();
            if (arrow.getShooter() instanceof Player) {
                Player p = (Player) arrow.getShooter();
                if (((Main) this.module.getParent()).getApi().hasCosmetics(p, CosmeticType.BOWTRAIL)) {
                    for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.BOWTRAIL)) {
                        this.module.getArrowsMap().put(arrow, cosmetic);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) this.module.getArrowsMap().remove(e.getEntity());
    }
}
