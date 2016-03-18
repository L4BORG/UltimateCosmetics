package com.j0ach1mmall3.ultimatecosmetics.modules.music;

import com.j0ach1mmall3.jlib.nbsapi.NBSDecoder;
import com.j0ach1mmall3.jlib.nbsapi.Song;
import com.j0ach1mmall3.jlib.plugin.JlibPlugin;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class Musics extends CosmeticConfig {
    public Musics(MusicModule musicModule) {
        super("music.yml", musicModule.getParent(), "Music");
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new Music(this, player, (MusicStorage) cosmeticStorage);
    }

    @Override
    protected CosmeticStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        Song song = null;
        try {
            song = new NBSDecoder(new File(this.storage.getPlugin().getDataFolder().getPath() + "/songs", this.config.getString(path + "SongName") + ".nbs")).getSong();
        } catch (Exception e) {
            ((JlibPlugin) this.storage.getPlugin()).getjLogger().log(ChatColor.RED + "An exception occured while loading Song " + identifier + '!');
            e.printStackTrace();
        }
        return new MusicStorage(
                (Main) this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.customConfig.getGuiItemNew(this.config, path),
                song,
                this.config.getBoolean(path + "Repeat")

        );
    }
}
