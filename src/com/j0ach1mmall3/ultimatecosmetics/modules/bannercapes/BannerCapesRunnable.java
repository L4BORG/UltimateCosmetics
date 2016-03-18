package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public final class BannerCapesRunnable extends BukkitRunnable {
    private final BannerCapesModule module;

    public BannerCapesRunnable(BannerCapesModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(CosmeticType.BANNERCAPE)) {
            ArmorStand armorStand = ((BannerCape) cosmetic).getArmorStand();
            Player p = cosmetic.getPlayer();
            long lastWalked = ((com.j0ach1mmall3.jlib.Main) Bukkit.getPluginManager().getPlugin("JLib")).getJoinListener().getLastWalked(p);
            if(lastWalked != 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWalked) < ((BannerCapes) this.module.getConfig()).getGiveDelay()) armorStand.setHelmet(null);
            else if(armorStand.getHelmet().getType() == Material.AIR) armorStand.setHelmet(cosmetic.getCosmeticStorage().getGuiItem().getItem());
            armorStand.teleport(p.getEyeLocation().add(0, 100, 0));
        }
    }
}
