package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public final class BlockPetsRunnable extends BukkitRunnable {
    private final BlockPetsModule module;

    public BlockPetsRunnable(BlockPetsModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(CosmeticType.BLOCKPET)) {
            Ocelot ocelot = ((BlockPet) cosmetic).getOcelot();
            Player p = cosmetic.getPlayer();
            if (!ocelot.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || ocelot.getLocation().distance(p.getLocation()) >= ((BlockPets) this.module.getConfig()).getTeleportDistance()) cosmetic.give();
        }
    }
}
