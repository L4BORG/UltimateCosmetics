package com.j0ach1mmall3.ultimatecosmetics.modules.mounts;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class MountsModule extends PluginModule<Main, Mounts> {
    private int task;

    public MountsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Mounts(this);
        new MountsListener(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (Mount cosmetic : MountsModule.this.parent.getApi().getAllCosmetics(Mount.class)) {
                    Entity ent = cosmetic.getEntity();
                    Player p = cosmetic.getPlayer();
                    if (ent.getPassenger() == null) {
                        cosmetic.remove(MountsModule.this.parent);
                        return;
                    }
                    Vector v = p.getLocation().getDirection().setY(0).normalize().multiply(4);
                    Location loc = p.getLocation().add(v);
                    Reflection.setNavigation(ent, loc, MountsModule.this.config.getMountSpeed());
                }
            }
        }, 5L, 5L);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
