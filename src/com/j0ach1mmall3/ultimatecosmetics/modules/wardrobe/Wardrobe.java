package com.j0ach1mmall3.ultimatecosmetics.modules.wardrobe;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class Wardrobe extends CosmeticConfig {
    private final boolean checkOnBody;
    private final boolean keepOnDeath;

    public Wardrobe(WardrobeModule module) {
        super("wardrobe.yml", module.getParent(), "Outfits");
        this.checkOnBody = this.config.getBoolean("CheckOnBody");
        this.keepOnDeath = this.config.getBoolean("KeepOnDeath");
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new Outfit(this, player, (OutfitStorage) cosmeticStorage);
    }

    @Override
    protected OutfitStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new OutfitStorage(
                (Main) this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.customConfig.getGuiItemNew(this.config, path),
                this.customConfig.getItemNew(this.config, path + "Leggings"),
                this.customConfig.getItemNew(this.config, path + "Boots")
        );
    }

    public boolean isCheckOnBody() {
        return this.checkOnBody;
    }

    public boolean isKeepOnDeath() {
        return this.keepOnDeath;
    }
}
