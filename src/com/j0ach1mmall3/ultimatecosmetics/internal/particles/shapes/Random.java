package com.j0ach1mmall3.ultimatecosmetics.internal.particles.shapes;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Particle;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleStorage;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 20/12/15
 */
public final class Random extends BukkitRunnable {
    private final Main plugin;

    public Random(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for (Particle particle : this.plugin.getApi().getParticles()) {
            ParticleStorage particleStorage = particle.getParticleStorage();
            if(particleStorage.getShape() == ParticleShape.RANDOM) {
                Location l = particle.getPlayer().getLocation();
                l.setY(l.getY() + 0.5);
                l.getWorld().spigot().playEffect(l, Effect.valueOf(particleStorage.getParticle()), particleStorage.getId(), particleStorage.getData(), 0.5f, 0.5f, 0.5f, particleStorage.getSpeed(), 10, this.plugin.getParticles().getViewDistance());
            }
        }
    }
}
