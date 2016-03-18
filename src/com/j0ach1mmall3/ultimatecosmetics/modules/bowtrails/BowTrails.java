package com.j0ach1mmall3.ultimatecosmetics.modules.bowtrails;

import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 31/10/2015
 */
public final class BowTrails extends CosmeticConfig {
    private final int updateInterval;
    private final int viewDistance;

    public BowTrails(BowTrailsModule module) {
        super("bowtrails.yml", module.getParent(), "BowTrails");
        this.updateInterval = this.config.getInt("UpdateInterval");
        this.viewDistance = this.config.getInt("ViewDistance");
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new BowTrail(this, player, (ParticleCosmeticStorage) cosmeticStorage);
    }

    @Override
    protected CosmeticStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new ParticleCosmeticStorage(
                (Main) this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.customConfig.getGuiItemNew(this.config, path),
                Effect.valueOf(this.config.getString(path + "Effect")),
                this.config.getInt(path + "ID"),
                this.config.getInt(path + "Data"),
                Parsing.parseFloat(this.config.getString(path + "Speed")),
                ParticleCosmeticStorage.Shape.TRAIL
        );
    }

    public int getUpdateInterval() {
        return this.updateInterval;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }
}
