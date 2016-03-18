package com.j0ach1mmall3.ultimatecosmetics.modules.hats;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class HatsModule extends PluginModule {
    public HatsModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Hats(this);
        new HatsListener(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public ConfigLoader getConfig() {
        return this.config;
    }
}
