package com.j0ach1mmall3.ultimatecosmetics.config;

import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
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
public final class Misc extends ConfigLoader<Main> {
    private final boolean stackerEnabled;
    private final String stackerPermission;
    private final String stackerNoPermissionMessage;
    private final boolean stackerStackPlayersOnly;
    private final String stackerPrefix;
    private final boolean doubleJumpEnabled;
    private final List<DoubleJumpStorage> doubleJumpGroups;

    public Misc(Main plugin) {
        super("misc.yml", plugin);
        if(this.config.getBoolean("Stacker.Enabled") && ReflectionAPI.verBiggerThan(1, 9)) {
            this.storage.getPlugin().getjLogger().log(ChatColor.RED + "Stacker is enabled in the misc config, but you are running 1.9 or higher. Adjusting that value.", JLogger.LogLevel.MINIMAL);
            this.stackerEnabled = false;
        } else this.stackerEnabled = this.config.getBoolean("Stacker.Enabled");
        this.stackerPermission = this.config.getString("Stacker.Permission");
        this.stackerNoPermissionMessage = this.config.getString("Stacker.NoPermissionMessage");
        this.stackerStackPlayersOnly = this.config.getBoolean("Stacker.StackPlayersOnly");
        this.stackerPrefix = this.config.getString("Stacker.Prefix");
        this.doubleJumpEnabled = this.config.getBoolean("DoubleJump.Enabled");
        this.doubleJumpGroups = this.loadDoubleJumpGroups();
        plugin.getjLogger().log(ChatColor.GREEN + "Misc config successfully loaded!", JLogger.LogLevel.EXTENDED);
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

    public List<DoubleJumpStorage> getDoubleJumpGroups() {
        return this.doubleJumpGroups;
    }

    private List<DoubleJumpStorage> loadDoubleJumpGroups() {
        List<DoubleJumpStorage> doubleJumps = new ArrayList<>();
        for(String s : this.storage.getKeys("DoubleJump.Groups")) {
            doubleJumps.add(new DoubleJumpStorage(s, this.config.getString("DoubleJump.Groups." + s + ".Permission"), this.config.getInt("DoubleJump.Groups." + s + ".Multiplier"), Sound.valueOf(this.config.getString("DoubleJump.Groups." + s + ".Sound").toUpperCase())));
        }
        return doubleJumps;
    }
}
