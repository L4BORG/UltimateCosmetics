package com.j0ach1mmall3.ultimatecosmetics.api;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 17/08/2015
 */
public final class CosmeticsAPI {
    private final Main plugin;
    private final Map<Player, Set<Cosmetic>> cosmetics = new HashMap<>();

    public CosmeticsAPI(Main plugin) {
        this.plugin = plugin;
    }

    public Set<Cosmetic> getCosmetics(Player p) {
        return this.cosmetics.containsKey(p) ? this.cosmetics.get(p) : Collections.newSetFromMap(new ConcurrentHashMap<Cosmetic, Boolean>());
    }

    public Set<Cosmetic> getCosmetics(Player p, CosmeticType... type) {
        List<CosmeticType> types = Arrays.asList(type);
        Set<Cosmetic> cosmetics = Collections.newSetFromMap(new ConcurrentHashMap<Cosmetic, Boolean>());
        for(Cosmetic cosmetic : this.getCosmetics(p)) {
            if(types.contains(cosmetic.getCosmeticType())) cosmetics.add(cosmetic);
        }
        return cosmetics;
    }

    public void addCosmetics(Player p, Cosmetic... cosmetic) {
        Set<Cosmetic> cosmetics = this.getCosmetics(p);
        cosmetics.addAll(Arrays.asList(cosmetic));
        this.cosmetics.put(p, cosmetics);
    }

    public void removeCosmetics(Player p, CosmeticType... type) {
        List<CosmeticType> types = Arrays.asList(type);
        Set<Cosmetic> cosmetics = Collections.newSetFromMap(new ConcurrentHashMap<Cosmetic, Boolean>());
        for(Cosmetic cosmetic : this.getCosmetics(p)) {
            if(!types.contains(cosmetic.getCosmeticType())) cosmetics.add(cosmetic);
        }
        this.cosmetics.put(p, cosmetics);
    }

    public boolean hasCosmetics(Player p, CosmeticType... type) {
        return !this.getCosmetics(p, type).isEmpty();
    }

    public Set<Cosmetic> getCosmetics() {
        Set<Cosmetic> cosmetics = Collections.newSetFromMap(new ConcurrentHashMap<Cosmetic, Boolean>());
        for(Map.Entry<Player, Set<Cosmetic>> entry : this.cosmetics.entrySet()) {
            cosmetics.addAll(entry.getValue());
        }
        return cosmetics;
    }

    public Set<Cosmetic> getCosmetics(CosmeticType... type) {
        List<CosmeticType> types = Arrays.asList(type);
        Set<Cosmetic> cosmetics = Collections.newSetFromMap(new ConcurrentHashMap<Cosmetic, Boolean>());
        for(Map.Entry<Player, Set<Cosmetic>> entry : this.cosmetics.entrySet()) {
            for(Cosmetic cosmetic : entry.getValue()) {
                if(types.contains(cosmetic.getCosmeticType())) cosmetics.add(cosmetic);
            }
        }
        return cosmetics;
    }
}
