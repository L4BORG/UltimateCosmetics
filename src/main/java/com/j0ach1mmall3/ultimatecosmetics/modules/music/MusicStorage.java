package com.j0ach1mmall3.ultimatecosmetics.modules.music;


import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.jlib.nbsapi.Song;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class MusicStorage extends CosmeticStorage {
    private final Song song;
    private final boolean repeat;

    public MusicStorage(String identifier, String permission, JLibItem jlibItem, Song song, boolean repeat) {
        super(identifier, permission, jlibItem);
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
