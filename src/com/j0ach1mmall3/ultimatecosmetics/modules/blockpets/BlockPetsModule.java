package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BlockPetsModule extends PluginModule {
    private BukkitTask task;

    public BlockPetsModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new BlockPets(this);
        this.task = new BlockPetsRunnable(this).runTaskTimer(this.getParent(), ((BlockPets) this.config).getTeleportInterval(), ((BlockPets) this.config).getTeleportInterval());
        new BlockPetsListener(this);
    }

    @Override
    public void onDisable() {
        this.task.cancel();
    }

    @Override
    public ConfigLoader getConfig() {
        return this.config;
    }
}
