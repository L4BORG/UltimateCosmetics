package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;

import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class GadgetsModule extends PluginModule<Main, Gadgets> {
    public GadgetsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Gadgets(this);
        new GadgetsListener(this);
        if(ReflectionAPI.verBiggerThan(1, 9)) new GadgetsListener1_9(this);
    }

    @Override
    public void onDisable() {
        // NOP
    }
}
