package com.j0ach1mmall3.ultimatecosmetics.modules.hearts;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class HeartsModule extends PluginModule {
    private BukkitTask task;

    public HeartsModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Hearts(this);
        new HeartsListener(this);
        this.task = new HeartsRunnable(this).runTaskTimer(this.getParent(), 10L, 10L);
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
