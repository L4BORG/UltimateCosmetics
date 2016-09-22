package com.j0ach1mmall3.ultimatecosmetics.modules.auras;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 20/08/2015
 */
public final class Auras extends CosmeticConfig<AuraStorage> {
    private final int updateInterval;

    public Auras(AurasModule module) {
        super("auras.yml", module.getParent(), "Auras");
        this.updateInterval = this.config.getInt("UpdateInterval");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Aura.class;
    }

    @Override
    public Cosmetic getCosmetic(AuraStorage cosmeticStorage, Player player) {
        return new Aura(player, cosmeticStorage);
    }

    @Override
    protected AuraStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new AuraStorage(
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getJLibItem(this.config, path),
                AuraStorage.Color.valueOf(this.config.getString(path + "Color").toUpperCase())
        );
    }

    public int getUpdateInterval() {
        return this.updateInterval;
    }
}
