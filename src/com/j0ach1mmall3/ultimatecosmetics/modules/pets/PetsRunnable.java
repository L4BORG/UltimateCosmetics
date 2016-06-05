package com.j0ach1mmall3.ultimatecosmetics.modules.pets;

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
        for (Pet cosmetic : this.module.getParent().getApi().getAllCosmetics(Pet.class)) {
            Creature entity = cosmetic.getEntity();
            Player p = cosmetic.getPlayer();
            if (!entity.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || entity.getLocation().distance(p.getLocation()) >= this.module.getConfig().getTeleportDistance()) {
                entity.teleport(p);
                entity.setTarget(p);
            }
        }
    }
}
