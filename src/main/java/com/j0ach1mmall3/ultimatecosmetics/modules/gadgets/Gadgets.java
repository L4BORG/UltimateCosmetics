package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 22/08/2015
 */
public final class Gadgets extends CosmeticConfig<GadgetStorage> {
    private final boolean keepGadget;
    private final boolean checkInSlot;
    private final int gadgetSlot;

    public Gadgets(GadgetsModule module) {
        super("gadgets.yml", module.getParent(), "Gadgets");
        this.keepGadget = this.config.getBoolean("KeepGadget");
        this.checkInSlot = this.config.getBoolean("CheckInSlot");
        this.gadgetSlot = this.config.getInt("GadgetSlot");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Gadget.class;
    }

    @Override
    public Cosmetic getCosmetic(GadgetStorage cosmeticStorage, Player player) {
        return new Gadget(player, cosmeticStorage);
    }

    @Override
    protected GadgetStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        Map<String, Object> values = new HashMap<>();
        for(String s : this.config.getConfigurationSection(section + '.' + identifier).getKeys(true)) {
            values.put(s, this.config.get(path + s));
        }
        return new GadgetStorage(
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getJLibItem(this.config, path),
                this.config.getInt(path + "Cooldown"),
                Sound.valueOf(this.config.getString(path + "Sound").toUpperCase()),
                this.config.getBoolean(path + "UseAmmo"),
                this.config.getString(path + "AmmoName"),
                values
        );
    }

    public boolean isKeepGadget() {
        return this.keepGadget;
    }

    public boolean isCheckInSlot() {
        return this.checkInSlot;
    }

    public int getGadgetSlot() {
        return this.gadgetSlot;
    }

    public boolean isGadgetItem(ItemStack itemStack) {
        return this.getGadgetStorage(itemStack) != null;
    }

    public GadgetStorage getGadgetStorage(ItemStack itemStack) {
        if(itemStack == null) return null;
        for(GadgetStorage cosmeticStorage : this.cosmetics) {
            if(cosmeticStorage.getjLibItem().isSimilar(itemStack)) return cosmeticStorage;
        }
        return null;
    }
}
