package com.j0ach1mmall3.ultimatecosmetics.modules.hearts;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class Hearts extends CosmeticConfig<HeartStorage> {
    private final boolean keepOnDeath;
    private final boolean scaleDamage;

    public Hearts(HeartsModule module) {
        super("hearts.yml", module.getParent(), "Hearts");
        this.keepOnDeath = this.config.getBoolean("KeepOnDeath");
        this.scaleDamage = this.config.getBoolean("ScaleDamage");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Heart.class;
    }

    @Override
    public Cosmetic getCosmetic(HeartStorage cosmeticStorage, Player player) {
        return new Heart(this, player, (HeartStorage) cosmeticStorage);
    }

    @Override
    protected CosmeticStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new HeartStorage(
                this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getGuiItemNew(this.config, path),
                this.config.getInt(path + "Rows"),
                HeartStorage.Color.valueOf(this.config.getString(path + "Color").toUpperCase()),
                HeartStorage.Effect.valueOf(this.config.getString(path + "Effect").toUpperCase())
        );
    }

    public boolean isKeepOnDeath() {
        return this.keepOnDeath;
    }

    public boolean isScaleDamage() {
        return this.scaleDamage;
    }
}