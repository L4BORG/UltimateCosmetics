package com.j0ach1mmall3.ultimatecosmetics.modules.trails;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class TrailsModule extends PluginModule {
    private BukkitTask task;

    public TrailsModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Trails(this);
        this.task = new TrailsRunnable(this).runTaskTimer(this.getParent(), ((Trails) this.config).getDropInterval(), ((Trails) this.config).getDropInterval());
        new TrailsListener(this);
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
