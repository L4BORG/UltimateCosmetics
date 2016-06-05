package com.j0ach1mmall3.ultimatecosmetics.modules.trails;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class Trails extends CosmeticConfig<CosmeticStorage> {
    private final int dropInterval;
    private final int removeDelay;

    public Trails(TrailsModule module) {
        super("trails.yml", module.getParent(), "Trails");
        this.dropInterval = this.config.getInt("DropInterval");
        this.removeDelay = this.config.getInt("RemoveDelay");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Trail.class;
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new Trail(this, player, cosmeticStorage);
    }

    public int getDropInterval() {
        return this.dropInterval;
    }

    public int getRemoveDelay() {
        return this.removeDelay;
    }
}
