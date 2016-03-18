package com.j0ach1mmall3.ultimatecosmetics.modules.music;


import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.jlib.nbsapi.Song;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class MusicStorage extends CosmeticStorage {
    private final Song song;
    private final boolean repeat;

    public MusicStorage(Main plugin, String identifier, String permission, GuiItem guiItem, Song song, boolean repeat) {
        super(plugin, identifier, permission, guiItem);
        this.song = song;
        this.repeat = repeat;
    }

    public Song getSong() {
        return this.song;
    }

    public boolean isRepeat() {
        return this.repeat;
    }
}
