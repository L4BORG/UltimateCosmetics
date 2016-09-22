package com.j0ach1mmall3.ultimatecosmetics.modules.auras;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class AurasModule extends PluginModule<Main, Auras> {
    private int task;

    public AurasModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Auras(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (Aura cosmetic : AurasModule.this.parent.getApi().getAllCosmetics(Aura.class)) {
                    cosmetic.giveInternal(AurasModule.this.parent);
                }
            }
        }, this.config.getUpdateInterval() * 20L, this.config.getUpdateInterval() * 20L);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
