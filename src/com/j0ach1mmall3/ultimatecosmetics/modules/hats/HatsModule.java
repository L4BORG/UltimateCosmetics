package com.j0ach1mmall3.ultimatecosmetics.modules.hats;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class HatsModule extends PluginModule<Main, Hats> {
    public HatsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Hats(this);
        new HatsListener(this);
    }

    @Override
    public void onDisable() {
        // NOP
    }
}
