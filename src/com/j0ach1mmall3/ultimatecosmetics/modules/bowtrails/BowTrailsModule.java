package com.j0ach1mmall3.ultimatecosmetics.modules.bowtrails;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BowTrailsModule extends PluginModule<Main, BowTrails> {
    private final Map<Arrow, Cosmetic> arrowsMap = new ConcurrentHashMap<>();
    private BukkitTask task;

    public BowTrailsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new BowTrails(this);
        this.task = new BowTrailsRunnable(this).runTaskTimer(this.getParent(), this.config.getUpdateInterval(), this.config.getUpdateInterval());
        new BowTrailsListener(this);
    }

    @Override
    public void onDisable() {
        this.task.cancel();
        for(Arrow arrow : this.arrowsMap.keySet()) {
            arrow.remove();
        }
        this.arrowsMap.clear();
    }

    Map<Arrow, Cosmetic> getArrowsMap() {
        return this.arrowsMap;
    }
}
