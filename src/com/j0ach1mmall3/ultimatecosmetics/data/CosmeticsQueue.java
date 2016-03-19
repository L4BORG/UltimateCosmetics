package com.j0ach1mmall3.ultimatecosmetics.data;

import com.j0ach1mmall3.jlib.storage.serialization.JSerializable;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 1/11/2015
 */
public final class CosmeticsQueue {
    private final Main plugin;
    private final EnumMap<CosmeticType, String> cosmetics;

    public CosmeticsQueue(Main plugin, Player player) {
        this.plugin = plugin;
        this.cosmetics = new EnumMap<>(CosmeticType.class);
        for(CosmeticType type : CosmeticType.values()) {
            for(Cosmetic cosmetic : plugin.getApi().getCosmetics(player, type)) {
                this.cosmetics.put(type, cosmetic.getCosmeticStorage().getIdentifier());
            }
        }
    }

    CosmeticsQueue(Main plugin) {
        this.plugin = plugin;
        this.cosmetics = new EnumMap<>(CosmeticType.class);
    }

    CosmeticsQueue(Main plugin, String cosmetics) {
        this.plugin = plugin;
        EnumMap o = new EnumMap<>(CosmeticType.class);
        if(cosmetics != null) {
            try {
                o = new JSerializable<EnumMap>(cosmetics).getObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.cosmetics = o;
    }

    public String asString() {
        try {
            return new JSerializable<EnumMap>(this.cosmetics).getString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void giveBack(Player player) {
        final Set<Cosmetic> cosmetics = new HashSet<>();
        for(CosmeticType type : CosmeticType.values()) {
            CosmeticConfig cosmeticConfig = this.plugin.getConfigByType(type);
            String identifier = this.cosmetics.get(type);
            if(cosmeticConfig != null && identifier != null && !identifier.isEmpty()) {
                cosmetics.add(cosmeticConfig.getCosmetic(cosmeticConfig.getByIdentifier(identifier), player));
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
