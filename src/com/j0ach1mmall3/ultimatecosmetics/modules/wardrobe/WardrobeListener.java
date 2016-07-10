package com.j0ach1mmall3.ultimatecosmetics.modules.wardrobe;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 24/08/2015
 */
public final class WardrobeListener implements Listener {
    private final WardrobeModule module;
    private final Map<Player, Outfit> outfitMap = new HashMap<>();

    public WardrobeListener(WardrobeModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (this.module.getParent().getApi().hasCosmetics((Player) e.getWhoClicked(), Outfit.class) && (e.getRawSlot() == 6 || e.getRawSlot() == 7 || e.getRawSlot() == 8)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (this.module.getConfig().isKeepOnDeath()) {
            Player p = e.getEntity();
            for(Outfit cosmetic : this.module.getParent().getApi().getCosmetics(p, Outfit.class)) {
                this.outfitMap.put(p, cosmetic);
                e.getDrops().remove(cosmetic.getCosmeticStorage().getGuiItem().getItem());
                e.getDrops().remove(cosmetic.getCosmeticStorage().getLeggingsItem());
                e.getDrops().remove(cosmetic.getCosmeticStorage().getBootsItem());
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (this.module.getParent().getApi().hasCosmetics(p, Outfit.class)) {
                ItemStack chestplate = p.getInventory().getChestplate();
                if (chestplate != null) chestplate.setDurability((short) 0);
                p.getInventory().setChestplate(chestplate);
                ItemStack leggings = p.getInventory().getLeggings();
                if (leggings != null) leggings.setDurability((short) 0);
                p.getInventory().setLeggings(leggings);
                ItemStack boots = p.getInventory().getBoots();
                if (boots != null) boots.setDurability((short) 0);
                p.getInventory().setBoots(boots);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (this.outfitMap.containsKey(p)) {
            Outfit cosmetic = this.outfitMap.get(p);
            p.getInventory().setChestplate(cosmetic.getCosmeticStorage().getGuiItem().getItem());
            p.getInventory().setLeggings(cosmetic.getCosmeticStorage().getLeggingsItem());
            p.getInventory().setBoots(cosmetic.getCosmeticStorage().getBootsItem());
            this.outfitMap.remove(p);
        }
    }
}
