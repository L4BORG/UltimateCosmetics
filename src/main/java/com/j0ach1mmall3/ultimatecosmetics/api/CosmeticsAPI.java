package com.j0ach1mmall3.ultimatecosmetics.api;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 17/08/2015
 */
public final class CosmeticsAPI {
    private final Map<Player, Set<Cosmetic>> cosmetics = new HashMap<>();

    void addCosmetic(Player p, Cosmetic cosmetic) {
        Set<Cosmetic> cosmetics = this.getCosmetics(p);
        cosmetics.add(cosmetic);
        this.cosmetics.put(p, cosmetics);
    }

    void removeCosmetic(Player p, Cosmetic cosmetic) {
        Set<Cosmetic> cosmetics = this.getCosmetics(p);
        cosmetics.remove(cosmetic);
        this.cosmetics.put(p, cosmetics);
    }

    public boolean hasCosmetics(Player p, Class<? extends Cosmetic> clazz) {
        return !this.getCosmetics(p, clazz).isEmpty();
    }

    public Set<Cosmetic> getCosmetics(Player p) {
        return this.cosmetics.containsKey(p) ? this.cosmetics.get(p) : Collections.newSetFromMap(new ConcurrentHashMap<Cosmetic, Boolean>());
    }

    public <C extends Cosmetic> Set<C> getCosmetics(Player p, Class<C> clazz) {
        Set<C> cosmetics = Collections.newSetFromMap(new ConcurrentHashMap<C, Boolean>());
        for(Cosmetic cosmetic : this.getCosmetics(p)) {
            if(cosmetic.getClass() == clazz) cosmetics.add((C) cosmetic);
        }
        return cosmetics;
    }

    public Set<Cosmetic> getAllCosmetics() {
        Set<Cosmetic> cosmetics = Collections.newSetFromMap(new ConcurrentHashMap<Cosmetic, Boolean>());
        for(Set<Cosmetic> cosmeticz : this.cosmetics.values()) {
            cosmetics.addAll(cosmeticz);
        }
        return cosmetics;
    }

    public <C extends Cosmetic> Set<C> getAllCosmetics(Class<C> clazz) {
        Set<C> cosmetics = Collections.newSetFromMap(new ConcurrentHashMap<C, Boolean>());
        for(Cosmetic cosmetic : this.getAllCosmetics()) {
            if(cosmetic.getClass() == clazz) cosmetics.add((C) cosmetic);
        }
        return cosmetics;
    }
}
