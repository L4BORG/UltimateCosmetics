package com.j0ach1mmall3.ultimatecosmetics.modules.bowtrails;

import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public final class BowTrailsRunnable extends BukkitRunnable {
    private final BowTrailsModule module;

    public BowTrailsRunnable(BowTrailsModule module) {
        this.module = module;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
        for (Map.Entry<Arrow, Cosmetic> entry : this.module.getArrowsMap().entrySet()) {
            Location l = entry.getKey().getLocation();
            ParticleCosmeticStorage particleCosmeticStorage = (ParticleCosmeticStorage) entry.getValue().getCosmeticStorage();
            Methods.broadcastSafeParticle(l, particleCosmeticStorage.getParticle(), particleCosmeticStorage.getId(), particleCosmeticStorage.getData(), particleCosmeticStorage.getSpeed(), 1, this.module.getConfig().getViewDistance());
        }
    }
}
