package com.j0ach1mmall3.ultimatecosmetics.modules.banners;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
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
public final class BannersListener implements Listener {
    private final BannersModule module;
    private final Map<Player, Cosmetic> bannerMap = new HashMap<>();

    public BannersListener(BannersModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (this.module.getParent().getApi().hasCosmetics(p, Banner.class)) {
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
            if (this.module.getParent().getApi().hasCosmetics(p, Banner.class)) {
                for(Cosmetic banner : this.module.getParent().getApi().getCosmetics(p, Banner.class)) {
                    this.bannerMap.put(p, banner);
                    e.getDrops().remove(banner.getCosmeticStorage().getjLibItem().getItemStack());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (this.bannerMap.containsKey(p)) {
            p.getInventory().setHelmet(this.bannerMap.get(p).getCosmeticStorage().getjLibItem().getItemStack());
            this.bannerMap.remove(p);
        }
    }
}
