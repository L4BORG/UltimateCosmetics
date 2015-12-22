package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.mongodb.MongoDBLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
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
    private final Map<String, Map<String, Integer>> ammo = new HashMap<>();
    private final String ammoName;
    private final String queueName;
    private final String stackerName;

    public MongoDBDataLoader(Main plugin) {
        super(plugin, plugin.getStorage().getDatabaseHost(), plugin.getStorage().getDatabasePort(), plugin.getStorage().getDatabaseDatabase(), plugin.getStorage().getDatabaseUser(), plugin.getStorage().getDatabasePassword());
        this.ammoName = plugin.getStorage().getDatabasePrefix() + "ammo";
        this.queueName = plugin.getStorage().getDatabasePrefix() + "queue";
        this.stackerName = plugin.getStorage().getDatabasePrefix() + "stacker";
    }

    @Override
    public void disconnectLoader() {
        disconnect();
    }

    @Override
    public void loadAmmo(final String uuid) {
        final Map<String, Integer> gadgetAmmo = new HashMap<>();
        this.mongoDB.getObject(new BasicDBObject("player", uuid), this.ammoName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                if (dbObject != null) {
                    DBObject ammoObject = (DBObject) dbObject.get("ammo");
                    for (GadgetStorage gadget : ((Main) MongoDBDataLoader.this.plugin).getGadgets().getGadgets()) {
                        gadgetAmmo.put(gadget.getIdentifier(), (int) ammoObject.get(gadget.getIdentifier().toLowerCase()));
                    }
                    MongoDBDataLoader.this.ammo.put(uuid, gadgetAmmo);
                } else {
                    createAmmo(uuid);
                    for (GadgetStorage gadget : ((Main) MongoDBDataLoader.this.plugin).getGadgets().getGadgets()) {
                        gadgetAmmo.put(gadget.getIdentifier(), 0);
                    }
                    MongoDBDataLoader.this.ammo.put(uuid, gadgetAmmo);
                }
            }
        });
    }

    @Override
    public void unloadAmmo(final String uuid) {
        Map<String, Integer> gadgetAmmo = this.ammo.get(uuid);
        this.ammo.remove(uuid);
        if(gadgetAmmo == null) return;
        for(final Map.Entry<String, Integer> entry : gadgetAmmo.entrySet()) {
            this.mongoDB.getObject(new BasicDBObject("player", uuid), this.ammoName, new CallbackHandler<DBObject>() {
                @Override
                public void callback(DBObject dbObject) {
                    BSONObject ammoObject = (BSONObject) dbObject.get("ammo");
                    ammoObject.put(entry.getKey().toLowerCase(), entry.getValue());
                    dbObject.put("ammo", ammoObject);
                    MongoDBDataLoader.this.mongoDB.updateObject(dbObject, new BasicDBObject("player", uuid), MongoDBDataLoader.this.ammoName);
                }
            });
        }
    }

    @Override
    public int getAmmo(String identifier, String uuid) {
        return this.ammo.get(uuid).get(identifier);
    }

    @Override
    public void giveAmmo(String identifier, String uuid, int amount) {
        if(this.ammo.get(uuid).get(identifier) + amount <= 99999) setAmmo(identifier, uuid, this.ammo.get(uuid).get(identifier) + amount);
    }

    @Override
    public void takeAmmo(String identifier, String uuid, int amount) {
        if(this.ammo.get(uuid).get(identifier) - amount >= 0) setAmmo(identifier, uuid, this.ammo.get(uuid).get(identifier) - amount);
    }

    @Override
    public void createAmmo(final String uuid) {
        this.mongoDB.getObject(new BasicDBObject("player", uuid), this.ammoName, new CallbackHandler<DBObject>() {
            @Override
            public void callback(DBObject dbObject) {
                if(dbObject == null) {
                    DBObject object = new BasicDBObject("player", uuid);
                    BSONObject ammoObject = new BasicDBObject("enderbow", 0);
                    List<GadgetStorage> gadgets = ((Main) MongoDBDataLoader.this.plugin).getGadgets().getGadgets();
                    for (int i=1;i<gadgets.size();i++) {
                        ammoObject.put(gadgets.get(i).getIdentifier().toLowerCase(), 0);
                    }
                    object.put("ammo", ammoObject);
                    MongoDBDataLoader.this.mongoDB.storeObject(object, MongoDBDataLoader.this.ammoName);
                }
            }
        });
    }

    @Override
    public void setAmmo(String identifier, String uuid, int amount) {
        this.ammo.get(uuid).put(identifier, amount);
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
                    final CosmeticsQueue queue =  new CosmeticsQueue((Main) MongoDBDataLoader.this.plugin, values);
                    Bukkit.getScheduler().callSyncMethod(MongoDBDataLoader.this.plugin, new Callable<Void>() {
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
    public void updateQueue(final Player p, final CosmeticsQueue queue) {
        Methods.removeCosmetics(p, (Main) this.plugin);
        this.mongoDB.getObject(new BasicDBObject("player", p.getUniqueId().toString()), this.queueName, new CallbackHandler<DBObject>() {
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
                MongoDBDataLoader.this.mongoDB.updateObject(dbObject, new BasicDBObject("player", p.getUniqueId().toString()), MongoDBDataLoader.this.queueName);
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
}