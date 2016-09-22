package com.j0ach1mmall3.ultimatecosmetics.modules.fireworks;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Firework extends Cosmetic<CosmeticStorage> {
    public Firework(Player player, CosmeticStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    public boolean giveInternal(Main plugin) {
        org.bukkit.entity.Firework fw = this.player.getWorld().spawn(this.player.getLocation(), org.bukkit.entity.Firework.class);
        fw.setFireworkMeta((FireworkMeta) this.cosmeticStorage.getjLibItem().getItemStack().getItemMeta());
        return false;
    }

    @Override
    public void removeInternal(Main plugin) {
        // NOP
    }
}
