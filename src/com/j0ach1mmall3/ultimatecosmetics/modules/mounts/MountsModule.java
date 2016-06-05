package com.j0ach1mmall3.ultimatecosmetics.modules.mounts;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class MountsModule extends PluginModule<Main, Mounts> {
    private BukkitTask task;

    public MountsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Mounts(this);
        new MountsListener(this);
        this.task = new MountsRunnable(this).runTaskTimer(this.getParent(), 5L, 5L);
    }

    @Override
    public void onDisable() {
        // NOP
    }
}
