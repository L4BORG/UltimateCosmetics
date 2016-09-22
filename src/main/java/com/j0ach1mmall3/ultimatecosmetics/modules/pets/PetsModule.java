package com.j0ach1mmall3.ultimatecosmetics.modules.pets;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class PetsModule extends PluginModule<Main, Pets> {
    private int task;

    public PetsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Pets(this);
        new PetsListener(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (Pet cosmetic : PetsModule.this.parent.getApi().getAllCosmetics(Pet.class)) {
                    Creature entity = cosmetic.getEntity();
                    Player p = cosmetic.getPlayer();
                    if (!entity.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || entity.getLocation().distance(p.getLocation()) >= PetsModule.this.config.getTeleportDistance()) {
                        entity.teleport(p);
                        entity.setTarget(p);
                    }
                }
            }
        }, this.config.getTeleportInterval(), this.config.getTeleportInterval());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
