package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;


import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CooldownCosmeticStorage;
import org.bukkit.Sound;

import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 22/08/2015
 */
public final class GadgetStorage extends CooldownCosmeticStorage {
    private final Sound sound;
    private final boolean useAmmo;
    private final String ammoName;
    private final Map<String, Object> additionalValues;

    public GadgetStorage(String identifier, String permission, JLibItem jLibItem, int cooldown, Sound sound, boolean useAmmo, String ammoName, Map<String, Object> additionalValues) {
        super(identifier, permission, jLibItem, cooldown);
        this.sound = sound;
        this.useAmmo = useAmmo;
        this.ammoName = ammoName;
        this.additionalValues = additionalValues;
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

    public Map<String, Object> getAdditionalValues() {
        return this.additionalValues;
    }
}
