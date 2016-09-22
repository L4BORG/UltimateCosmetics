package com.j0ach1mmall3.ultimatecosmetics.modules.hats;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Hat extends Cosmetic<CosmeticStorage> {
    public Hat(Player player, CosmeticStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(Main plugin) {
        if (this.player.getInventory().getHelmet() != null && plugin.getConfig(Hats.class).isCheckOnHead()) {
            this.player.sendMessage(Placeholders.parse(plugin.getLang().getAlreadyOnHead(), this.player).replace("%itemname%", this.player.getInventory().getHelmet().getType().toString().toLowerCase().replace("_", " ")));
            return false;
        } else {
            this.player.getInventory().setHelmet(this.cosmeticStorage.getjLibItem().getItemStack());
            return true;
        }
    }

    @Override
    protected void removeInternal(Main plugin) {
        this.player.getInventory().setHelmet(null);
    }
}
