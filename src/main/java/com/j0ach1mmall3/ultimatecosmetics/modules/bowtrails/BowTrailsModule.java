package com.j0ach1mmall3.ultimatecosmetics.modules.bowtrails;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BowTrailsModule extends PluginModule<Main, BowTrails> {
    private final Map<Arrow, Cosmetic> arrowsMap = new ConcurrentHashMap<>();
    private int task;

    public BowTrailsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new BowTrails(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<Arrow, Cosmetic> entry : BowTrailsModule.this.arrowsMap.entrySet()) {
                    Location l = entry.getKey().getLocation();
                    ParticleCosmeticStorage particleCosmeticStorage = (ParticleCosmeticStorage) entry.getValue().getCosmeticStorage();
                    Methods.broadcastSafeParticle(l, particleCosmeticStorage.getParticle(), particleCosmeticStorage.getId(), particleCosmeticStorage.getData(), particleCosmeticStorage.getSpeed(), 1, BowTrailsModule.this.config.getViewDistance());
                }
            }
        }, this.config.getUpdateInterval(), this.config.getUpdateInterval());
        new BowTrailsListener(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
        for(Arrow arrow : this.arrowsMap.keySet()) {
            arrow.remove();
        }
        this.arrowsMap.clear();
    }

    Map<Arrow, Cosmetic> getArrowsMap() {
        return this.arrowsMap;
    }
}
