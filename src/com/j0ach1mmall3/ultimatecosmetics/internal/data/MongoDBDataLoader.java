package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.mongodb.MongoDBLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.BSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class MongoDBDataLoader extends MongoDBLoader implements DataLoader {
    private final Map<Player, Map<String, Integer>> ammo = new HashMap<>();
    private final String ammoName;
    private final String queueName;
    private final String stackerName;
    private final String petNamesName;

    public MongoDBDataLoader(Main plugin) {
        super(plugin, plugin.getStorage().getDatabaseHost(), plugin.getStorage().getDatabasePort(), plugin.getStorage().getDatabaseDatabase(), plugin.getStorage().getDatabaseUser(), plugin.getStorage().getDatabasePassword());
        this.ammoName = plugin.getStorage().getDatabasePrefix() + "ammo";
        this.queueName = plugin.getStorage().getDatabasePrefix() + "queue";
        this.stackerName = plugin.getStorage().getDatabasePrefix() + "stacker";
        this.petNamesName = plugin.getStorage().getDatabasePrefix() + "petnames";
    }

    @Override
    public void disconnectLoader() {
        disconnect();
    }

    @Override
    public void loadAmmo(final Player player) {
        createAmmo(player.getUniqueId().toString());
        getOfflineAmmo(player.getUniqueId().toString(), new CallbackHandler<Map<String, Integer>>() {
            @Override
            public void callback(Map<String, Integer> map) {
                MongoDBDataLoader.this.ammo.put(player, map);
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
    public void getOfflineAmmo(String uuid, final CallbackHandler<Map<String, Integer>> callbackHandler) {
        this.mongoDB.getObject(new BasicDBObject("player", uuid), this.ammoName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                Map<String, Integer> map = new HashMap<>();
                if(dbObject == null) {
                    for (GadgetStorage gadget : ((Main) MongoDBDataLoader.this.storage.getPlugin()).getGadgets().getGadgets()) {
                        map.put(gadget.getIdentifier(), 0);
                    }
                } else {
                    DBObject ammoObject = (DBObject) dbObject.get("ammo");
                    if(ammoObject == null) {
                        for (GadgetStorage gadget : ((Main) MongoDBDataLoader.this.storage.getPlugin()).getGadgets().getGadgets()) {
                            map.put(gadget.getIdentifier(), 0);
                        }
                    } else {
                        for (GadgetStorage gadget : ((Main) MongoDBDataLoader.this.storage.getPlugin()).getGadgets().getGadgets()) {
                            map.put(gadget.getIdentifier(), (int) ammoObject.get(gadget.getIdentifier().toLowerCase()));
                        }
                    }
                }
                callbackHandler.callback(map);
            }
        });
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
    public void setOfflineAmmo(final String uuid, final Map<String, Integer> map) {
        this.mongoDB.getObject(new BasicDBObject("player", uuid), this.ammoName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                if (dbObject == null) createAmmo(uuid);
                BSONObject ammoObject = (BSONObject) dbObject.get("ammo");
                for(Map.Entry<String, Integer> entry : map.entrySet()) {
                    ammoObject.put(entry.getKey().toLowerCase(), entry.getValue());
                }
                dbObject.put("ammo", ammoObject);
                MongoDBDataLoader.this.mongoDB.updateObject(dbObject, new BasicDBObject("player", uuid), MongoDBDataLoader.this.ammoName);
            }
        });
    }

    private void createAmmo(final String uuid) {
        this.mongoDB.getObject(new BasicDBObject("player", uuid), this.ammoName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                if(dbObject == null) {
                    DBObject object = new BasicDBObject("player", uuid);
                    BSONObject ammoObject = new BasicDBObject("enderbow", 0);
                    List<GadgetStorage> gadgets = ((Main) MongoDBDataLoader.this.storage.getPlugin()).getGadgets().getGadgets();
                    for (int i = 1;i < gadgets.size();i++) {
                        ammoObject.put(gadgets.get(i).getIdentifier().toLowerCase(), 0);
                    }
                    object.put("ammo", ammoObject);
                    MongoDBDataLoader.this.mongoDB.storeObject(object, MongoDBDataLoader.this.ammoName);
                }
            }
        });
    }

    @Override
    public void giveBackQueue(final Player p) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.queueName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                if (dbObject != null) {
                    DBObject cosmetics = (DBObject) dbObject.get("cosmetics");
                    List<String> values = new ArrayList<>();
                    for(String s : cosmetics.keySet()) {
                        values.add((String) cosmetics.get(s));
                    }
                    final CosmeticsQueue queue =  new CosmeticsQueue((Main) MongoDBDataLoader.this.storage.getPlugin(), values);
                    Bukkit.getScheduler().callSyncMethod(MongoDBDataLoader.this.storage.getPlugin(), new Callable<Void>() {
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
    public void updateQueue(final String uuid, final CosmeticsQueue queue) {
        this.mongoDB.getObject(new BasicDBObject("player", uuid), this.queueName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                BSONObject cosmetics = (BSONObject) dbObject.get("cosmetics");
                List<String> list = queue.asList();
                cosmetics.put("balloon", list.get(0));
                cosmetics.put("banner", list.get(1));
                cosmetics.put("bowtrail", list.get(2));
                cosmetics.put("gadget", list.get(3));
                cosmetics.put("hat", list.get(4));
                cosmetics.put("hearts", list.get(5));
                cosmetics.put("morph", list.get(6));
                cosmetics.put("mount", list.get(7));
                cosmetics.put("music", list.get(8));
                cosmetics.put("particles", list.get(9));
                cosmetics.put("pet", list.get(10));
                cosmetics.put("trail", list.get(11));
                cosmetics.put("outfit", list.get(12));
                dbObject.put("cosmetics", cosmetics);
                MongoDBDataLoader.this.mongoDB.updateObject(dbObject, new BasicDBObject("player", uuid), MongoDBDataLoader.this.queueName);
            }
        });
    }

    @Override
    public void createQueue(final Player p) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.queueName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                if(dbObject == null) {
                    DBObject object = new BasicDBObject("player", p.getUniqueId().toString());
                    BSONObject cosmetics = new BasicDBObject("balloon", "");
                    cosmetics.put("banner", "");
                    cosmetics.put("bowtrail", "");
                    cosmetics.put("gadget", "");
                    cosmetics.put("hat", "");
                    cosmetics.put("hearts", "");
                    cosmetics.put("morph", "");
                    cosmetics.put("mount", "");
                    cosmetics.put("music", "");
                    cosmetics.put("particles", "");
                    cosmetics.put("pet", "");
                    cosmetics.put("trail", "");
                    cosmetics.put("outfit", "");
                    object.put("cosmetics", cosmetics);
                    MongoDBDataLoader.this.mongoDB.storeObject(object, MongoDBDataLoader.this.queueName);
                }
            }
        });
    }

    @Override
    public void getStacker(Player p, final CallbackHandler<Boolean> callbackHandler) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.stackerName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                callbackHandler.callback((boolean) dbObject.get("Stacker"));
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
            public void callback(DBObject dbObject) {
                if (dbObject == null) {
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
            public void callback(DBObject dbObject) {
                callbackHandler.callback((String) dbObject.get("PetName"));
            }
        });
    }

    @Override
    public void createPetName(final Player p) {
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.stackerName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                if (dbObject == null) {
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