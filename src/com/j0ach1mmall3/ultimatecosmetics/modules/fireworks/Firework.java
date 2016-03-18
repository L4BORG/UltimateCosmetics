package com.j0ach1mmall3.ultimatecosmetics.modules.fireworks;

import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Firework extends Cosmetic {
    public Firework(CosmeticConfig cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.FIREWORK);
    }

    @Override
    public boolean giveInternal() {
        org.bukkit.entity.Firework fw = this.player.getWorld().spawn(this.player.getLocation(), org.bukkit.entity.Firework.class);
        fw.setFireworkMeta((FireworkMeta) this.cosmeticStorage.getGuiItem().getItem().getItemMeta());
        return false;
    }

    @Override
    public void removeInternal() {
        // NOP
    }
}
