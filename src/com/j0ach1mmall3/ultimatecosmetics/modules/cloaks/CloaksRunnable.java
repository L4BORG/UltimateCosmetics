package com.j0ach1mmall3.ultimatecosmetics.modules.cloaks;

import com.j0ach1mmall3.jlib.effectsapi.HaloEffect;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 4/03/2016
 */
public final class CloaksRunnable extends BukkitRunnable {
    private final CloaksModule module;

    public CloaksRunnable(CloaksModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Cloak cosmetic : this.module.getParent().getApi().getAllCosmetics(Cloak.class)) {
            Player p = cosmetic.getPlayer();
            long lastWalked = ((com.j0ach1mmall3.jlib.Main) Bukkit.getPluginManager().getPlugin("JLib")).getJoinListener().getLastWalked(p);
            if (lastWalked != 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWalked) < this.module.getConfig().getGiveDelay())
                return;
            ParticleCosmeticStorage pcs = cosmetic.getCosmeticStorage();
            switch (pcs.getShape()) {
                case HALO:
                    new HaloEffect(p.getLocation().add(0, 2, 0), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 0.5, 16).play(this.module.getParent());
                    break;
            }
        }
    }
}
