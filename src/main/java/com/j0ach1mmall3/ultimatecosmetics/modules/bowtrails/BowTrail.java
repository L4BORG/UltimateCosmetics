package com.j0ach1mmall3.ultimatecosmetics.modules.bowtrails;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class BowTrail extends Cosmetic<ParticleCosmeticStorage> {
    public BowTrail(Player player, ParticleCosmeticStorage cosmeticStorage) {
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
