package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;

import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import com.j0ach1mmall3.ultimatecosmetics.modules.balloons.Balloon;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
        return new Gadget(this, player, (GadgetStorage) cosmeticStorage);
    }

    @Override
    protected CosmeticStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new GadgetStorage(
                this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getGuiItemNew(this.config, path),
                this.config.getInt(path + "Cooldown"),
                Sound.valueOf(this.config.getString(path + "Sound").toUpperCase()),
                this.config.getBoolean(path + "UseAmmo"),
                this.config.getString(path + "AmmoName")
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

    public int getIntValue(String identifier, String value) {
        return this.config.getInt("Gadgets." + identifier + '.' + value);
    }

    public boolean getBooleanValue(String identifier, String value) {
        return this.config.getBoolean("Gadgets." + identifier + '.' + value);
    }

    public Sound getSound(String identifier, String value) {
        return Sound.valueOf(this.config.getString("Gadgets." + identifier + '.' + value).toUpperCase());
    }

    public List<String> getStringList(String identifier, String value) {
        return this.config.getStringList("Gadgets." + identifier + '.' + value);
    }

    public boolean isGadgetItem(ItemStack itemStack) {
        return this.getGadgetStorage(itemStack) != null;
    }

    public GadgetStorage getGadgetStorage(ItemStack itemStack) {
        if(itemStack == null) return null;
        for(CosmeticStorage cosmeticStorage : this.getCosmetics()) {
            if(General.areSimilar(cosmeticStorage.getGuiItem().getItem(), itemStack)) return (GadgetStorage) cosmeticStorage;
        }
        return null;
    }
}
