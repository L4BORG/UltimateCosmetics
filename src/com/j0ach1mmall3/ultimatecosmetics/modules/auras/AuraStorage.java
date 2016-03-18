package com.j0ach1mmall3.ultimatecosmetics.modules.auras;


import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.ChatColor;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 6/03/2016
 */
public final class AuraStorage extends CosmeticStorage {
    private final ChatColor color;

    public AuraStorage(Main plugin, String identifier, String permission, GuiItem guiItem, ChatColor color) {
        super(plugin, identifier, permission, guiItem);
        this.color = color;
    }

    public ChatColor getColor() {
        return this.color;
    }
}
