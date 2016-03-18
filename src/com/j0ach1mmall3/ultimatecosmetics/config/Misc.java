package com.j0ach1mmall3.ultimatecosmetics.config;

import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.unique.DoubleJumpStorage;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class Misc extends ConfigLoader {
    private final boolean stackerEnabled;
    private final String stackerPermission;
    private final String stackerNoPermissionMessage;
    private final boolean stackerStackPlayersOnly;
    private final String stackerPrefix;
    private final boolean doubleJumpEnabled;
    private final boolean upsideDownEnabled;
    private final String upsideDownPermission;
    private final boolean earsEnabled;
    private final String earsPermission;
    private final List<DoubleJumpStorage> doubleJumpGroups;

    public Misc(Main plugin) {
        super("misc.yml", plugin);
        this.stackerEnabled = this.config.getBoolean("Stacker.Enabled");
        this.stackerPermission = this.config.getString("Stacker.Permission");
        this.stackerNoPermissionMessage = this.config.getString("Stacker.NoPermissionMessage");
        this.stackerStackPlayersOnly = this.config.getBoolean("Stacker.StackPlayersOnly");
        this.stackerPrefix = this.config.getString("Stacker.Prefix");
        this.doubleJumpEnabled = this.config.getBoolean("DoubleJump.Enabled");
        this.upsideDownEnabled = this.config.getBoolean("UpsideDown.Enabled");
        this.upsideDownPermission = this.config.getString("UpsideDown.Permission");
        this.earsEnabled = this.config.getBoolean("Ears.Enabled");
        this.earsPermission = this.config.getString("Ears.Permission");
        this.doubleJumpGroups = this.getDoubleJumpGroupsInternal();
        plugin.getjLogger().log(ChatColor.GREEN + "Misc config successfully loaded!", JLogger.LogLevel.EXTENDED);
    }

    private List<DoubleJumpStorage> getDoubleJumpGroupsInternal() {
        List<DoubleJumpStorage> doubleJumpz = new ArrayList<>();
        for(String s : this.customConfig.getKeys("DoubleJump.Groups")) {
            doubleJumpz.add(this.getDoubleJumpGroupByIdentifier(s));
        }
        return doubleJumpz;
    }

    private DoubleJumpStorage getDoubleJumpGroupByIdentifier(String identifier) {
        String path = "DoubleJump.Groups." + identifier + '.';
        return new DoubleJumpStorage(
                (Main) this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.config.getInt(path + "Multiplier"),
                Sound.valueOf(this.config.getString(path + "Sound").toUpperCase())
        );
    }

    public boolean isStackerEnabled() {
        return this.stackerEnabled;
    }

    public String getStackerPermission() {
        return this.stackerPermission;
    }

    public String getStackerNoPermissionMessage() {
        return this.stackerNoPermissionMessage;
    }

    public boolean isStackerStackPlayersOnly() {
        return this.stackerStackPlayersOnly;
    }

    public String getStackerPrefix() {
        return this.stackerPrefix;
    }

    public boolean isDoubleJumpEnabled() {
        return this.doubleJumpEnabled;
    }

    public boolean isUpsideDownEnabled() {
        return this.upsideDownEnabled;
    }

    public String getUpsideDownPermission() {
        return this.upsideDownPermission;
    }

    public boolean isEarsEnabled() {
        return this.earsEnabled;
    }

    public String getEarsPermission() {
        return this.earsPermission;
    }

    public List<DoubleJumpStorage> getDoubleJumpGroups() {
        return this.doubleJumpGroups;
    }
}
