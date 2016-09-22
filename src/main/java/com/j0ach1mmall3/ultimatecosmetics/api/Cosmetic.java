package com.j0ach1mmall3.ultimatecosmetics.api;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 13/10/2015
 */
public abstract class Cosmetic<C extends CosmeticStorage> {
    protected final Player player;
    protected final C cosmeticStorage;

    protected Cosmetic(Player player, C cosmeticStorage) {
        this.player = player;
        this.cosmeticStorage = cosmeticStorage;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final C getCosmeticStorage() {
        return this.cosmeticStorage;
    }

    public final void give(Main plugin) {
        if (plugin.getApi().hasCosmetics(this.player, this.getClass())) {
            for(Cosmetic cosmetic : plugin.getApi().getCosmetics(this.player, this.getClass())) {
                cosmetic.remove(plugin);
            }
            this.give(plugin);
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            if(this.giveInternal(plugin)) plugin.getApi().addCosmetic(this.player, this);
        }
    }

    public final void remove(Main plugin) {
        if (plugin.getApi().hasCosmetics(this.player, this.getClass())) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            plugin.getApi().removeCosmetic(this.player, this);
            this.removeInternal(plugin);
        }
    }

    protected abstract boolean giveInternal(Main plugin);

    protected abstract void removeInternal(Main plugin);
}
