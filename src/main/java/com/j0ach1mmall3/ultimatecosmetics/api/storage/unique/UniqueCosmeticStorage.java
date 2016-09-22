package com.j0ach1mmall3.ultimatecosmetics.api.storage.unique;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 29/02/2016
 */
public abstract class UniqueCosmeticStorage {
    protected final String identifier;
    protected final String permission;

    protected UniqueCosmeticStorage(String identifier, String permission) {
        this.identifier = identifier;
        this.permission = permission;
    }

    public final String getIdentifier() {
        return this.identifier;
    }

    public final String getPermission() {
        return this.permission;
    }
}
