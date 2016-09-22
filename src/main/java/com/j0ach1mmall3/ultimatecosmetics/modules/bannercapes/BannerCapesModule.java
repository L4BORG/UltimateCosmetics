package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BannerCapesModule extends PluginModule<Main, BannerCapes> {
    private int task;

    public BannerCapesModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new BannerCapes(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (BannerCape cosmetic : BannerCapesModule.this.parent.getApi().getAllCosmetics(BannerCape.class)) {
                    ArmorStand armorStand = cosmetic.getArmorStand();
                    Player p = cosmetic.getPlayer();
                    long lastWalked = JavaPlugin.getPlugin(com.j0ach1mmall3.jlib.Main.class).getPlayerListener().getLastWalked(p);
                    if(lastWalked != 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWalked) < 0.1) armorStand.setHelmet(null);
                    else if(armorStand.getHelmet().getType() == Material.AIR) armorStand.setHelmet(cosmetic.getCosmeticStorage().getjLibItem().getItemStack());
                    armorStand.teleport(p.getEyeLocation().subtract(0, 1, 0));
                }
            }
        }, 1, 1);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
