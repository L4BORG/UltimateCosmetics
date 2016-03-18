package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;


import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CooldownCosmeticStorage;
import org.bukkit.Sound;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 22/08/2015
 */
public final class GadgetStorage extends CooldownCosmeticStorage {
    private final Sound sound;
    private final boolean useAmmo;
    private final String ammoName;

    public GadgetStorage(Main plugin, String identifier, String permission, GuiItem guiItem, int cooldown, Sound sound, boolean useAmmo, String ammoName) {
        super(plugin, identifier, permission, guiItem, cooldown);
        this.sound = sound;
        this.useAmmo = useAmmo;
        this.ammoName = ammoName;
    }

    public Sound getSound() {
        return this.sound;
    }

    public boolean isUseAmmo() {
        return this.useAmmo;
    }

    public String getAmmoName() {
        return this.ammoName;
    }
}
