package com.j0ach1mmall3.ultimatecosmetics.modules.hats;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class Hats extends CosmeticConfig<CosmeticStorage> {
    private final boolean checkOnHead;
    private final boolean keepOnDeath;

    public Hats(HatsModule module) {
        super("hats.yml", module.getParent(), "Hats");
        this.checkOnHead = this.config.getBoolean("CheckOnHead");
        this.keepOnDeath = this.config.getBoolean("KeepOnDeath");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Hat.class;
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new Hat(this, player, cosmeticStorage);
    }

    public boolean isCheckOnHead() {
        return this.checkOnHead;
    }

    public boolean isKeepOnDeath() {
        return this.keepOnDeath;
    }
}
