package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BannerCapesModule extends PluginModule<Main, BannerCapes> {
    private BukkitTask task;

    public BannerCapesModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new BannerCapes(this);
        this.task = new BannerCapesRunnable(this).runTaskTimer(this.getParent(), 1L, 1L);
    }

    @Override
    public void onDisable() {
        this.task.cancel();
    }
}
