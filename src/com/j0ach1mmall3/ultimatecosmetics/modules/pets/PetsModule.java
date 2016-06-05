package com.j0ach1mmall3.ultimatecosmetics.modules.pets;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class PetsModule extends PluginModule<Main, Pets> {
    private BukkitTask task;

    public PetsModule(Main parent) {
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
        // NOP
    }
}
