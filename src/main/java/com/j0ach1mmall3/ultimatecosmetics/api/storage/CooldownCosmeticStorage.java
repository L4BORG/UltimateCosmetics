package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.JLibItem;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public abstract class CooldownCosmeticStorage extends CosmeticStorage {
    private final int cooldown;

    protected CooldownCosmeticStorage(String identifier, String permission, JLibItem jLibItem, int cooldown) {
        super(identifier, permission, jLibItem);
        this.cooldown = cooldown;
    }

    public final int getCooldown() {
        return this.cooldown;
    }
}
