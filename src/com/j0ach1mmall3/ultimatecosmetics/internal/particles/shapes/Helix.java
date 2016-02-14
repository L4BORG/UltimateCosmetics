package com.j0ach1mmall3.ultimatecosmetics.internal.particles.shapes;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Particle;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleStorage;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 20/12/15
 */
public final class Helix extends BukkitRunnable {
    private final Main plugin;

    public Helix(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for (Particle particle : this.plugin.getApi().getParticles()) {
            final ParticleStorage particleStorage = particle.getParticleStorage();
            if(particleStorage.getShape() == ParticleShape.HELIX) {
                Player p = particle.getPlayer();
                long lastWalked = ((com.j0ach1mmall3.jlib.Main) Bukkit.getPluginManager().getPlugin("JLib")).getJoinListener().getLastWalked(p);
                if(lastWalked != 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastWalked) < 3) return;
                final Location l = p.getLocation();
                new BukkitRunnable() {
                    double y = 0;

                    @Override
                    public void run() {
                        this.y += Math.PI/16;
                        double x = Math.cos(this.y);
                        double z = Math.sin(this.y);
                        l.getWorld().spigot().playEffect(new Location(l.getWorld(), l.getX() + x, l.getY() + this.y/6, l.getZ() + z), Effect.valueOf(particleStorage.getParticle()), particleStorage.getId(), particleStorage.getData(), 0, 0, 0, particleStorage.getSpeed(), 1, Helix.this.plugin.getParticles().getViewDistance());

                        if (this.y > 12) this.cancel();
                    }

                }.runTaskTimerAsynchronously(this.plugin, 0, 1);
            }
        }
    }
}