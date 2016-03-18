package com.j0ach1mmall3.ultimatecosmetics.modules.balloons;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
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
        for (Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(CosmeticType.BALLOON)) {
            Bat bat = ((Balloon) cosmetic).getBat();
            Player p = cosmetic.getPlayer();
            if (!bat.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || bat.getLocation().distance(p.getLocation()) >= ((Balloons) this.module.getConfig()).getTeleportDistance()) cosmetic.give();
        }
    }
}
