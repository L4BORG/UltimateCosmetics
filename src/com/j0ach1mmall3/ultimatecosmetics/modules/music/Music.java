package com.j0ach1mmall3.ultimatecosmetics.modules.music;

import com.j0ach1mmall3.jlib.nbsapi.songplayer.DynamicSongPlayer;
import com.j0ach1mmall3.jlib.nbsapi.songplayer.SongPlayer;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class Music extends Cosmetic<Musics, MusicStorage> {
    private SongPlayer songPlayer;

    public Music(Musics cosmeticConfig, Player player, MusicStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal() {
        this.songPlayer = new DynamicSongPlayer(this.cosmeticStorage.getSong(), this.cosmeticStorage.isRepeat(), true);
        this.songPlayer.addPlayer(this.player);
        this.songPlayer.start();
        return true;
    }

    @Override
    protected void removeInternal() {
        this.songPlayer.stop();
    }
}
