package com.j0ach1mmall3.ultimatecosmetics.modules.hearts;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 4/03/2016
 */
public final class HeartsRunnable extends BukkitRunnable {
    private final HeartsModule module;

    public HeartsRunnable(HeartsModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(CosmeticType.HEART)) {
            Player p = cosmetic.getPlayer();
            Heart heart = (Heart) cosmetic;
            if (heart.getColorEffect() != null && !p.hasPotionEffect(heart.getColorEffect().getType()) || heart.getEffectsEffect() != null && !p.hasPotionEffect(heart.getEffectsEffect().getType())) heart.regive();
        }
    }
}
