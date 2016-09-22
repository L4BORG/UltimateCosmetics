package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class BannerCapes extends CosmeticConfig<CosmeticStorage> {
    private final int angle;

    public BannerCapes(BannerCapesModule module) {
        super("bannercapes.yml", module.getParent(), "BannerCapes");
        this.angle = this.config.getInt("Angle");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return BannerCape.class;
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new BannerCape(player, cosmeticStorage);
    }

    public int getAngle() {
        return this.angle;
    }
}
