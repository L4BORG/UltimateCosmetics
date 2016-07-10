package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 1/10/2015
 */
public final class Morphs extends CosmeticConfig<MorphStorage> {
    private final boolean enableAbilities;
    private final boolean checkInSlot;
    private final int abilitySlot;

    public Morphs(MorphsModule module) {
        super("morphs.yml", module.getParent(), "Morphs");
        this.enableAbilities = this.config.getBoolean("EnableAbilities");
        this.checkInSlot = this.config.getBoolean("CheckInSlot");
        this.abilitySlot = this.config.getInt("AbilitySlot");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Morph.class;
    }

    @Override
    public Cosmetic getCosmetic(MorphStorage cosmeticStorage, Player player) {
        return new Morph(this, player, cosmeticStorage);
    }

    @Override
    protected CosmeticStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new MorphStorage(
                this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getGuiItemNew(this.config, path),
                this.config.getInt(path + "Ability.Cooldown"),
                DisguiseType.valueOf(this.config.getString(path + "MorphType")),
                this.config.getBoolean(path + "Ability.Enabled"),
                this.storage.getItemNew(this.config, path + "Ability.Item"),
                this.config.getInt(path + "Ability.Duration")
        );
    }

    public boolean isAbilityItem(ItemStack itemStack) {
        return this.getMorphByAbilityItem(itemStack) != null;
    }

    public MorphStorage getMorphByAbilityItem(ItemStack itemStack) {
        for(CosmeticStorage cosmeticStorage : this.getCosmetics()) {
            if(General.areSimilar(((MorphStorage) cosmeticStorage).getAbilityItem(), itemStack)) return (MorphStorage) cosmeticStorage;
        }
        return null;
    }

    public boolean isEnableAbilities() {
        return this.enableAbilities;
    }

    public boolean isCheckInSlot() {
        return this.checkInSlot;
    }

    public int getAbilitySlot() {
        return this.abilitySlot;
    }
}
