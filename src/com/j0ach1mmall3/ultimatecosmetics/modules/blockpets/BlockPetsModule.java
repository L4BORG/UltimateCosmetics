package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BlockPetsModule extends PluginModule<Main, BlockPets> {
    private BukkitTask task;

    public BlockPetsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new BlockPets(this);
        this.task = new BlockPetsRunnable(this).runTaskTimer(this.getParent(), this.config.getTeleportInterval(), this.config.getTeleportInterval());
        new BlockPetsListener(this);
    }

    @Override
    public void onDisable() {
        this.task.cancel();
    }
}
