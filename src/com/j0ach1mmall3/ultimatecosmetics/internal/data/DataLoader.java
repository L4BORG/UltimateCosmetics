package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.Cache;
import com.j0ach1mmall3.jlib.storage.Storage;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public interface DataLoader {

    Cache<Map<String, Integer>> getCache();

    void giveAmmo(String identifier, Player player, int amount);

    void takeAmmo(String identifier, Player player, int amount);

    void giveBackQueue(Player p);

    void updateQueue(String uuid, CosmeticsQueue queue);

    void createQueue(Player p);

    void getStacker(Player p, CallbackHandler<Boolean> callbackHandler);

    void createStacker(Player p);

    void setStacker(Player p, boolean stacker);

    void getPetName(Player p, CallbackHandler<String> callbackHandler);

    void createPetName(Player p);

    void setPetName(Player p, String name);

    void disconnectLoader();

    Storage getStorage();
}
