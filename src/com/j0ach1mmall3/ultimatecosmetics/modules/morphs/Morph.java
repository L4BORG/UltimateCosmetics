package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Morph extends Cosmetic {
    public Morph(CosmeticConfig cosmeticConfig, Player player, MorphStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.MORPH);
    }

    @Override
    protected boolean giveInternal() {
        if (((MorphStorage) this.cosmeticStorage).isAbility()) {
            Morphs morphs = ((Morphs) this.cosmeticConfig);
            if (this.player.getInventory().getItem(morphs.getAbilitySlot()) != null && morphs.isCheckInSlot()) {
                this.cosmeticStorage.getPlugin().informPlayerNoPermission(this.player, Placeholders.parse(this.cosmeticStorage.getPlugin().getLang().getAlreadyInAbilitySlot(), this.player));
                return false;
            } else this.player.getInventory().setItem(morphs.getAbilitySlot(), ((MorphStorage) this.cosmeticStorage).getAbilityItem());
        }
        MobDisguise md = new MobDisguise(((MorphStorage) this.cosmeticStorage).getMorphType());
        DisguiseAPI.disguiseIgnorePlayers(this.player, md, this.player);
        return true;
    }

    @Override
    protected void removeInternal() {
        this.player.setFlying(false);
        this.player.setAllowFlight(false);
        DisguiseAPI.undisguiseToAll(this.player);
        if (((MorphStorage) this.cosmeticStorage).isAbility()) this.player.getInventory().setItem(((Morphs) this.cosmeticConfig).getAbilitySlot(), null);
    }
}
