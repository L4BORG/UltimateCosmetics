package com.j0ach1mmall3.ultimatecosmetics.modules.balloons;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BalloonsModule extends PluginModule<Main, Balloons> {
    private BukkitTask task;

    public BalloonsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Balloons(this);
        this.task = new BalloonsRunnable(this).runTaskTimer(this.getParent(), this.config.getTeleportInterval(), this.config.getTeleportInterval());
        new BalloonsListener(this);
    }

    @Override
    public void onDisable() {
        this.task.cancel();
    }
}
