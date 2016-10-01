package com.j0ach1mmall3.ultimatecosmetics.data;

import com.j0ach1mmall3.jlib.storage.Cache;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class FileDataLoader extends ConfigLoader<Main> implements DataLoader {
    private final Cache<Map<String, Integer>> cache = new Cache<Map<String, Integer>>() {
        @Override
        public void getOffline(String s, CallbackHandler<Map<String, Integer>> callbackHandler) {
            Map<String, Integer> map = new HashMap<>(Methods.DEFAULT_AMMO);
            for (String gadget : map.keySet()) {
                map.put(gadget, FileDataLoader.this.config.getInt("Ammo." + s + '.' + gadget));
            }
            callbackHandler.callback(map);
        }

        @Override
        public void setOffline(String s, Map<String, Integer> c) {
            if(c == null) return;
            for(Map.Entry<String, Integer> entry : c.entrySet()) {
                FileDataLoader.this.config.set("Ammo." + s + '.' + entry.getKey(), entry.getValue());
            }
            FileDataLoader.this.storage.saveConfig(FileDataLoader.this.config);
        }

        @Override
        public void existsOffline(String s, CallbackHandler<Boolean> callbackHandler) {
            callbackHandler.callback(FileDataLoader.this.storage.getKeys("Ammo").contains(s));
        }

        @Override
        public Map<String, Integer> createOffline(String s) {
            Map<String, Integer> map = new HashMap<>(Methods.DEFAULT_AMMO);
            setOffline(s, map);
            return map;
        }
    };

    public FileDataLoader(Main plugin) {
        super("data.yml", plugin);
    }

    @Override
    public void disconnectLoader() {
        this.disconnect();
    }

    @Override
    public Cache<Map<String, Integer>> getAmmoCache() {
        return this.cache;
    }

    @Override
    public void giveAmmo(String identifier, Player player, int amount) {
        Map<String, Integer> ammo = this.cache.get(player);
        ammo.put(identifier, ammo.get(identifier) + amount);
        this.cache.set(player, ammo);
    }

    @Override
    public void takeAmmo(String identifier, Player player, int amount) {
        Map<String, Integer> ammo = this.cache.get(player);
        ammo.put(identifier, ammo.get(identifier) - amount);
        this.cache.set(player, ammo);
    }

    @Override
    public void giveBackQueue(Player p) {
        new CosmeticsQueue(this.storage.getPlugin(), this.config.getString("NewQueue." + p.getUniqueId())).giveBack(p);
    }

    @Override
    public void createQueue(Player p) {
        if(this.storage.getKeys("NewQueue") == null || !this.storage.getKeys("NewQueue").contains(p.getUniqueId().toString())) {
            this.config.set("NewQueue." + p.getUniqueId(), "");
            this.storage.saveConfig(this.config);
        }
    }

    @Override
    public void updateQueue(String uuid, CosmeticsQueue queue) {
        this.config.set("NewQueue." + uuid, queue.asString());
        this.storage.saveConfig(this.config);
    }

    @Override
    public void getStacker(Player p, CallbackHandler<Boolean> callbackHandler) {
        callbackHandler.callback(this.config.getBoolean("Stacker." + p.getUniqueId()));
    }

    @Override
    public void setStacker(Player p, boolean stacker) {
        this.config.set("Stacker." + p.getUniqueId(), stacker);
        this.storage.saveConfig(this.config);
    }

    @Override
    public void createStacker(Player p) {
        if(this.storage.getKeys("Stacker") == null || !this.storage.getKeys("Stacker").contains(p.getUniqueId().toString())) {
            this.config.set("Stacker." + p.getUniqueId(), true);
            this.storage.saveConfig(this.config);
        }
    }

    @Override
    public void getPetName(Player p, CallbackHandler<String> callbackHandler) {
        callbackHandler.callback(this.config.getString("PetNames." + p.getUniqueId()));
    }

    @Override
    public void createPetName(Player p) {
        if(this.storage.getKeys("PetNames") == null || !this.storage.getKeys("PetNames").contains(p.getUniqueId().toString())) {
            this.config.set("PetNames." + p.getUniqueId(), "");
            this.storage.saveConfig(this.config);
        }
    }

    @Override
    public void setPetName(Player p, String name) {
        this.config.set("PetNames." + p.getUniqueId(), name.replace(".", ""));
        this.storage.saveConfig(this.config);
    }
}
