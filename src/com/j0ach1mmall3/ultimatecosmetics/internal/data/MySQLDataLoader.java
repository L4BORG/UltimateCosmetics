package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.Cache;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.mysql.MySQLLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class MySQLDataLoader extends MySQLLoader implements DataLoader {
    private final Cache<Map<String, Integer>> cache = new Cache<Map<String, Integer>>() {
        @Override
        public void getOffline(String player, final CallbackHandler<Map<String, Integer>> callbackHandler) {
            Map<Integer, Object> params = new HashMap<>();
            params.put(1, player);
            Map<String, Class> columns = new HashMap<>();
            for (GadgetStorage gadget : ((Main) MySQLDataLoader.this.storage.getPlugin()).getGadgets().getGadgets()) {
                columns.put(gadget.getIdentifier(), String.class);
            }
            MySQLDataLoader.this.mySQL.executeQuery("SELECT * FROM " + MySQLDataLoader.this.ammoName + " WHERE Player = ?", params, columns, new CallbackHandler<List<Map<String, Object>>>() {
                @Override
                public void callback(List<Map<String, Object>> results) {
                    Map<String, Integer> map = new HashMap<>();
                    if(results.isEmpty()) map = ((Main) MySQLDataLoader.this.storage.getPlugin()).getDefaultAmmo();
                    else {
                        for (GadgetStorage gadget : ((Main) MySQLDataLoader.this.storage.getPlugin()).getGadgets().getGadgets()) {
                            map.put(gadget.getIdentifier(), (Integer) results.get(0).get(gadget.getIdentifier()));
                        }
                    }
                    callbackHandler.callback(map);
                }
            });
        }

        @Override
        public void setOffline(final String player, final Map<String, Integer> map) {
            this.existsOffline(player, new CallbackHandler<Boolean>() {
                @Override
                public void callback(Boolean b) {
                    if(b) {
                        for(Map.Entry<String, Integer> entry : map.entrySet()) {
                            Map<Integer, Object> params = new HashMap<>();
                            params.put(1, entry.getKey());
                            params.put(2, String.valueOf(entry.getValue()));
                            params.put(3, player);
                            MySQLDataLoader.this.mySQL.execute("UPDATE " + MySQLDataLoader.this.ammoName + " SET ?=? WHERE Player=?", params);
                        }
                    }
                }
            });
        }

        @Override
        public void existsOffline(String player, CallbackHandler<Boolean> callbackHandler) {
            Map<Integer, Object> params = new HashMap<>();
            params.put(1, player);
            MySQLDataLoader.this.mySQL.hasResultSetNext("SELECT * FROM " + MySQLDataLoader.this.ammoName + " WHERE Player = ?", params, callbackHandler);
        }

        @Override
        public Map<String, Integer> createOffline(String player) {
            Map<Integer, Object> params = new HashMap<>();
            params.put(1, player);
            MySQLDataLoader.this.mySQL.execute("INSERT INTO " + MySQLDataLoader.this.ammoName + " VALUES(?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)", params);
            return ((Main) MySQLDataLoader.this.storage.getPlugin()).getDefaultAmmo();
        }
    };


    private final String ammoName;
    private final String queueName;
    private final String stackerName;
    private final String petNamesName;

    public MySQLDataLoader(Main plugin) {
        super(plugin, plugin.getStorage().getDatabaseHost(), plugin.getStorage().getDatabasePort(), plugin.getStorage().getDatabaseDatabase(), plugin.getStorage().getDatabaseUser(), plugin.getStorage().getDatabasePassword());
        this.ammoName = plugin.getStorage().getDatabasePrefix() + "ammo";
        this.queueName = plugin.getStorage().getDatabasePrefix() + "queue";
        this.stackerName = plugin.getStorage().getDatabasePrefix() + "stacker";
        this.petNamesName = plugin.getStorage().getDatabasePrefix() + "petnames";
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.ammoName + "(Player VARCHAR(36), Enderbow INT(5), EtherealPearl INT(5), PaintballGun INT(5), FlyingPig INT(5), BatBlaster INT(5), CATapult INT(5), RailGun INT(5), CryoTube INT(5), Rocket INT(5), PoopBomb INT(5), GrapplingHook INT(5), SelfDestruct INT(5), SlimeVasion INT(5), FunGun INT(5), MelonThrower INT(5), ColorBomb INT(5), FireTrail INT(5), DiamondShower INT(5), GoldFountain INT(5), PaintTrail INT(5))", new HashMap<Integer, Object>());
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.queueName + "(Player VARCHAR(36), Balloon VARCHAR(64), Banner VARCHAR(64), Bowtrail VARCHAR(64), Gadget VARCHAR(64), Hat VARCHAR(64), Hearts VARCHAR(64), Morph VARCHAR(64), Mount VARCHAR(64), Music VARCHAR(64), Particles VARCHAR(64), Pet VARCHAR(64), Trail VARCHAR(64), Outfit VARCHAR(64))", new HashMap<Integer, Object>());
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.stackerName + "(Player VARCHAR(36), Enabled TINYINT(1))", new HashMap<Integer, Object>());
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.petNamesName + "(Player VARCHAR(36), PetName VARCHAR(64))", new HashMap<Integer, Object>());
    }

    @Override
    public void disconnectLoader() {
        disconnect();
    }

    @Override
    public Cache<Map<String, Integer>> getCache() {
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
    public void giveBackQueue(final Player p) {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, p.getUniqueId().toString());
        Map<String, Class> columns = new HashMap<>();
        columns.put("Balloon", String.class);
        columns.put("Banner", String.class);
        columns.put("Bowtrail", String.class);
        columns.put("Gadget", String.class);
        columns.put("Hat", String.class);
        columns.put("Hearts", String.class);
        columns.put("Morph", String.class);
        columns.put("Mount", String.class);
        columns.put("Music", String.class);
        columns.put("Particles", String.class);
        columns.put("Pet", String.class);
        columns.put("Trail", String.class);
        columns.put("Outfit", String.class);
        this.mySQL.executeQuery("SELECT * FROM " + this.queueName + " WHERE Player = ?", params, columns, new CallbackHandler<List<Map<String, Object>>>() {
            @Override
            public void callback(List<Map<String, Object>> results) {
                if(!results.isEmpty()) {
                    final CosmeticsQueue queue = new CosmeticsQueue((Main) MySQLDataLoader.this.storage.getPlugin(), Arrays.asList(
                            (String) results.get(0).get("Balloon"),
                            (String) results.get(0).get("Banner"),
                            (String) results.get(0).get("Bowtrail"),
                            (String) results.get(0).get("Gadget"),
                            (String) results.get(0).get("Hat"),
                            (String) results.get(0).get("Hearts"),
                            (String) results.get(0).get("Morph"),
                            (String) results.get(0).get("Mount"),
                            (String) results.get(0).get("Music"),
                            (String) results.get(0).get("Particles"),
                            (String) results.get(0).get("Pet"),
                            (String) results.get(0).get("Trail"),
                            (String) results.get(0).get("Outfit")));
                    Bukkit.getScheduler().callSyncMethod(MySQLDataLoader.this.storage.getPlugin(), new Callable<Void>() {
                        @Override
                        public Void call() {
                            queue.give(p);
                            return null;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void updateQueue(String uuid, CosmeticsQueue queue) {
        List<String> list = queue.asList();
        Map<Integer, Object> params = new HashMap<>();
        for(int i=0;i<13;i++) {
            params.put(i+1, list.get(i));
        }
        params.put(14, uuid);
        this.mySQL.execute("UPDATE " + this.queueName + " SET Balloon=?, Banner=?, Bowtrail=?, Gadget=?, Hat=?, Hearts=?, Morph=?, Mount=?, Music=?, Particles=?, Pet=?, Trail=?, Outfit=? WHERE Player=?", params);
    }

    @Override
    public void createQueue(Player p) {
        final Map<Integer, Object> params = new HashMap<>();
        params.put(1, p.getUniqueId().toString());
        this.mySQL.hasResultSetNext("SELECT * FROM " + this.queueName + " WHERE Player = ?", params, new CallbackHandler<Boolean>() {
            @Override
            public void callback(Boolean b) {
                if(!b) MySQLDataLoader.this.mySQL.execute("INSERT INTO " + MySQLDataLoader.this.queueName + " VALUES(?, '', '', '', '', '', '', '', '', '', '', '', '', '')", params);
            }
        });
    }

    @Override
    public void getStacker(Player p, final CallbackHandler<Boolean> callbackHandler) {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, p.getUniqueId().toString());
        Map<String, Class> columns = new HashMap<>();
        columns.put("Enabled", Boolean.class);
        this.mySQL.executeQuery("SELECT * FROM " + this.stackerName + " WHERE Player = ?", params, columns, new CallbackHandler<List<Map<String, Object>>>() {
            @Override
            public void callback(List<Map<String, Object>> results) {
                if(results.isEmpty()) callbackHandler.callback(false);
                else callbackHandler.callback((Boolean) results.get(0).get("Enabled"));
            }
        });
    }

    @Override
    public void setStacker(Player p, boolean stacker) {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, stacker);
        params.put(2, p.getUniqueId().toString());
        this.mySQL.execute("UPDATE " + this.stackerName + " SET Enabled=? WHERE Player=?", params);
    }

    @Override
    public void createStacker(Player p) {
        final Map<Integer, Object> params = new HashMap<>();
        params.put(1, p.getUniqueId().toString());
        this.mySQL.hasResultSetNext("SELECT * FROM " + this.stackerName + " WHERE Player = ?", params, new CallbackHandler<Boolean>() {
            @Override
            public void callback(Boolean b) {
                if(!b) MySQLDataLoader.this.mySQL.execute("INSERT INTO " + MySQLDataLoader.this.stackerName + " VALUES(?, 1)", params);
            }
        });
    }

    @Override
    public void getPetName(Player p, final CallbackHandler<String> callbackHandler) {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, p.getUniqueId().toString());
        Map<String, Class> columns = new HashMap<>();
        columns.put("PetName", String.class);
        this.mySQL.executeQuery("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", params, columns, new CallbackHandler<List<Map<String, Object>>>() {
            @Override
            public void callback(List<Map<String, Object>> results) {
                if(results.isEmpty()) callbackHandler.callback("");
                else callbackHandler.callback((String) results.get(0).get("PetName"));
            }
        });
    }

    @Override
    public void createPetName(Player p) {
        final Map<Integer, Object> params = new HashMap<>();
        params.put(1, p.getUniqueId().toString());
        this.mySQL.hasResultSetNext("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", params, new CallbackHandler<Boolean>() {
            @Override
            public void callback(Boolean b) {
                if (!b) MySQLDataLoader.this.mySQL.execute("INSERT INTO " + MySQLDataLoader.this.petNamesName + " VALUES(?, '')", params);
            }
        });
    }

    @Override
    public void setPetName(final Player p, final String name) {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, p.getUniqueId().toString());
        this.mySQL.hasResultSetNext("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", params, new CallbackHandler<Boolean>() {
            @Override
            public void callback(Boolean b) {
                if(b) {
                    Map<Integer, Object> params = new HashMap<>();
                    params.put(1, name);
                    params.put(2, p.getUniqueId().toString());
                    MySQLDataLoader.this.mySQL.execute("UPDATE " + MySQLDataLoader.this.petNamesName + " SET PetName = ? WHERE Player = ?", params);
                } else {
                    Map<Integer, Object> params = new HashMap<>();
                    params.put(1, p.getUniqueId().toString());
                    params.put(2, name);
                    MySQLDataLoader.this.mySQL.execute("INSERT INTO " + MySQLDataLoader.this.petNamesName + " VALUES(?, ?)", params);
                }
            }
        });
    }
}
