package com.j0ach1mmall3.ultimatecosmetics.modules.mounts;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Reflection;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 4/03/2016
 */
public final class MountsRunnable extends BukkitRunnable {
    private final MountsModule module;

    public MountsRunnable(MountsModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(CosmeticType.MOUNT)) {
            Entity ent = ((Mount) cosmetic).getEntity();
            Player p = cosmetic.getPlayer();
            if (ent.getPassenger() == null) {
                cosmetic.remove();
                return;
            }
            Vector v = p.getLocation().getDirection().setY(0).normalize().multiply(4);
            Location loc = p.getLocation().add(v);
            Reflection.setNavigation(ent, loc, ((Mounts) this.module.getConfig()).getMountSpeed());
        }
    }
}
