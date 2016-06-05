package com.j0ach1mmall3.ultimatecosmetics.config;

import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.ChatColor;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class Lang extends ConfigLoader<Main> {
    private final String notEnabled;
    private final String commandNoPermission;
    private final String alreadyOnHead;
    private final String dismountVehicle;
    private final String renamePet;
    private final String successfulRename;
    private final String alreadyOnBody;
    private final String stackedPlayer;
    private final String stackedByPlayer;
    private final String gadgetsCooldown;
    private final String lostAmmo;
    private final String notEnoughAmmo;
    private final String alreadyInGadgetsSlot;
    private final String alreadyInAbilitySlot;
    private final String abilityCooldown;
    private final String toggled;
    private final String stackerNotEnabled;
    private final String stackedNotEnabled;

    public Lang(Main plugin) {
        super("lang.yml", plugin);
        this.notEnabled = this.config.getString("NotEnabled");
        this.commandNoPermission = this.config.getString("CommandNoPermission");
        this.alreadyOnHead = this.config.getString("AlreadyOnHead");
        this.dismountVehicle = this.config.getString("DismountVehicle");
        this.renamePet = this.config.getString("RenamePet");
        this.successfulRename = this.config.getString("SuccessfulRename");
        this.alreadyOnBody = this.config.getString("AlreadyOnBody");
        this.stackedPlayer = this.config.getString("StackedPlayer");
        this.stackedByPlayer = this.config.getString("StackedByPlayer");
        this.gadgetsCooldown = this.config.getString("GadgetsCooldown");
        this.lostAmmo = this.config.getString("LostAmmo");
        this.notEnoughAmmo = this.config.getString("NotEnoughAmmo");
        this.alreadyInGadgetsSlot = this.config.getString("AlreadyInGadgetsSlot");
        this.alreadyInAbilitySlot = this.config.getString("AlreadyInAbilitySlot");
        this.abilityCooldown = this.config.getString("AbilityCooldown");
        this.toggled = this.config.getString("Toggled");
        this.stackerNotEnabled = this.config.getString("StackerNotEnabled");
        this.stackedNotEnabled = this.config.getString("StackedNotEnabled");
        plugin.getjLogger().log(ChatColor.GREEN + "Language config successfully loaded!", JLogger.LogLevel.EXTENDED);
    }

    public String getStackedByPlayer() {
        return this.stackedByPlayer;
    }

    public String getStackedPlayer() {
        return this.stackedPlayer;
    }

    public String getAlreadyOnBody() {
        return this.alreadyOnBody;
    }

    public String getSuccessfulRename() {
        return this.successfulRename;
    }

    public String getRenamePet() {
        return this.renamePet;
    }

    public String getDismountVehicle() {
        return this.dismountVehicle;
    }

    public String getAlreadyOnHead() {
        return this.alreadyOnHead;
    }

    public String getCommandNoPermission() {
        return this.commandNoPermission;
    }

    public String getNotEnabled() {
        return this.notEnabled;
    }

    public String getGadgetsCooldown() {
        return this.gadgetsCooldown;
    }

    public String getNotEnoughAmmo() {
        return this.notEnoughAmmo;
    }

    public String getLostAmmo() {
        return this.lostAmmo;
    }

    public String getAlreadyInGadgetsSlot() {
        return this.alreadyInGadgetsSlot;
    }

    public String getAlreadyInAbilitySlot() {
        return this.alreadyInAbilitySlot;
    }

    public String getAbilityCooldown() {
        return this.abilityCooldown;
    }

    public String getToggled() {
        return this.toggled;
    }

    public String getStackedNotEnabled() {
        return this.stackedNotEnabled;
    }
}
