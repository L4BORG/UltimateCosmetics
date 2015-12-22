package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.TrailStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by j0ach1mmall3 on 14:21 23/08/2015 using IntelliJ IDEA.
 */
public final class Trails extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final List<String> worldsBlacklist;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final double dropInterval;
    private final int removeDelay;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final GuiItem removeItem;
    private final GuiItem homeItem;
    private final GuiItem previousItem;
    private final GuiItem nextItem;
    private final List<TrailStorage> trails;

    private final int maxPage;

    public Trails(Main plugin) {
        super("trails.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.worldsBlacklist = this.config.getStringList("WorldsBlacklist");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.dropInterval = this.config.getDouble("DropInterval");
        this.removeDelay = this.config.getInt("RemoveDelay");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.removeItem = Methods.getGuiItem(this.config, "RemoveItem");
        this.homeItem = Methods.getGuiItem(this.config, "HomeItem");
        this.previousItem = Methods.getGuiItem(this.config, "PreviousItem");
        this.nextItem = Methods.getGuiItem(this.config, "NextItem");
        this.trails = getTrailsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Trails config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (TrailStorage trail : this.trails) {
            if (trail.getPosition() > biggestSlot) biggestSlot = trail.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<TrailStorage> getTrailsInternal() {
        List<TrailStorage> trailz = new ArrayList<>();
        for(String s : this.customConfig.getKeys("Trails")) {
            trailz.add(this.getTrailByIdentifier(s));
        }
        return trailz;
    }

    private TrailStorage getTrailByIdentifier(String identifier) {
        String path = "Trails." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new TrailStorage(
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

    public String getNoPermissionMessage() {
        return this.noPermissionMessage;
    }

    public String getCommand() {
        return this.command;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public Iterable<TrailStorage> getTrails() {
        return Collections.unmodifiableList(this.trails);
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

    public List<String> getWorldsBlacklist() {
        return Collections.unmodifiableList(this.worldsBlacklist);
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

    public CustomItem getNoPermissionItem(TrailStorage trail) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", trail.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public double getDropInterval() {
        return this.dropInterval;
    }

    public int getRemoveDelay() {
        return this.removeDelay;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
