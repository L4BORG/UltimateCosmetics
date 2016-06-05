package com.j0ach1mmall3.ultimatecosmetics.modules.wardrobe;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class WardrobeModule extends PluginModule<Main, Wardrobe> {
    public WardrobeModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Wardrobe(this);
        new WardrobeListener(this);
    }

    @Override
    public void onDisable() {
        // NOP
    }
}
