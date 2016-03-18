package com.j0ach1mmall3.ultimatecosmetics.data;

import com.j0ach1mmall3.jlib.storage.Cache;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.mongodb.MongoDBLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.BSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class MongoDBDataLoader extends MongoDBLoader implements DataLoader {
    private final Cache<Map<String, Integer>> cache = new Cache<Map<String, Integer>>() {
        @Override
        public void getOffline(String s, final CallbackHandler<Map<String, Integer>> callbackHandler) {
            MongoDBDataLoader.this.mongoDB.getObject(new BasicDBObject("player", s), MongoDBDataLoader.this.ammoName, new CallbackHandler<DBObject>() {
                @Override
                public void callback(DBObject o) {
                    Map<String, Integer> map = ((Main) MongoDBDataLoader.this.storage.getPlugin()).getDefaultAmmo();
                    if(o != null) {
                        DBObject ammoObject = (DBObject) o.get("ammo");
                        if(ammoObject != null) {
                            for(String s : new HashSet<>(map.keySet())){
                                map.put(s, (int) ammoObject.get(s.toLowerCase()));
                            }
                        }
                    }
                    callbackHandler.callback(map);
                }
            });
        }

        @Override
        public void setOffline(final String s, final Map<String, Integer> c) {
            if(c == null) return;
            MongoDBDataLoader.this.mongoDB.getObject(new BasicDBObject("player", s), MongoDBDataLoader.this.ammoName, new CallbackHandler<DBObject>() {
                @Override
                public void callback(DBObject o) {
                    if (o == null) createOffline(s);
                    else {
                        BSONObject ammoObject = (BSONObject) o.get("ammo");
                        for(Map.Entry<String, Integer> entry : c.entrySet()) {
                            ammoObject.put(entry.getKey().toLowerCase(), entry.getValue());
                        }
                        o.put("ammo", ammoObject);
                        MongoDBDataLoader.this.mongoDB.updateObject(o, new BasicDBObject("player", s), MongoDBDataLoader.this.ammoName);
                    }
                }
            });
        }

        @Override
        public void existsOffline(String s, final CallbackHandler<Boolean> callbackHandler) {
            MongoDBDataLoader.this.mongoDB.getObject(new BasicDBObject("player", s), MongoDBDataLoader.this.ammoName, new CallbackHandler<DBObject>() {
                @Override
                public void callback(DBObject o) {
                    callbackHandler.callback(o != null);
                }
            });
        }

        @Override
        public Map<String, Integer> createOffline(String s) {
            Map<String, Integer> map = ((Main) MongoDBDataLoader.this.storage.getPlugin()).getDefaultAmmo();
            DBObject object = new BasicDBObject("player", s);
            BSONObject ammoObject = new BasicDBObject();
            for (String gadget : ((Main) MongoDBDataLoader.this.storage.getPlugin()).getDefaultAmmo().keySet()) {
                ammoObject.put(gadget, 0);
            }
            object.put("ammo", ammoObject);
            MongoDBDataLoader.this.mongoDB.storeObject(object, MongoDBDataLoader.this.ammoName);
            return map;
        }
    };

    private final String ammoName;
    private final String queueName;
    private final String stackerName;
    private final String petNamesName;

    public MongoDBDataLoader(Main plugin) {
        super(plugin, plugin.getStorage().getDatabaseHost(), plugin.getStorage().getDatabasePort(), plugin.getStorage().getDatabaseDatabase(), plugin.getStorage().getDatabaseUser(), plugin.getStorage().getDatabasePassword());
        this.ammoName = plugin.getStorage().getDatabasePrefix() + "ammo";
        this.queueName = plugin.getStorage().getDatabasePrefix() + "queue_new";
        this.stackerName = plugin.getStorage().getDatabasePrefix() + "stacker";
        this.petNamesName = plugin.getStorage().getDatabasePrefix() + "petnames";
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
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.queueName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject o) {
                if (o != null) {
                    final CosmeticsQueue queue =  new CosmeticsQueue((Main) MongoDBDataLoader.this.storage.getPlugin(), (String) o.get("queue"));
                    Bukkit.getScheduler().callSyncMethod(MongoDBDataLoader.this.storage.getPlugin(), new Callable<Void>() {
                        @Override
                        public Void call() {
                            queue.giveBack(p);
                            return null;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void updateQueue(final String uuid, final CosmeticsQueue queue) {
        this.mongoDB.getObject(new BasicDBObject("player", uuid), this.queueName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject o) {
                o.put("o", queue.asString());
                MongoDBDataLoader.this.mongoDB.updateObject(o, new BasicDBObject("player", uuid), MongoDBDataLoader.this.queueName);
            }
        });
    }

    @Override
    public void createQueue(final Player p) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.queueName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject o) {
                if(o == null) {
                    DBObject object = new BasicDBObject("player", p.getUniqueId().toString());
                    object.put("queue", "");
                    MongoDBDataLoader.this.mongoDB.storeObject(object, MongoDBDataLoader.this.queueName);
                }
            }
        });
    }

    @Override
    public void getStacker(Player p, final CallbackHandler<Boolean> callbackHandler) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.stackerName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject o) {
                callbackHandler.callback((boolean) o.get("Stacker"));
            }
        });
    }

    @Override
    public void setStacker(Player p, boolean stacker) {
        DBObject object = new BasicDBObject("player", p.getUniqueId().toString());
        object.put("Stacker", stacker);
        this.mongoDB.updateObject(object, new BasicDBObject("player", p.getUniqueId().toString()), this.stackerName);
    }

    @Override
    public void createStacker(final Player p) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.stackerName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject o) {
                if (o == null) {
                    DBObject object = new BasicDBObject("player", p.getUniqueId().toString());
                    object.put("Stacker", true);
                    MongoDBDataLoader.this.mongoDB.storeObject(object, MongoDBDataLoader.this.stackerName);
                }
            }
        });
    }

    @Override
    public void getPetName(Player p, final CallbackHandler<String> callbackHandler) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.petNamesName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject o) {
                callbackHandler.callback((String) o.get("PetName"));
            }
        });
    }

    @Override
    public void createPetName(final Player p) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.stackerName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject o) {
                if (o == null) {
                    DBObject object = new BasicDBObject("player", p.getUniqueId().toString());
                    object.put("PetName", "");
                    MongoDBDataLoader.this.mongoDB.storeObject(object, MongoDBDataLoader.this.stackerName);
                }
            }
        });
    }

    @Override
    public void setPetName(Player p, String name) {
        DBObject object = new BasicDBObject("player", p.getUniqueId().toString());
        object.put("PetName", name);
        this.mongoDB.updateObject(object, new BasicDBObject("player", p.getUniqueId().toString()), this.stackerName);
    }
}