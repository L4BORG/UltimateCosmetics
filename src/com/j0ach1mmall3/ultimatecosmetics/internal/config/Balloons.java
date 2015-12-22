package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.BalloonStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by j0ach1mmall3 on 11:10 20/08/2015 using IntelliJ IDEA.
 */
public final class Balloons extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final List<String> worldsBlacklist;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final boolean disableBatSounds;
    private final int teleportInterval;
    private final int teleportDistance;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final GuiItem removeItem;
    private final GuiItem homeItem;
    private final GuiItem previousItem;
    private final GuiItem nextItem;
    private final List<BalloonStorage> balloons;

    private final int maxPage;

    public Balloons(Main plugin) {
        super("balloons.yml", plugin);
        this.plugin = plugin;
        this.enabled = this.config.getBoolean("Enabled");
        this.worldsBlacklist = this.config.getStringList("WorldsBlacklist");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.disableBatSounds = this.config.getBoolean("DisableBatSounds");
        this.teleportInterval = this.config.getInt("TeleportInterval");
        this.teleportDistance = this.config.getInt("TeleportDistance");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = this.config.getInt("GUISize");
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.removeItem = Methods.getGuiItem(this.config, "RemoveItem");
        this.homeItem = Methods.getGuiItem(this.config, "HomeItem");
        this.previousItem = Methods.getGuiItem(this.config, "PreviousItem");
        this.nextItem = Methods.getGuiItem(this.config, "NextItem");
        this.balloons = getBalloonsInternal();
        this.maxPage = getMaxPageInternal();
        if (plugin.getBabies().getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Balloons config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (BalloonStorage balloon : this.balloons) {
            if (balloon.getPosition() > biggestSlot) biggestSlot = balloon.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<BalloonStorage> getBalloonsInternal() {
        List<BalloonStorage> balloonz = new ArrayList<>();
        for(String s : this.customConfig.getKeys("Balloons")) {
            balloonz.add(this.getBalloonByIdentifier(s));
        }
        return balloonz;
    }

    private BalloonStorage getBalloonByIdentifier(String identifier) {
        String path = "Balloons." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new BalloonStorage(
                this.plugin,
                identifier,
                new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                this.config.getInt(path + "Position"),
                this.config.getString(path + "Permission")
        );
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public List<String> getWorldsBlacklist() {
        return Collections.unmodifiableList(this.worldsBlacklist);
    }

    public String getNoPermissionMessage() {
        return this.noPermissionMessage;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isDisableBatSounds() {
        return this.disableBatSounds;
    }

    public int getTeleportInterval() {
        return this.teleportInterval;
    }

    public int getTeleportDistance() {
        return this.teleportDistance;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public Sound getGiveSound() {
        return this.giveSound;
    }

    public Sound getRemoveSound() {
        return this.removeSound;
    }

    public int getGuiSize() {
        return this.guiSize;
    }

    public GuiItem getRemoveItem() {
        return this.removeItem;
    }

    public GuiItem getHomeItem() {
        return this.homeItem;
    }

    public GuiItem getPreviousItem() {
        return this.previousItem;
    }

    public GuiItem getNextItem() {
        return this.nextItem;
    }

    public boolean isNoPermissionItemEnabled() {
        return this.noPermissionItem_Enabled;
    }

    public CustomItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public int getMaxPage() {
        return this.maxPage;
    }

    public Iterable<BalloonStorage> getBalloons() {
        return Collections.unmodifiableList(this.balloons);
    }

    public CustomItem getNoPermissionItem(BalloonStorage balloon) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", balloon.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }
}
