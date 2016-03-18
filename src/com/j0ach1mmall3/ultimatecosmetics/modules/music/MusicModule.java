package com.j0ach1mmall3.ultimatecosmetics.modules.music;

import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class MusicModule extends PluginModule {
    public MusicModule(ModularizedPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        String songsPath = this.parent.getDataFolder().getPath() + "/songs";
        File dir = new File(songsPath);
        if (!dir.exists()) dir.mkdir();
        File song = new File(songsPath, "ExampleMusic.nbs");
        if (!song.exists()) {
            this.parent.saveResource("ExampleMusic.nbs", false);
            try {
                Files.move(new File(this.parent.getDataFolder(), "ExampleMusic.nbs").toPath(), song.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = new Musics(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public ConfigLoader getConfig() {
        return this.config;
    }
}
