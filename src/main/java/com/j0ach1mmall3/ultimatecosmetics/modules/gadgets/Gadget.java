package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Gadget extends Cosmetic<GadgetStorage> {
    public Gadget(Player player, GadgetStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(Main plugin) {
        if (this.player.getInventory().getItem(plugin.getConfig(Gadgets.class).getGadgetSlot()) != null && plugin.getConfig(Gadgets.class).isCheckInSlot()) {
            Methods.informPlayerNoPermission(this.player, Placeholders.parse(plugin.getLang().getAlreadyInGadgetsSlot(), this.player));
            return false;
        } else {
            this.player.getInventory().setItem(plugin.getConfig(Gadgets.class).getGadgetSlot(), this.cosmeticStorage.getjLibItem().getItemStack());
            return true;
        }
    }

    @Override
    protected void removeInternal(Main plugin) {
        this.player.getInventory().setItem(plugin.getConfig(Gadgets.class).getGadgetSlot(), null);
    }
}
