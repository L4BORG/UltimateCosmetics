package com.j0ach1mmall3.ultimatecosmetics.modules.hearts;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class HeartsModule extends PluginModule<Main, Hearts> {
    private int task;

    public HeartsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Hearts(this);
        new HeartsListener(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (Heart cosmetic : HeartsModule.this.parent.getApi().getAllCosmetics(Heart.class)) {
                    Player p = cosmetic.getPlayer();
                    if (cosmetic.getColorEffect() != null && !p.hasPotionEffect(cosmetic.getColorEffect().getType()) || cosmetic.getEffectsEffect() != null && !p.hasPotionEffect(cosmetic.getEffectsEffect().getType())) cosmetic.giveInternal(HeartsModule.this.parent);
                }
            }
        }, 10L, 10L);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
