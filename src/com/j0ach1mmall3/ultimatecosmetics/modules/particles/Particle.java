package com.j0ach1mmall3.ultimatecosmetics.modules.particles;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Particle extends Cosmetic {
    public Particle(CosmeticConfig cosmeticConfig, Player player, ParticleCosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.PARTICLE);
    }

    @Override
    protected boolean giveInternal() {
        return true;
    }

    @Override
    protected void removeInternal() {
        // NOP
    }
}
