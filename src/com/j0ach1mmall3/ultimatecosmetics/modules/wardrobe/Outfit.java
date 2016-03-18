package com.j0ach1mmall3.ultimatecosmetics.modules.wardrobe;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Outfit extends Cosmetic {
    public Outfit(CosmeticConfig cosmeticConfig, Player player, OutfitStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.OUTFIT);
    }

    @Override
    protected boolean giveInternal() {
        if(((Wardrobe) this.cosmeticConfig).isCheckOnBody()) {
            if (this.player.getInventory().getChestplate() != null) {
                this.cosmeticStorage.getPlugin().informPlayerNoPermission(this.player, Placeholders.parse(this.cosmeticStorage.getPlugin().getLang().getAlreadyOnBody(), this.player).replace("%itemname%", this.player.getInventory().getChestplate().getType().toString().toLowerCase().replace("_", " ")).replace("%armorslot%", "Chestplate"));
                return false;
            }
            if (this.player.getInventory().getLeggings() != null) {
                this.cosmeticStorage.getPlugin().informPlayerNoPermission(this.player, Placeholders.parse(this.cosmeticStorage.getPlugin().getLang().getAlreadyOnBody(), this.player).replace("%itemname%", this.player.getInventory().getLeggings().getType().toString().toLowerCase().replace("_", " ")).replace("%armorslot%", "Leggings"));
                return false;
            }
            if (this.player.getInventory().getBoots() != null) {
                this.cosmeticStorage.getPlugin().informPlayerNoPermission(this.player, Placeholders.parse(this.cosmeticStorage.getPlugin().getLang().getAlreadyOnBody(), this.player).replace("%itemname%", this.player.getInventory().getBoots().getType().toString().toLowerCase().replace("_", " ")).replace("%armorslot%", "Boots"));
                return false;
            }
        }

        this.player.getInventory().setChestplate(this.cosmeticStorage.getGuiItem().getItem());
        this.player.getInventory().setLeggings(((OutfitStorage) this.cosmeticStorage).getLeggingsItem());
        this.player.getInventory().setBoots(((OutfitStorage) this.cosmeticStorage).getBootsItem());
        return true;
    }

    @Override
    protected void removeInternal() {
        this.player.getInventory().setChestplate(null);
        this.player.getInventory().setLeggings(null);
        this.player.getInventory().setBoots(null);
    }
}
