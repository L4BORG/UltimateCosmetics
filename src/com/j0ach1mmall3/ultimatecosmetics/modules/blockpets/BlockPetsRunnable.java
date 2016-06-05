package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

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
        for (BlockPet cosmetic : this.module.getParent().getApi().getAllCosmetics(BlockPet.class)) {
            Ocelot ocelot = cosmetic.getOcelot();
            Player p = cosmetic.getPlayer();
            if (!ocelot.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || ocelot.getLocation().distance(p.getLocation()) >= this.module.getConfig().getTeleportDistance()) cosmetic.give();
        }
    }
}
