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
public abstract class Cosmetic {
    protected final CosmeticConfig cosmeticConfig;
    protected final Player player;
    protected final CosmeticStorage cosmeticStorage;
    protected final CosmeticType cosmeticType;

    public Cosmetic(CosmeticConfig cosmeticConfig, Player player, CosmeticStorage cosmeticStorage, CosmeticType cosmeticType) {
        if(cosmeticStorage.getPlugin().getConfigByType(cosmeticType) == null) throw new IllegalStateException(cosmeticType.name().toLowerCase() + " aren't enabled!");
        this.cosmeticConfig = cosmeticConfig;
        this.player = player;
        this.cosmeticStorage = cosmeticStorage;
        this.cosmeticType = cosmeticType;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final CosmeticStorage getCosmeticStorage() {
        return this.cosmeticStorage;
    }

    public final CosmeticType getCosmeticType() {
        return this.cosmeticType;
    }

    public final void give() {
        CosmeticsAPI api = this.cosmeticStorage.getPlugin().getApi();
        if (api.hasCosmetics(this.player, this.cosmeticType)) {
            for(Cosmetic cosmetic : api.getCosmetics(this.player, this.cosmeticType)) {
                cosmetic.remove();
            }
            this.give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            if(this.giveInternal()) api.addCosmetics(this.player, this);
        }
    }

    public final void remove() {
        CosmeticsAPI api = this.cosmeticStorage.getPlugin().getApi();
        if (api.hasCosmetics(this.player, this.cosmeticType)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            api.removeCosmetics(this.player, this.cosmeticType);
            this.removeInternal();
        }
    }

    protected abstract boolean giveInternal();

    protected abstract void removeInternal();
}
