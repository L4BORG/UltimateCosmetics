package com.j0ach1mmall3.ultimatecosmetics.modules.auras;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class AurasModule extends PluginModule {
    private BukkitTask task;

    public AurasModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Auras(this);
        this.task = new AurasRunnable(this).runTaskTimer(this.getParent(), ((Auras) this.config).getUpdateInterval() * 20, ((Auras) this.config).getUpdateInterval() * 20);
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
