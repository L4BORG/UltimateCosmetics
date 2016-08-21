package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public abstract class CooldownCosmeticStorage extends CosmeticStorage {
    private final int cooldown;

    protected CooldownCosmeticStorage(Main plugin, String identifier, String permission, GuiItem guiItem, int cooldown) {
        super(plugin, identifier, permission, guiItem);
        this.cooldown = cooldown;
    }

    public final int getCooldown() {
        return this.cooldown;
    }
}
