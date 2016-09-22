package com.j0ach1mmall3.ultimatecosmetics.modules.trails;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Trail extends Cosmetic<CosmeticStorage> {
    public Trail(Player player, CosmeticStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(Main plugin) {
        return true;
    }

    @Override
    protected void removeInternal(Main plugin) {
        // NOP
    }
}
