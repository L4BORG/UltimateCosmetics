package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.unique.UniqueCosmeticStorage;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 28/02/2016
 */
public class CosmeticStorage extends UniqueCosmeticStorage {
    protected final int page;
    protected final GuiItem guiItem;

    public CosmeticStorage(Main plugin, String identifier, String permission, GuiItem guiItem) {
        super(plugin, identifier, permission);
        int position = guiItem.getPosition();
        this.page = position / 45;
        this.guiItem = new GuiItem(guiItem.getItem(), position - 45 * this.page);
    }

    public final int getPage() {
        return this.page;
    }

    public final GuiItem getGuiItem() {
        return this.guiItem;
    }
}
