package com.j0ach1mmall3.ultimatecosmetics.modules.cloaks;

import com.j0ach1mmall3.jlib.effectsapi.Effect2D;
import com.j0ach1mmall3.jlib.effectsapi.HaloEffect;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class CloaksModule extends PluginModule<Main, Cloaks> {
    private int task;

    public CloaksModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Cloaks(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (Cloak cosmetic : CloaksModule.this.parent.getApi().getAllCosmetics(Cloak.class)) {
                    Player p = cosmetic.getPlayer();
                    long lastWalked = JavaPlugin.getPlugin(com.j0ach1mmall3.jlib.Main.class).getPlayerListener().getLastWalked(p);
                    CloakStorage pcs = cosmetic.getCosmeticStorage();
                    if (pcs == null || (lastWalked != 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWalked) < 0.1)) return;
                    switch (pcs.getShape()) {
                        case HALO:
                            new HaloEffect(p.getLocation().add(0, 2, 0), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), CloaksModule.this.config.getViewDistance(), 0.5, 16).play(CloaksModule.this.parent);
                            break;
                        case EFFECT2D:
                            new Effect2D(p.getLocation().add(0, 2, 0), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), CloaksModule.this.config.getViewDistance(), pcs.getGridShape()).play(CloaksModule.this.parent);
                            break;
                        default:
                            // NOP
                    }
                }
            }
        }, this.config.getUpdateInterval(), this.config.getUpdateInterval());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
