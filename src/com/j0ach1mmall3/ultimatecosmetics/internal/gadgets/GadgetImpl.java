package com.j0ach1mmall3.ultimatecosmetics.internal.gadgets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Gadget;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 9:55 22/08/2015 using IntelliJ IDEA.
 */
public final class GadgetImpl implements Gadget {
    private Player player;
    private final GadgetStorage gadgetStorage;

    public GadgetImpl(Player player, GadgetStorage gadgetStorage) {
        this.player = player;
        this.gadgetStorage = gadgetStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public GadgetStorage getGadgetStorage() {
        return this.gadgetStorage;
    }

    @Override
    public void give() {
        Main plugin = this.gadgetStorage.getPlugin();
        CosmeticsAPI api = plugin.getApi();
        if (api.hasGadget(this.player)) {
            this.player.getInventory().setItem(plugin.getGadgets().getGadgetSlot(), null);
            give();
        } else {
            if (this.player.getInventory().getItem(plugin.getGadgets().getGadgetSlot()) != null && this.gadgetStorage.getPlugin().getGadgets().isCheckInSlot()) {
                plugin.informPlayerNoPermission(this.player, Placeholders.parse(plugin.getLang().getAlreadyInGadgetsSlot(), this.player));
            } else {
                this.player.getInventory().setItem(plugin.getGadgets().getGadgetSlot(), this.gadgetStorage.getItem());
            }
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.gadgetStorage.getPlugin().getApi();
        if (api.hasGadget(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.player.getInventory().setItem(this.gadgetStorage.getPlugin().getGadgets().getGadgetSlot(), null);
        }
    }
}
