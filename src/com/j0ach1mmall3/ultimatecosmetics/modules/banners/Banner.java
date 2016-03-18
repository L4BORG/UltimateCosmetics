package com.j0ach1mmall3.ultimatecosmetics.modules.banners;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Banner extends Cosmetic {
    public Banner(CosmeticConfig cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.BANNER);
    }

    @Override
    protected boolean giveInternal() {
        Main plugin = this.cosmeticStorage.getPlugin();
        if (this.player.getInventory().getHelmet() != null && ((Banners) this.cosmeticConfig).isCheckOnHead()) {
            plugin.informPlayerNoPermission(this.player, Placeholders.parse(plugin.getLang().getAlreadyOnHead(), this.player).replace("%itemname%", this.player.getInventory().getHelmet().getType().toString().toLowerCase().replace("_", " ")));
            return false;
        } else {
            this.player.getInventory().setHelmet(this.cosmeticStorage.getGuiItem().getItem());
            return true;
        }
    }

    @Override
    protected void removeInternal() {
        this.player.getInventory().setHelmet(null);
    }
}
