package com.j0ach1mmall3.ultimatecosmetics.modules.wardrobe;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class Wardrobe extends CosmeticConfig<OutfitStorage> {
    private final boolean checkOnBody;
    private final boolean keepOnDeath;

    public Wardrobe(WardrobeModule module) {
        super("wardrobe.yml", module.getParent(), "Outfits");
        this.checkOnBody = this.config.getBoolean("CheckOnBody");
        this.keepOnDeath = this.config.getBoolean("KeepOnDeath");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Outfit.class;
    }

    @Override
    public Cosmetic getCosmetic(OutfitStorage cosmeticStorage, Player player) {
        return new Outfit(player, cosmeticStorage);
    }

    @Override
    protected OutfitStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new OutfitStorage(
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getJLibItem(this.config, path),
                this.storage.getItemNew(this.config, path + "Leggings"),
                this.storage.getItemNew(this.config, path + "Boots")
        );
    }

    public boolean isCheckOnBody() {
        return this.checkOnBody;
    }

    public boolean isKeepOnDeath() {
        return this.keepOnDeath;
    }
}
