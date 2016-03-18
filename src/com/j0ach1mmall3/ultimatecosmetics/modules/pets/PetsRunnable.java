package com.j0ach1mmall3.ultimatecosmetics.modules.pets;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public final class PetsRunnable extends BukkitRunnable {
    private final PetsModule module;

    public PetsRunnable(PetsModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(CosmeticType.PET)) {
            Creature entity = ((Pet) cosmetic).getEntity();
            Player p = cosmetic.getPlayer();
            if (!entity.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || entity.getLocation().distance(p.getLocation()) >= ((Pets) this.module.getConfig()).getTeleportDistance()) {
                entity.teleport(p);
                entity.setTarget(p);
            }
        }
    }
}
