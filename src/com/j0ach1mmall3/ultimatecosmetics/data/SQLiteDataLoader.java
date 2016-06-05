package com.j0ach1mmall3.ultimatecosmetics.data;

import com.j0ach1mmall3.jlib.storage.Cache;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.sqlite.SQLiteLoader;
import com.j0ach1mmall3.jlib.storage.database.wrapped.WrappedParameters;
import com.j0ach1mmall3.jlib.storage.database.wrapped.WrappedResultSet;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class SQLiteDataLoader extends SQLiteLoader<Main> implements DataLoader {
    private final Cache<Map<String, Integer>> cache = new Cache<Map<String, Integer>>() {
        @Override
        public void getOffline(String s, final CallbackHandler<Map<String, Integer>> callbackHandler) {
            WrappedParameters params = new WrappedParameters();
            params.addParameter(1, s);
            SQLiteDataLoader.this.sqLite.executeQuery("SELECT * FROM " + SQLiteDataLoader.this.ammoName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
                @Override
                public void callback(WrappedResultSet o) {
                    Map<String, Integer> map = new HashMap<>(Methods.DEFAULT_AMMO);
                    if(o.next()) {
                        for(String s : new HashSet<>(map.keySet())) {
                            map.put(s, (Integer) o.get(s));
                        }
                    }
                    callbackHandler.callback(map);
                }
            });
        }

        @Override
        public void setOffline(final String s, final Map<String, Integer> c) {
            if(c == null) return;
            this.existsOffline(s, new CallbackHandler<Boolean>() {
                @Override
                public void callback(Boolean o) {
                    if(o) {
                        for(Map.Entry<String, Integer> entry : c.entrySet()) {
                            WrappedParameters params = new WrappedParameters();
                            params.addParameter(1, String.valueOf(entry.getValue()));
                            params.addParameter(2, s);
                            SQLiteDataLoader.this.sqLite.execute("UPDATE " + SQLiteDataLoader.this.ammoName + " SET " + entry.getKey() + "=? WHERE Player=?", params);
                        }
                    } else createOffline(s);
                }
            });
        }

        @Override
        public void existsOffline(String s, final CallbackHandler<Boolean> callbackHandler) {
            WrappedParameters params = new WrappedParameters();
            params.addParameter(1, s);
            SQLiteDataLoader.this.sqLite.executeQuery("SELECT * FROM " + SQLiteDataLoader.this.ammoName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
                @Override
                public void callback(WrappedResultSet o) {
                    callbackHandler.callback(o.next());
                }
            });
        }

        @Override
        public Map<String, Integer> createOffline(String s) {
            WrappedParameters params = new WrappedParameters();
            params.addParameter(1, s);
            SQLiteDataLoader.this.sqLite.execute("INSERT INTO " + SQLiteDataLoader.this.ammoName + " VALUES(?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)", params);
            return new HashMap<>(Methods.DEFAULT_AMMO);
        }
    };


    private final String ammoName;
    private final String queueName;
    private final String stackerName;
    private final String petNamesName;

    public SQLiteDataLoader(Main plugin) {
        super(plugin, "data");
        this.ammoName = plugin.getStorage().getDatabasePrefix() + "ammo";
        this.queueName = plugin.getStorage().getDatabasePrefix() + "queue_new";
        this.stackerName = plugin.getStorage().getDatabasePrefix() + "stacker";
        this.petNamesName = plugin.getStorage().getDatabasePrefix() + "petnames";
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " + this.ammoName + "(Player VARCHAR(36), Enderbow INT(5), EtherealPearl INT(5), PaintballGun INT(5), FlyingPig INT(5), BatBlaster INT(5), CATapult INT(5), RailGun INT(5), CryoTube INT(5), Rocket INT(5), PoopBomb INT(5), GrapplingHook INT(5), SelfDestruct INT(5), SlimeVasion INT(5), FunGun INT(5), MelonThrower INT(5), ColorBomb INT(5), FireTrail INT(5), DiamondShower INT(5), GoldFountain INT(5), PaintTrail INT(5))", new WrappedParameters());
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " + this.queueName + "(Player VARCHAR(36), Queue VARCHAR(1024))", new WrappedParameters());
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " + this.stackerName + "(Player VARCHAR(36), Enabled TINYINT(1))", new WrappedParameters());
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " + this.petNamesName + "(Player VARCHAR(36), PetName VARCHAR(64))", new WrappedParameters());
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
    public void giveBackQueue(final Player p) {
        WrappedParameters params = new WrappedParameters();
        params.addParameter(1, p.getUniqueId().toString());
        this.sqLite.executeQuery("SELECT * FROM " + this.queueName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
            @Override
            public void callback(final WrappedResultSet o) {
                if(o.next()) {
                    Bukkit.getScheduler().callSyncMethod(SQLiteDataLoader.this.storage.getPlugin(), new Callable<Void>() {
                        @Override
                        public Void call() {
                            new CosmeticsQueue(SQLiteDataLoader.this.storage.getPlugin(), (String) o.get("Queue")).giveBack(p);
                            return null;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void updateQueue(String uuid, CosmeticsQueue queue) {
        String s = queue.asString();
        WrappedParameters params = new WrappedParameters();
        params.addParameter(1, s);
        params.addParameter(2, uuid);
        this.sqLite.execute("UPDATE " + this.queueName + " SET Queue=? WHERE Player=?", params);
    }

    @Override
    public void createQueue(Player p) {
        final WrappedParameters params = new WrappedParameters();
        params.addParameter(1, p.getUniqueId().toString());
        this.sqLite.executeQuery("SELECT * FROM " + this.queueName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
            @Override
            public void callback(WrappedResultSet o) {
                if(!o.next()) SQLiteDataLoader.this.sqLite.execute("INSERT INTO " + SQLiteDataLoader.this.queueName + " VALUES(?, ?)", params);
            }
        });
    }

    @Override
    public void getStacker(Player p, final CallbackHandler<Boolean> callbackHandler) {
        WrappedParameters params = new WrappedParameters();
        params.addParameter(1, p.getUniqueId().toString());
        this.sqLite.executeQuery("SELECT * FROM " + this.stackerName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
            @Override
            public void callback(WrappedResultSet o) {
                if(o.next()) callbackHandler.callback((boolean) o.get("Enabled"));
                else callbackHandler.callback(false);
            }
        });
    }

    @Override
    public void setStacker(Player p, boolean stacker) {
        WrappedParameters params = new WrappedParameters();
        params.addParameter(1, stacker);
        params.addParameter(2, p.getUniqueId().toString());
        this.sqLite.execute("UPDATE " + this.stackerName + " SET Enabled=? WHERE Player=?", params);
    }

    @Override
    public void createStacker(Player p) {
        final WrappedParameters params = new WrappedParameters();
        params.addParameter(1, p.getUniqueId().toString());
        this.sqLite.executeQuery("SELECT * FROM " + this.stackerName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
            @Override
            public void callback(WrappedResultSet o) {
                if(!o.next()) SQLiteDataLoader.this.sqLite.execute("INSERT INTO " + SQLiteDataLoader.this.stackerName + " VALUES(?, 1)", params);
            }
        });
    }

    @Override
    public void getPetName(Player p, final CallbackHandler<String> callbackHandler) {
        WrappedParameters params = new WrappedParameters();
        params.addParameter(1, p.getUniqueId().toString());
        this.sqLite.executeQuery("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
            @Override
            public void callback(WrappedResultSet o) {
                if(o.next()) callbackHandler.callback((String) o.get("PetName"));
                else callbackHandler.callback("");
            }
        });
    }

    @Override
    public void createPetName(Player p) {
        final WrappedParameters params = new WrappedParameters();
        params.addParameter(1, p.getUniqueId().toString());
        this.sqLite.executeQuery("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
            @Override
            public void callback(WrappedResultSet o) {
                if (!o.next()) SQLiteDataLoader.this.sqLite.execute("INSERT INTO " + SQLiteDataLoader.this.petNamesName + " VALUES(?, '')", params);
            }
        });
    }

    @Override
    public void setPetName(final Player p, final String name) {
        WrappedParameters params = new WrappedParameters();
        params.addParameter(1, p.getUniqueId().toString());
        this.sqLite.executeQuery("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", params, new CallbackHandler<WrappedResultSet>() {
            @Override
            public void callback(WrappedResultSet o) {
                if(o.next()) {
                    WrappedParameters params = new WrappedParameters();
                    params.addParameter(1, name);
                    params.addParameter(2, p.getUniqueId().toString());
                    SQLiteDataLoader.this.sqLite.execute("UPDATE " + SQLiteDataLoader.this.petNamesName + " SET PetName = ? WHERE Player = ?", params);
                } else {
                    WrappedParameters params = new WrappedParameters();
                    params.addParameter(1, p.getUniqueId().toString());
                    params.addParameter(2, name);
                    SQLiteDataLoader.this.sqLite.execute("INSERT INTO " + SQLiteDataLoader.this.petNamesName + " VALUES(?, ?)", params);
                }
            }
        });
    }
}

