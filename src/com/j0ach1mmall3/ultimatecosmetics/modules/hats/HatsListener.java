package com.j0ach1mmall3.ultimatecosmetics.modules.hats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class HatsListener implements Listener {
    private final HatsModule module;
    private final Map<Player, Hat> hatMap = new HashMap<>();

    public HatsListener(HatsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (this.module.getParent().getApi().hasCosmetics(p, Hat.class)) {
            if (e.getRawSlot() == 5) {
                e.setCancelled(true);
                p.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (this.module.getConfig().isKeepOnDeath()) {
            Player p = e.getEntity();
            if (this.module.getParent().getApi().hasCosmetics(p, Hat.class)) {
                for(Hat cosmetic : this.module.getParent().getApi().getCosmetics(p, Hat.class)) {
                    this.hatMap.put(p, cosmetic);
                    e.getDrops().remove(cosmetic.getCosmeticStorage().getGuiItem().getItem());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (this.hatMap.containsKey(p)) {
            p.getInventory().setHelmet(this.hatMap.get(p).getCosmeticStorage().getGuiItem().getItem());
            this.hatMap.remove(p);
        }
    }
}
