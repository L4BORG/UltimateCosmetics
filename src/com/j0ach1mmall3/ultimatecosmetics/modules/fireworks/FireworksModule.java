package com.j0ach1mmall3.ultimatecosmetics.modules.fireworks;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class FireworksModule extends PluginModule<Main, Fireworks> {
    public FireworksModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Fireworks(this);
    }

    @Override
    public void onDisable() {
        // NOP
    }
}
