package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class BannerCapes extends CosmeticConfig {
    private final int angle;
    private final int giveDelay;

    public BannerCapes(BannerCapesModule module) {
        super("bannercapes.yml", module.getParent(), "BannerCapes");
        this.angle = this.config.getInt("Angle");
        this.giveDelay = this.config.getInt("GiveDelay");
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new BannerCape(this, player, cosmeticStorage);
    }

    public int getAngle() {
        return this.angle;
    }

    public int getGiveDelay() {
        return this.giveDelay;
    }
}
