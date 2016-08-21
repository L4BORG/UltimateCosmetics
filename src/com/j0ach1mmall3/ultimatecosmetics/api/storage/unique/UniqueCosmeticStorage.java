package com.j0ach1mmall3.ultimatecosmetics.api.storage.unique;

import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 29/02/2016
 */
public abstract class UniqueCosmeticStorage {
    protected final Main plugin;
    protected final String identifier;
    protected final String permission;

    protected UniqueCosmeticStorage(Main plugin, String identifier, String permission) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.permission = permission;
    }

    public final Main getPlugin() {
        return this.plugin;
    }

    public final String getIdentifier() {
        return this.identifier;
    }

    public final String getPermission() {
        return this.permission;
    }
}
