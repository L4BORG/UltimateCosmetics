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
public final class Cross extends BukkitRunnable {
    private final Main plugin;

    public Cross(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for (Particle particle : this.plugin.getApi().getParticles()) {
            final ParticleStorage particleStorage = particle.getParticleStorage();
            if (particleStorage.getShape() == ParticleShape.CROSS) {
                final Location l = particle.getPlayer().getLocation();
                new BukkitRunnable() {
                    double y = 0;

                    @Override
                    public void run() {
                        this.y += Math.PI/32;
                        double x = Math.cos(this.y);
                        double z = Math.sin(this.y);
                        l.getWorld().spigot().playEffect(new Location(l.getWorld(), l.getX() + x, l.getY() + Math.sin(this.y) + 1, l.getZ() + z), Effect.valueOf(particleStorage.getParticle()), particleStorage.getId(), particleStorage.getData(), 0, 0, 0, particleStorage.getSpeed(), 1, Cross.this.plugin.getParticles().getViewDistance());
                        l.getWorld().spigot().playEffect(new Location(l.getWorld(), l.getX() + x, l.getY() + Math.cos(this.y) + 1, l.getZ() + z), Effect.valueOf(particleStorage.getParticle()), particleStorage.getId(), particleStorage.getData(), 0, 0, 0, particleStorage.getSpeed(), 1, Cross.this.plugin.getParticles().getViewDistance());

                        if (this.y > 6) this.cancel();
                    }

                }.runTaskTimer(this.plugin, 0, 1);
            }
        }
    }
}
