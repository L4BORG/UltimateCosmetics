package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.unique.UniqueCosmeticStorage;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 28/02/2016
 */
public class CosmeticStorage extends UniqueCosmeticStorage {
    protected final JLibItem jLibItem;

    public CosmeticStorage(String identifier, String permission, JLibItem jLibItem) {
        super(identifier, permission);
        this.jLibItem = jLibItem;
    }

    public final JLibItem getjLibItem() {
        return this.jLibItem;
    }
}
