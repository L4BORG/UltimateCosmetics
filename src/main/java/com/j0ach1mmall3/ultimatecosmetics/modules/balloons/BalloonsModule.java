package com.j0ach1mmall3.ultimatecosmetics.modules.balloons;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BalloonsModule extends PluginModule<Main, Balloons> {
    private int task;

    public BalloonsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Balloons(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (Balloon cosmetic : BalloonsModule.this.parent.getApi().getAllCosmetics(Balloon.class)) {
                    Bat bat = cosmetic.getBat();
                    Player p = cosmetic.getPlayer();
                    if (!bat.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || bat.getLocation().distance(p.getLocation()) >= BalloonsModule.this.config.getTeleportDistance()) cosmetic.give(BalloonsModule.this.parent);
                }
            }
        }, this.config.getTeleportInterval(), this.config.getTeleportInterval());
        new BalloonsListener(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
