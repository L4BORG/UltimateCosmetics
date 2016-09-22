package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class MorphsModule extends PluginModule<Main, Morphs> {
    public MorphsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Morphs(this);
        new MorphsListener(this);
        if(ReflectionAPI.verBiggerThan(1, 9)) new MorphsListener1_9(this);
    }

    @Override
    public void onDisable() {
        // NOP
    }
}
