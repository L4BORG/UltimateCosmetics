package com.j0ach1mmall3.ultimatecosmetics.modules.trails;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class TrailsModule extends PluginModule<Main, Trails> {
    private BukkitTask task;

    public TrailsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Trails(this);
        this.task = new TrailsRunnable(this).runTaskTimer(this.getParent(), this.config.getDropInterval(), this.config.getDropInterval());
        new TrailsListener(this);
    }

    @Override
    public void onDisable() {
        this.task.cancel();
    }
}
