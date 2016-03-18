package com.j0ach1mmall3.ultimatecosmetics.modules.cloaks;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class CloaksModule extends PluginModule {
    private BukkitTask task;

    public CloaksModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Cloaks(this);
        this.task = new CloaksRunnable(this).runTaskTimer(this.getParent(), ((Cloaks) this.config).getUpdateInterval(), ((Cloaks) this.config).getUpdateInterval());
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
