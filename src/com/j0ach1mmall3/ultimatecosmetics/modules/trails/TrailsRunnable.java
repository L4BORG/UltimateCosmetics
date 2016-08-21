package com.j0ach1mmall3.ultimatecosmetics.modules.trails;

import com.j0ach1mmall3.jlib.methods.Random;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public final class TrailsRunnable extends BukkitRunnable {
    private final TrailsModule module;

    public TrailsRunnable(TrailsModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        final Set<Item> items = new HashSet<>();
        for (Trail cosmetic : this.module.getParent().getApi().getAllCosmetics(Trail.class)) {
            Player p = cosmetic.getPlayer();
            ItemStack ci = cosmetic.getCosmeticStorage().getGuiItem().getItem().clone();
            ItemMeta im = ci.getItemMeta();
            im.setDisplayName(String.valueOf(Random.getInt(100)));
            ci.setItemMeta(im);
            Item i = p.getWorld().dropItem(p.getLocation(), ci);
            i.setPickupDelay(Integer.MAX_VALUE);
            this.module.getParent().queueEntity(i);
            items.add(i);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Item i : items) {
                    i.remove();
                    TrailsRunnable.this.module.getParent().removeEntity(i);
                }

            }
        }.runTaskLater(this.module.getParent(), this.module.getConfig().getRemoveDelay() * 20);
    }
}
