package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Morph extends Cosmetic<Morphs, MorphStorage> {
    public Morph(Morphs cosmeticConfig, Player player, MorphStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal() {
        if (this.cosmeticStorage.isAbility()) {
            Morphs morphs = this.cosmeticConfig;
            if (this.player.getInventory().getItem(morphs.getAbilitySlot()) != null && morphs.isCheckInSlot()) {
                Methods.informPlayerNoPermission(this.player, Placeholders.parse(this.cosmeticStorage.getPlugin().getLang().getAlreadyInAbilitySlot(), this.player));
                return false;
            } else this.player.getInventory().setItem(morphs.getAbilitySlot(), this.cosmeticStorage.getAbilityItem());
        }
        MobDisguise md = new MobDisguise(this.cosmeticStorage.getMorphType());
        DisguiseAPI.disguiseIgnorePlayers(this.player, md, this.player);
        return true;
    }

    @Override
    protected void removeInternal() {
        this.player.setFlying(false);
        this.player.setAllowFlight(false);
        DisguiseAPI.undisguiseToAll(this.player);
        if (this.cosmeticStorage.isAbility()) this.player.getInventory().setItem(this.cosmeticConfig.getAbilitySlot(), null);
    }
}
