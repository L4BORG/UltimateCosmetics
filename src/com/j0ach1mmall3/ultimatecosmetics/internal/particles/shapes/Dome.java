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
public final class Dome extends BukkitRunnable {
    private final Main plugin;

    public Dome(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for (Particle particle : this.plugin.getApi().getParticles()) {
            final ParticleStorage particleStorage = particle.getParticleStorage();
            if(particleStorage.getShape() == ParticleShape.DOME) {
                final Location l = particle.getPlayer().getLocation();
                new BukkitRunnable() {
                    double t = Math.PI/4;

                    @Override
                    public void run() {
                        this.t += 0.1 * Math.PI;
                        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI/32){
                            double x = this.t * Math.cos(theta) / 2;
                            double y = 2 * Math.exp(-0.1 * this.t) * Math.sin(this.t) + 1;
                            double z = this.t * Math.sin(theta) / 2;
                            l.getWorld().spigot().playEffect(new Location(l.getWorld(), l.getX() + x, l.getY() + y, l.getZ() + z), Effect.valueOf(particleStorage.getParticle()), particleStorage.getId(), particleStorage.getData(), 0, 0, 0, particleStorage.getSpeed(), 1, Dome.this.plugin.getParticles().getViewDistance());
                        }
                        if (this.t > 3.5) this.cancel();
                    }

                }.runTaskTimer(this.plugin, 0, 1);
            }
        }
    }
}
