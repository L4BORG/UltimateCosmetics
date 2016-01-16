package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class FileDataLoader extends ConfigLoader implements DataLoader {
    private final Map<Player, Map<String, Integer>> ammo = new HashMap<>();


    public FileDataLoader(Main plugin) {
        super("data.yml", plugin);
    }

    @Override
    public void disconnectLoader() {
        // NOP
    }

    @Override
    public void loadAmmo(final Player player) {
        createAmmo(player.getUniqueId().toString());
        getOfflineAmmo(player.getUniqueId().toString(), new CallbackHandler<Map<String, Integer>>() {
            @Override
            public void callback(Map<String, Integer> map) {
                FileDataLoader.this.ammo.put(player, map);
            }
        });
    }

    @Override
    public void unloadAmmo(Player player) {
        if(this.ammo.containsKey(player)) setOfflineAmmo(player.getUniqueId().toString(), this.ammo.get(player));
        else createAmmo(player.getUniqueId().toString());
        this.ammo.remove(player);
    }

    @Override
    public Map<String, Integer> getAmmo(Player player) {
        return this.ammo.get(player);
    }

    @Override
    public void giveAmmo(String identifier, Player player, int amount) {
        setAmmo(identifier, player, getAmmo(player).get(identifier) + amount);
    }

    @Override
    public void takeAmmo(String identifier, Player player, int amount) {
        setAmmo(identifier, player, getAmmo(player).get(identifier) - amount);
    }

    @Override
    public void setAmmo(String identifier, Player player, int amount) {
        Map<String, Integer> map = this.ammo.get(player);
        map.put(identifier, amount);
        this.ammo.put(player, map);
    }

    @Override
    public void getOfflineAmmo(String uuid, CallbackHandler<Map<String, Integer>> callbackHandler) {
        Map<String, Integer> map = new HashMap<>();
        for (GadgetStorage gadget : ((Main) this.storage.getPlugin()).getGadgets().getGadgets()) {
            map.put(gadget.getIdentifier(), this.config.getInt("Ammo." + uuid + '.' + gadget.getIdentifier()));
        }
        callbackHandler.callback(map);
    }

    @Override
    public void giveOfflineAmmo(final String identifier, final String uuid, final int amount) {
        getOfflineAmmo(uuid, new CallbackHandler<Map<String, Integer>>() {
            @Override
            public void callback(Map<String, Integer> map) {
                map.put(identifier, map.get(identifier) + amount);
                setOfflineAmmo(uuid, map);
            }
        });
    }

    @Override
    public void takeOfflineAmmo(final String identifier, final String uuid, final int amount) {
        getOfflineAmmo(uuid, new CallbackHandler<Map<String, Integer>>() {
            @Override
            public void callback(Map<String, Integer> map) {
                map.put(identifier, map.get(identifier) - amount);
                setOfflineAmmo(uuid, map);
            }
        });
    }

    @Override
    public void setOfflineAmmo(String uuid, Map<String, Integer> map) {
        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            this.config.set("Ammo." + uuid + '.' + entry.getKey(), entry.getValue());
        }
        this.customConfig.saveConfig(this.config);
    }

    private void createAmmo(String uuid) {
        if(this.customConfig.getKeys("Ammo") == null || !this.customConfig.getKeys("Ammo").contains(uuid)) {
            Map<String, Integer> map = new HashMap<>();
            for (GadgetStorage gadget : ((Main) this.storage.getPlugin()).getGadgets().getGadgets()) {
                map.put(gadget.getIdentifier(), 0);
            }
            setOfflineAmmo(uuid, map);
        }
    }

    @Override
    public void giveBackQueue(Player p) {
        if(this.customConfig.getKeys("Queue") != null && this.customConfig.getKeys("Queue").contains(p.getUniqueId().toString())) new CosmeticsQueue((Main) this.storage.getPlugin(), this.config.getStringList("Queue." + p.getUniqueId())).give(p);
    }

    @Override
    public void updateQueue(String uuid, CosmeticsQueue queue) {
        this.config.set("Queue." + uuid, queue.asList());
        this.customConfig.saveConfig(this.config);
    }

    @Override
    public void createQueue(Player p) {
        if(this.customConfig.getKeys("Queue") == null || !this.customConfig.getKeys("Queue").contains(p.getUniqueId().toString())) {
            this.config.set("Queue." + p.getUniqueId(), new ArrayList<>());
            this.customConfig.saveConfig(this.config);
        }
    }


    @Override
    public void getStacker(Player p, CallbackHandler<Boolean> callbackHandler) {
        callbackHandler.callback(this.config.getBoolean("Stacker." + p.getUniqueId()));
    }

    @Override
    public void setStacker(Player p, boolean stacker) {
        this.config.set("Stacker." + p.getUniqueId(), stacker);
        this.customConfig.saveConfig(this.config);
    }

    @Override
    public void createStacker(Player p) {
        if(this.customConfig.getKeys("Stacker") == null || !this.customConfig.getKeys("Stacker").contains(p.getUniqueId().toString())) {
            this.config.set("Stacker." + p.getUniqueId(), true);
            this.customConfig.saveConfig(this.config);
        }
    }

    @Override
    public void getPetName(Player p, CallbackHandler<String> callbackHandler) {
        callbackHandler.callback(this.config.getString("PetNames." + p.getUniqueId()));
    }

    @Override
    public void createPetName(Player p) {
        if(this.customConfig.getKeys("PetNames") == null || !this.customConfig.getKeys("PetNames").contains(p.getUniqueId().toString())) {
            this.config.set("PetNames." + p.getUniqueId(), "");
            this.customConfig.saveConfig(this.config);
        }
    }

    @Override
    public void setPetName(Player p, String name) {
        this.config.set("PetNames." + p.getUniqueId(), name.replace(".", ""));
    }
}
