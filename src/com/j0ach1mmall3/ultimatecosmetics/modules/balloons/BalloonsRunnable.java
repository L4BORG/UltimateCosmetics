package com.j0ach1mmall3.ultimatecosmetics.modules.balloons;

import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public final class BalloonsRunnable extends BukkitRunnable {
    private final BalloonsModule module;

    public BalloonsRunnable(BalloonsModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Balloon cosmetic : this.module.getParent().getApi().getAllCosmetics(Balloon.class)) {
            Bat bat = cosmetic.getBat();
            Player p = cosmetic.getPlayer();
            if (!bat.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || bat.getLocation().distance(p.getLocation()) >= this.module.getConfig().getTeleportDistance()) cosmetic.give();
        }
    }
}
