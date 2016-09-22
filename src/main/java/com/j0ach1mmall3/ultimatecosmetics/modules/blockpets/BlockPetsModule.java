package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BlockPetsModule extends PluginModule<Main, BlockPets> {
    private int task;

    public BlockPetsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new BlockPets(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                for (BlockPet cosmetic : BlockPetsModule.this.parent.getApi().getAllCosmetics(BlockPet.class)) {
                    Creature creature = cosmetic.getCreature();
                    Player p = cosmetic.getPlayer();
                    if (!creature.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || creature.getLocation().distance(p.getLocation()) >= BlockPetsModule.this.config.getTeleportDistance()) cosmetic.give(BlockPetsModule.this.parent);
                }
            }
        }, this.config.getTeleportInterval(), this.config.getTeleportInterval());
        new BlockPetsListener(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
