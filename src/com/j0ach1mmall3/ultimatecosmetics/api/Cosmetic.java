package com.j0ach1mmall3.ultimatecosmetics.api;

import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 13/10/2015
 */
public abstract class Cosmetic<C extends CosmeticConfig, S extends CosmeticStorage> {
    protected final C cosmeticConfig;
    protected final Player player;
    protected final S cosmeticStorage;

    protected Cosmetic(C cosmeticConfig, Player player, S cosmeticStorage) {
        this.cosmeticConfig = cosmeticConfig;
        this.player = player;
        this.cosmeticStorage = cosmeticStorage;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final S getCosmeticStorage() {
        return this.cosmeticStorage;
    }

    public final void give() {
        CosmeticsAPI api = this.cosmeticStorage.getPlugin().getApi();
        if (api.hasCosmetics(this.player, this.getClass())) {
            for(Cosmetic cosmetic : api.getCosmetics(this.player, this.getClass())) {
                cosmetic.remove();
            }
            this.give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            if(this.giveInternal()) api.addCosmetic(this.player, this);
        }
    }

    public final void remove() {
        CosmeticsAPI api = this.cosmeticStorage.getPlugin().getApi();
        if (api.hasCosmetics(this.player, this.getClass())) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            api.removeCosmetic(this.player, this);
            this.removeInternal();
        }
    }

    protected abstract boolean giveInternal();

    protected abstract void removeInternal();
}
