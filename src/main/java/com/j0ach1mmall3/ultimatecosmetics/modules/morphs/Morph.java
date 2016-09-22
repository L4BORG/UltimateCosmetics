package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Morph extends Cosmetic<MorphStorage> {
    public Morph(Player player, MorphStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(Main plugin) {
        if (this.cosmeticStorage.isAbility()) {
            if (this.player.getInventory().getItem(plugin.getConfig(Morphs.class).getAbilitySlot()) != null && plugin.getConfig(Morphs.class).isCheckInSlot()) {
                Methods.informPlayerNoPermission(this.player, Placeholders.parse(plugin.getLang().getAlreadyInAbilitySlot(), this.player));
                return false;
            } else this.player.getInventory().setItem(plugin.getConfig(Morphs.class).getAbilitySlot(), this.cosmeticStorage.getAbilityItem());
        }
        MobDisguise md = new MobDisguise(this.cosmeticStorage.getMorphType());
        DisguiseAPI.disguiseIgnorePlayers(this.player, md, this.player);
        return true;
    }

    @Override
    protected void removeInternal(Main plugin) {
        this.player.setFlying(false);
        this.player.setAllowFlight(false);
        DisguiseAPI.undisguiseToAll(this.player);
        if (this.cosmeticStorage.isAbility()) this.player.getInventory().setItem(plugin.getConfig(Morphs.class).getAbilitySlot(), null);
    }
}
