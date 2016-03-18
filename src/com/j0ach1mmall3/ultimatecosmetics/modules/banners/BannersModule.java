package com.j0ach1mmall3.ultimatecosmetics.modules.banners;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class BannersModule extends PluginModule {
    public BannersModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Banners(this);
        new BannersListener(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public ConfigLoader getConfig() {
        return this.config;
    }
}
