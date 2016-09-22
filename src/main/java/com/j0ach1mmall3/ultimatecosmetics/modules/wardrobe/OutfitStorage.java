package com.j0ach1mmall3.ultimatecosmetics.modules.wardrobe;


import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.inventory.ItemStack;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class OutfitStorage extends CosmeticStorage {
    private final ItemStack leggingsItem;
    private final ItemStack bootsItem;

    public OutfitStorage(String identifier, String permission, JLibItem jlibItem, ItemStack leggingsItem, ItemStack bootsItem) {
        super(identifier, permission, jlibItem);
        this.leggingsItem = leggingsItem;
        this.bootsItem = bootsItem;
    }

    public ItemStack getLeggingsItem() {
        return this.leggingsItem;
    }

    public ItemStack getBootsItem() {
        return this.bootsItem;
    }
}
