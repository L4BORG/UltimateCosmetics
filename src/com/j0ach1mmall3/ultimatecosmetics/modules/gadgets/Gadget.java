package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Gadget extends Cosmetic {
    public Gadget(CosmeticConfig cosmeticConfig, Player player, GadgetStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.GADGET);
    }

    @Override
    protected boolean giveInternal() {
        Gadgets gadgets = ((Gadgets) this.cosmeticConfig);
        if (this.player.getInventory().getItem(gadgets.getGadgetSlot()) != null && gadgets.isCheckInSlot()) {
            this.cosmeticStorage.getPlugin().informPlayerNoPermission(this.player, Placeholders.parse(this.cosmeticStorage.getPlugin().getLang().getAlreadyInGadgetsSlot(), this.player));
            return false;
        } else {
            this.player.getInventory().setItem(gadgets.getGadgetSlot(), this.cosmeticStorage.getGuiItem().getItem());
            return true;
        }
    }

    @Override
    protected void removeInternal() {
        this.player.getInventory().setItem(((Gadgets) this.cosmeticConfig).getGadgetSlot(), null);
    }
}
