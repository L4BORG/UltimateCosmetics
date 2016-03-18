package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CooldownCosmeticStorage;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.inventory.ItemStack;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 1/10/2015
 */
public final class MorphStorage extends CooldownCosmeticStorage {
    private final DisguiseType morphType;
    private final boolean ability;
    private final ItemStack abilityItem;
    private final int abilityDuration;

    public MorphStorage(Main plugin, String identifier, String permission, GuiItem guiItem, int cooldown, DisguiseType morphType, boolean ability, ItemStack abilityItem, int abilityDuration) {
        super(plugin, identifier, permission, guiItem, cooldown);
        this.morphType = morphType;
        this.ability = ability;
        this.abilityItem = abilityItem;
        this.abilityDuration = abilityDuration;
    }

    public DisguiseType getMorphType() {
        return this.morphType;
    }

    public boolean isAbility() {
        return this.ability;
    }

    public ItemStack getAbilityItem() {
        return this.abilityItem;
    }

    public int getAbilityDuration() {
        return this.abilityDuration;
    }
}
