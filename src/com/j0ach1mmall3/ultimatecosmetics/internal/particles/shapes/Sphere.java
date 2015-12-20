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
public final class Sphere extends BukkitRunnable {
    private final Main plugin;

    public Sphere(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for (Particle particle : this.plugin.getApi().getParticles()) {
            ParticleStorage particleStorage = particle.getParticleStorage();
            if(particleStorage.getShape() == ParticleShape.SPHERE) {
                Location l = particle.getPlayer().getLocation();
                new BukkitRunnable() {
                    double phi = 0;

                    @Override
                    public void run() {
                        this.phi += Math.PI/10;
                        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI/32){
                            double x = Math.cos(theta) * Math.sin(this.phi);
                            double y = Math.cos(this.phi) + 1;
                            double z = Math.sin(theta) * Math.sin(this.phi);
                            l.getWorld().spigot().playEffect(new Location(l.getWorld(), l.getX() + x, l.getY() + y, l.getZ() + z), Effect.valueOf(particleStorage.getParticle()), particleStorage.getId(), particleStorage.getData(), 0, 0, 0, particleStorage.getSpeed(), 1, Sphere.this.plugin.getParticles().getViewDistance());
                        }
                        if (this.phi > Math.PI) this.cancel();
                    }

                }.runTaskTimer(this.plugin, 0, 1);
            }
        }
    }
}
