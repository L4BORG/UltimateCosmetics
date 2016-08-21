package com.j0ach1mmall3.ultimatecosmetics.modules.particles;

import com.j0ach1mmall3.jlib.Main;
import com.j0ach1mmall3.jlib.effectsapi.*;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 4/03/2016
 */
public final class ParticlesRunnable extends BukkitRunnable {
    private final ParticlesModule module;

    public ParticlesRunnable(ParticlesModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Particle cosmetic : this.module.getParent().getApi().getAllCosmetics(Particle.class)) {
            Player p = cosmetic.getPlayer();
            long lastWalked = JavaPlugin.getPlugin(Main.class).getPlayerListener().getLastWalked(p);
            ParticleCosmeticStorage pcs = cosmetic.getCosmeticStorage();
            if(pcs.getShape() != ParticleCosmeticStorage.Shape.RANDOM && pcs.getShape() != ParticleCosmeticStorage.Shape.TRAIL && lastWalked != 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWalked) < this.module.getConfig().getGiveDelay()) return;
            switch (pcs.getShape()) {
                case CROSS:
                    new CrossEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 1, 32, 2).play(this.module.getParent());
                    break;
                case DOUBLECROSS:
                    new DoubleCrossEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 1, 32, 2).play(this.module.getParent());
                    break;
                case DOME:
                    new DomeEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 2, 256).play(this.module.getParent());
                    break;
                case SPHERE:
                    new SphereEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 2, 256).play(this.module.getParent());
                    break;
                case CONE:
                    new ConeEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 1, 32, 2.5).play(this.module.getParent());
                    break;
                case CYLINDER:
                    new CylinderEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 1, 32, 2).play(this.module.getParent());
                    break;
                case HELIX:
                    new HelixEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 1, 32, 2).play(this.module.getParent());
                    break;
                case DOUBLEHELIX:
                    new DoubleHelixEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), this.module.getConfig().getViewDistance(), 1, 32, 2).play(this.module.getParent());
                    break;
                case RANDOM:
                    Methods.broadcastSafeParticle(p.getLocation().add(0, 0.75, 0), pcs.getParticle(), pcs.getId(), pcs.getData(), 0.5F, 0.5F, 0.5F, pcs.getSpeed(), 16, this.module.getConfig().getViewDistance());
                    break;
                case TRAIL:
                    Methods.broadcastSafeParticle(p.getLocation().add(0, 0.75, 0), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), 1, this.module.getConfig().getViewDistance());
                    break;
                default:
                    // NOP
            }
        }
    }
}
