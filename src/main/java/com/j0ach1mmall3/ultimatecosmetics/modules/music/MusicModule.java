package com.j0ach1mmall3.ultimatecosmetics.modules.music;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class MusicModule extends PluginModule<Main, Musics> {
    public MusicModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        String songsPath = this.parent.getDataFolder().getPath() + "/songs";
        File dir = new File(songsPath);
        if (!dir.exists()) dir.mkdir();
        File song = new File(songsPath, "ExampleMusic.nbs");
        if (!song.exists()) {
            try {
                Files.copy(this.parent.getResource("ExampleMusic.nbs"), song.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = new Musics(this);
    }

    @Override
    public void onDisable() {
        // NOP
    }
}
