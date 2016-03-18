package com.j0ach1mmall3.ultimatecosmetics.modules.particles;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class ParticlesModule extends PluginModule {
    private BukkitTask task;

    public ParticlesModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Particles(this);
        this.task = new ParticlesRunnable(this).runTaskTimer(this.getParent(), ((Particles) this.config).getUpdateInterval(), ((Particles) this.config).getUpdateInterval());
    }

    @Override
    public void onDisable() {
        this.task.cancel();
    }

    @Override
    public ConfigLoader getConfig() {
        return this.config;
    }
}
