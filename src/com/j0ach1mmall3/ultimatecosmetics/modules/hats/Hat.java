package com.j0ach1mmall3.ultimatecosmetics.modules.hats;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Hat extends Cosmetic<Hats, CosmeticStorage> {
    public Hat(Hats cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal() {
        if (this.player.getInventory().getHelmet() != null && this.cosmeticConfig.isCheckOnHead()) {
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
