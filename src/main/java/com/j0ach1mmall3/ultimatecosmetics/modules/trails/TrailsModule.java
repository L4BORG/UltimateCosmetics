package com.j0ach1mmall3.ultimatecosmetics.modules.trails;

import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 8/03/2016
 */
public final class TrailsModule extends PluginModule<Main, Trails> {
    private int task;

    public TrailsModule(Main parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.config = new Trails(this);
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.parent, new Runnable() {
            @Override
            public void run() {
                final Set<Item> items = new HashSet<>();
                for (Trail cosmetic : TrailsModule.this.parent.getApi().getAllCosmetics(Trail.class)) {
                    Player p = cosmetic.getPlayer();
                    ItemStack ci = cosmetic.getCosmeticStorage().getjLibItem().getItemStack().clone();
                    ItemMeta im = ci.getItemMeta();
                    im.setDisplayName(String.valueOf(Random.getInt(100)));
                    ci.setItemMeta(im);
                    Item i = p.getWorld().dropItem(p.getLocation(), ci);
                    i.setPickupDelay(Integer.MAX_VALUE);
                    TrailsModule.this.parent.queueEntity(i);
                    items.add(i);
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(TrailsModule.this.parent, new Runnable() {
                    @Override
                    public void run() {
                        for(Item i : items) {
                            i.remove();
                            TrailsModule.this.parent.removeEntity(i);
                        }
                    }
                }, TrailsModule.this.config.getRemoveDelay() * 20L);
            }
        }, this.config.getDropInterval(), this.config.getDropInterval());
        new TrailsListener(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.task);
    }
}
