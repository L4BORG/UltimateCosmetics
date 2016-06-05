package com.j0ach1mmall3.ultimatecosmetics.modules.particles;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class ParticlesModule extends PluginModule<Main, Particles> {
    private BukkitTask task;

    public ParticlesModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Particles(this);
        this.task = new ParticlesRunnable(this).runTaskTimer(this.getParent(), this.config.getUpdateInterval(), this.config.getUpdateInterval());
    }

    @Override
    public void onDisable() {
        this.task.cancel();
    }

}
