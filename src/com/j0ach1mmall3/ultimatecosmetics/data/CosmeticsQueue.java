package com.j0ach1mmall3.ultimatecosmetics.data;

import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.serialization.JSerializable;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 1/11/2015
 */
public final class CosmeticsQueue {
    private final Main plugin;
    private final HashMap<Class<? extends Cosmetic>, String> cosmetics;

    public CosmeticsQueue(Main plugin, Player player) {
        this.plugin = plugin;
        this.cosmetics = new HashMap<>();
        for(Cosmetic cosmetic : plugin.getApi().getCosmetics(player)) {
            this.cosmetics.put(cosmetic.getClass(), cosmetic.getCosmeticStorage().getIdentifier());
        }
    }

    CosmeticsQueue(Main plugin) {
        this.plugin = plugin;
        this.cosmetics = new HashMap<>();
    }

    CosmeticsQueue(Main plugin, String cosmetics) {
        this.plugin = plugin;
        HashMap o = new HashMap<>();
        if(cosmetics != null) {
            try {
                o = new JSerializable<HashMap>(cosmetics).getObject();
            } catch (Exception e) {
                // NOP
            }
        }
        this.cosmetics = o;
    }

    public String asString() {
        try {
            return new JSerializable<HashMap>(this.cosmetics).getString();
        } catch (Exception e) {
            // NOP
            return "";
        }
    }

    public void giveBack(Player player) {
        final Set<Cosmetic> cosmetics = new HashSet<>();
        for(PluginModule<Main, CosmeticConfig> pluginModule : this.plugin.getModules()) {
            if(pluginModule.isEnabled()) {
                String identifier = this.cosmetics.get(pluginModule.getConfig().getCosmeticClass());
                if(identifier != null && !identifier.isEmpty()) cosmetics.add(pluginModule.getConfig().getCosmetic(pluginModule.getConfig().getByIdentifier(identifier), player));
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                for(Cosmetic cosmetic : cosmetics) {
                    cosmetic.give();
                }
            }
        }, 20L);
    }
}
