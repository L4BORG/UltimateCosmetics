package com.j0ach1mmall3.ultimatecosmetics.modules.auras;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class AurasModule extends PluginModule<Main, Auras> {
    private BukkitTask task;

    public AurasModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Auras(this);
        this.task = new AurasRunnable(this).runTaskTimer(this.getParent(), this.config.getUpdateInterval() * 20, this.config.getUpdateInterval() * 20);
    }

    @Override
    public void onDisable() {
        this.task.cancel();
    }
}
