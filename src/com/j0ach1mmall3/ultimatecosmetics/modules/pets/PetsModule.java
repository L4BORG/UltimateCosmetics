package com.j0ach1mmall3.ultimatecosmetics.modules.pets;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class PetsModule extends PluginModule {
    private BukkitTask task;

    public PetsModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Pets(this);
        new PetsListener(this);
        this.task = new PetsRunnable(this).runTaskTimer(this.getParent(), 5L, 5L);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public ConfigLoader getConfig() {
        return this.config;
    }
}
