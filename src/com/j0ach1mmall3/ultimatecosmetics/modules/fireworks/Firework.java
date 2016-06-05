package com.j0ach1mmall3.ultimatecosmetics.modules.fireworks;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Firework extends Cosmetic<Fireworks, CosmeticStorage> {
    public Firework(Fireworks cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage);
    }

    @Override
    public boolean giveInternal() {
        org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.FIREWORK);
        fw.setFireworkMeta((FireworkMeta) this.cosmeticStorage.getGuiItem().getItem().getItemMeta());
        return false;
    }

    @Override
    public void removeInternal() {
        // NOP
    }
}
