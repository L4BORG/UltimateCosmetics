package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import com.j0ach1mmall3.jlib.Main;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
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
        for (BannerCape cosmetic : this.module.getParent().getApi().getAllCosmetics(BannerCape.class)) {
            ArmorStand armorStand = cosmetic.getArmorStand();
            Player p = cosmetic.getPlayer();
            long lastWalked = JavaPlugin.getPlugin(Main.class).getPlayerListener().getLastWalked(p);
            if(lastWalked != 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWalked) < this.module.getConfig().getGiveDelay()) armorStand.setHelmet(null);
            else if(armorStand.getHelmet().getType() == Material.AIR) armorStand.setHelmet(cosmetic.getCosmeticStorage().getGuiItem().getItem());
            armorStand.teleport(p.getEyeLocation().subtract(0, 1, 0));
        }
    }
}