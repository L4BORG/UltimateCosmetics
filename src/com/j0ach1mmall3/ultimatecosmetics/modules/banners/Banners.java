package com.j0ach1mmall3.ultimatecosmetics.modules.banners;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class Banners extends CosmeticConfig {
    private final boolean checkOnHead;
    private final boolean keepOnDeath;

    public Banners(BannersModule module) {
        super("banners.yml", module.getParent(), "Banners");
        this.checkOnHead = this.config.getBoolean("CheckOnHead");
        this.keepOnDeath = this.config.getBoolean("KeepOnDeath");
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new Banner(this, player, cosmeticStorage);
    }

    public boolean isCheckOnHead() {
        return this.checkOnHead;
    }

    public boolean isKeepOnDeath() {
        return this.keepOnDeath;
    }
}
