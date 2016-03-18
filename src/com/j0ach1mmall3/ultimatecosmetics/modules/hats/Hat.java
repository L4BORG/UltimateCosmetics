package com.j0ach1mmall3.ultimatecosmetics.modules.hats;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Hat extends Cosmetic {
    public Hat(CosmeticConfig cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.HAT);
    }

    @Override
    protected boolean giveInternal() {
        if (this.player.getInventory().getHelmet() != null && ((Hats) this.cosmeticConfig).isCheckOnHead()) {
            this.player.sendMessage(Placeholders.parse(this.cosmeticStorage.getPlugin().getLang().getAlreadyOnHead(), this.player).replace("%itemname%", this.player.getInventory().getHelmet().getType().toString().toLowerCase().replace("_", " ")));
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
