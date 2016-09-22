package com.j0ach1mmall3.ultimatecosmetics.modules.particles;

import com.j0ach1mmall3.jlib.effectsapi.*;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class ParticlesModule extends PluginModule<Main, Particles> {
    private int task;

    public ParticlesModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Particles(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (Particle cosmetic : ParticlesModule.this.parent.getApi().getAllCosmetics(Particle.class)) {
                    Player p = cosmetic.getPlayer();
                    long lastWalked = JavaPlugin.getPlugin(com.j0ach1mmall3.jlib.Main.class).getPlayerListener().getLastWalked(p);
                    ParticleCosmeticStorage pcs = cosmetic.getCosmeticStorage();
                    if(pcs.getShape() != ParticleCosmeticStorage.Shape.RANDOM && pcs.getShape() != ParticleCosmeticStorage.Shape.TRAIL && lastWalked != 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWalked) < 0.1) return;
                    int viewDistance = ParticlesModule.this.config.getViewDistance();
                    switch (pcs.getShape()) {
                        case CROSS:
                            new CrossEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), viewDistance, 1, 32, 2).play(ParticlesModule.this.parent);
                            break;
                        case DOUBLECROSS:
                            new DoubleCrossEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), viewDistance, 1, 32, 2).play(ParticlesModule.this.parent);
                            break;
                        case DOME:
                            new DomeEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), viewDistance, 2, 256).play(ParticlesModule.this.parent);
                            break;
                        case SPHERE:
                            new SphereEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), viewDistance, 2, 256).play(ParticlesModule.this.parent);
                            break;
                        case CONE:
                            new ConeEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), viewDistance, 1, 32, 2.5).play(ParticlesModule.this.parent);
                            break;
                        case CYLINDER:
                            new CylinderEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), viewDistance, 1, 32, 2).play(ParticlesModule.this.parent);
                            break;
                        case HELIX:
                            new HelixEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), viewDistance, 1, 32, 2).play(ParticlesModule.this.parent);
                            break;
                        case DOUBLEHELIX:
                            new DoubleHelixEffect(p.getLocation(), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), viewDistance, 1, 32, 2).play(ParticlesModule.this.parent);
                            break;
                        case RANDOM:
                            Methods.broadcastSafeParticle(p.getLocation().add(0, 0.75, 0), pcs.getParticle(), pcs.getId(), pcs.getData(), 0.5F, 0.5F, 0.5F, pcs.getSpeed(), 16, viewDistance);
                            break;
                        case TRAIL:
                            Methods.broadcastSafeParticle(p.getLocation().add(0, 0.75, 0), pcs.getParticle(), pcs.getId(), pcs.getData(), pcs.getSpeed(), 1, viewDistance);
                            break;
                        default:
                            // NOP
                    }
                }
            }
        }, this.config.getUpdateInterval(), this.config.getUpdateInterval());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
