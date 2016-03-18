package com.j0ach1mmall3.ultimatecosmetics.modules.music;

import com.j0ach1mmall3.jlib.nbsapi.songplayer.DynamicSongPlayer;
import com.j0ach1mmall3.jlib.nbsapi.songplayer.SongPlayer;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class Music extends Cosmetic {
    private SongPlayer songPlayer;

    public Music(CosmeticConfig cosmeticConfig, Player player, MusicStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.MUSIC);
    }

    @Override
    protected boolean giveInternal() {
        this.songPlayer = new DynamicSongPlayer(((MusicStorage) this.cosmeticStorage).getSong(), ((MusicStorage) this.cosmeticStorage).isRepeat(), true);
        this.songPlayer.addPlayer(this.player);
        this.songPlayer.start();
        return true;
    }

    @Override
    protected void removeInternal() {
        this.songPlayer.stop();
    }
}
