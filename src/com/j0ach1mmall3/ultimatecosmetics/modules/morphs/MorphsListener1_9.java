package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 4/05/2016
 */
public final class MorphsListener1_9 implements Listener {
    private final MorphsModule module;

    public MorphsListener1_9(MorphsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onPlayerSwapItems(PlayerSwapHandItemsEvent e) {
        if (this.module.getConfig().isAbilityItem(e.getOffHandItem())) e.setCancelled(true);
    }
}
