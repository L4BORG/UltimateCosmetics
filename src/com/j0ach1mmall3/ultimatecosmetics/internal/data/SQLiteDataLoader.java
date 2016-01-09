package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.sqlite.SQLiteLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class SQLiteDataLoader extends SQLiteLoader implements DataLoader {
    private final Map<String, Map<String, Integer>> ammo = new HashMap<>();
    private final String ammoName;
    private final String queueName;
    private final String stackerName;
    private final String petNamesName;

    public SQLiteDataLoader(Main plugin) {
        super(plugin, "data");
        this.ammoName = plugin.getStorage().getDatabasePrefix() + "ammo";
        this.queueName = plugin.getStorage().getDatabasePrefix() + "queue";
        this.stackerName = plugin.getStorage().getDatabasePrefix() + "stacker";
        this.petNamesName = plugin.getStorage().getDatabasePrefix() + "petnames";
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " + this.ammoName + "(Player VARCHAR(36), Enderbow INT(5), EtherealPearl INT(5), PaintballGun INT(5), FlyingPig INT(5), BatBlaster INT(5), CATapult INT(5), RailGun INT(5), CryoTube INT(5), Rocket INT(5), PoopBomb INT(5), GrapplingHook INT(5), SelfDestruct INT(5), SlimeVasion INT(5), FunGun INT(5), MelonThrower INT(5), ColorBomb INT(5), FireTrail INT(5), DiamondShower INT(5), GoldFountain INT(5), PaintTrail INT(5))");
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " + this.queueName + "(Player VARCHAR(36), Balloon VARCHAR(64), Banner VARCHAR(64), Bowtrail VARCHAR(64), Gadget VARCHAR(64), Hat VARCHAR(64), Hearts VARCHAR(64), Morph VARCHAR(64), Mount VARCHAR(64), Music VARCHAR(64), Particles VARCHAR(64), Pet VARCHAR(64), Trail VARCHAR(64), Outfit VARCHAR(64))");
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " +  this.stackerName + "(Player VARCHAR(36), Enabled TINYINT(1))");
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " +  this.petNamesName + "(Player VARCHAR(36), PetName VARCHAR(64))");
    }

    @Override
    public void disconnectLoader() {
        disconnect();
    }

    @Override
    public void loadAmmo(final String uuid) {
        final Map<String, Integer> gadgetAmmo = new HashMap<>();
        this.sqLite.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, uuid);
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(!resultSet.isClosed() && resultSet.next()) {
                                for (GadgetStorage gadget : ((Main) SQLiteDataLoader.this.plugin).getGadgets().getGadgets()) {
                                    gadgetAmmo.put(gadget.getIdentifier(), resultSet.getInt(gadget.getIdentifier()));
                                }
                                SQLiteDataLoader.this.ammo.put(uuid, gadgetAmmo);
                            } else {
                                createAmmo(uuid);
                                for (GadgetStorage gadget : ((Main) SQLiteDataLoader.this.plugin).getGadgets().getGadgets()) {
                                    gadgetAmmo.put(gadget.getIdentifier(), 0);
                                }
                                SQLiteDataLoader.this.ammo.put(uuid, gadgetAmmo);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            createAmmo(uuid);
                            for (GadgetStorage gadget : ((Main) SQLiteDataLoader.this.plugin).getGadgets().getGadgets()) {
                                gadgetAmmo.put(gadget.getIdentifier(), 0);
                            }
                            SQLiteDataLoader.this.ammo.put(uuid, gadgetAmmo);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void unloadAmmo(final String uuid) {
        Map<String, Integer> gadgetAmmo = this.ammo.get(uuid);
        this.ammo.remove(uuid);
        if(gadgetAmmo == null) return;
        for(final Map.Entry<String, Integer> entry : gadgetAmmo.entrySet()) {
            this.sqLite.prepareStatement("UPDATE " + this.ammoName + " SET " + entry.getKey() + "=? WHERE Player=?", new CallbackHandler<PreparedStatement>() {
                @Override
                public void callback(PreparedStatement preparedStatement) {
                    SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, String.valueOf(entry.getValue()));
                    SQLiteDataLoader.this.sqLite.setString(preparedStatement, 2, uuid);
                    SQLiteDataLoader.this.sqLite.execute(preparedStatement);
                }
            });
        }
    }

    @Override
    public int getAmmo(String identifier, String uuid) {
        return this.ammo.get(uuid)==null?0:this.ammo.get(uuid).get(identifier);
    }

    @Override
    public void giveAmmo(String identifier, String uuid, int amount) {
        if(getAmmo(identifier, uuid) + amount <= 99999) setAmmo(identifier, uuid, this.ammo.get(uuid).get(identifier) + amount);
    }

    @Override
    public void takeAmmo(String identifier, String uuid, int amount) {
        if(getAmmo(identifier, uuid) - amount >= 0) setAmmo(identifier, uuid, this.ammo.get(uuid).get(identifier) - amount);
    }

    @Override
    public void createAmmo(final String uuid) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, uuid);
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if (!resultSet.isClosed() && !resultSet.next())
                                SQLiteDataLoader.this.sqLite.prepareStatement("INSERT INTO " + SQLiteDataLoader.this.ammoName + " VALUES(?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)", new CallbackHandler<PreparedStatement>() {
                                    @Override
                                    public void callback(PreparedStatement preparedStatement) {
                                        SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, uuid);
                                        SQLiteDataLoader.this.sqLite.execute(preparedStatement);
                                    }
                                });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void setAmmo(String identifier, String uuid, int amount) {
        this.ammo.get(uuid).put(identifier, amount);
    }

    @Override
    public void giveBackQueue(final Player p) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.queueName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(!resultSet.isClosed() && resultSet.next()) {
                                final CosmeticsQueue queue = new CosmeticsQueue((Main) SQLiteDataLoader.this.plugin, Arrays.asList(resultSet.getString("Balloon"), resultSet.getString("Banner"), resultSet.getString("Bowtrail"), resultSet.getString("Gadget"), resultSet.getString("Hat"), resultSet.getString("Hearts"), resultSet.getString("Morph"), resultSet.getString("Mount"), resultSet.getString("Music"), resultSet.getString("Particles"), resultSet.getString("Pet"), resultSet.getString("Trail"), resultSet.getString("Outfit")));
                                Bukkit.getScheduler().callSyncMethod(SQLiteDataLoader.this.plugin, new Callable<Void>() {
                                    @Override
                                    public Void call() {
                                        queue.give(p);
                                        return null;
                                    }
                                });
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void updateQueue(final String uuid, CosmeticsQueue queue) {
        final List<String> list = queue.asList();
        this.sqLite.prepareStatement("UPDATE " + this.queueName + " SET Balloon=?, Banner=?, Bowtrail=?, Gadget=?, Hat=?, Hearts=?, Morph=?, Mount=?, Music=?, Particles=?, Pet=?, Trail=?, Outfit=? WHERE Player=?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                for(int i=0;i<13;i++) {
                    SQLiteDataLoader.this.sqLite.setString(preparedStatement, i+1, list.get(i));
                }
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 14, uuid);
                SQLiteDataLoader.this.sqLite.execute(preparedStatement);
            }
        });
    }

    @Override
    public void createQueue(final Player p) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.queueName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(!resultSet.isClosed() && !resultSet.next()) SQLiteDataLoader.this.sqLite.prepareStatement("INSERT INTO " + SQLiteDataLoader.this.queueName + " VALUES(?, '', '', '', '', '', '', '', '', '', '', '', '', '')", new CallbackHandler<PreparedStatement>() {
                                @Override
                                public void callback(PreparedStatement preparedStatement) {
                                    SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                                    SQLiteDataLoader.this.sqLite.execute(preparedStatement);
                                }
                            });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getStacker(final Player p, final CallbackHandler<Boolean> callbackHandler) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.stackerName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            resultSet.next();
                            callbackHandler.callback(resultSet.getBoolean("Enabled"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                            callbackHandler.callback(false);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void setStacker(final Player p, final boolean stacker) {
        this.sqLite.prepareStatement("UPDATE " + this.stackerName + " SET Enabled=? WHERE Player=?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setBoolean(preparedStatement, 1, stacker);
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 2, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.execute(preparedStatement);
            }
        });
    }

    @Override
    public void createStacker(final Player p) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.stackerName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(!resultSet.isClosed() && !resultSet.next()) SQLiteDataLoader.this.sqLite.prepareStatement("INSERT INTO " + SQLiteDataLoader.this.stackerName + " VALUES(?, 1)", new CallbackHandler<PreparedStatement>() {
                                @Override
                                public void callback(PreparedStatement preparedStatement) {
                                    SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                                    SQLiteDataLoader.this.sqLite.execute(preparedStatement);
                                }
                            });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getPetName(final Player p, final CallbackHandler<String> callbackHandler) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            resultSet.next();
                            callbackHandler.callback(resultSet.getString("PetName"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                            callbackHandler.callback(null);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void createPetName(final Player p) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(!resultSet.isClosed() && !resultSet.next()) SQLiteDataLoader.this.sqLite.prepareStatement("INSERT INTO " + SQLiteDataLoader.this.petNamesName + " VALUES(?, '')", new CallbackHandler<PreparedStatement>() {
                                @Override
                                public void callback(PreparedStatement preparedStatement) {
                                    SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                                    SQLiteDataLoader.this.sqLite.execute(preparedStatement);
                                }
                            });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void setPetName(final Player p, final String name) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(!resultSet.isClosed() && resultSet.next()) {
                                SQLiteDataLoader.this.sqLite.prepareStatement("UPDATE " + SQLiteDataLoader.this.petNamesName + " SET PetName = ? WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
                                    @Override
                                    public void callback(PreparedStatement preparedStatement) {
                                        SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, name);
                                        SQLiteDataLoader.this.sqLite.setString(preparedStatement, 2, p.getUniqueId().toString());
                                        SQLiteDataLoader.this.sqLite.execute(preparedStatement);
                                    }
                                });
                            } else {
                                SQLiteDataLoader.this.sqLite.prepareStatement("INSERT INTO " + SQLiteDataLoader.this.petNamesName + " VALUES(?, ?)", new CallbackHandler<PreparedStatement>() {
                                    @Override
                                    public void callback(PreparedStatement preparedStatement) {
                                        SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                                        SQLiteDataLoader.this.sqLite.setString(preparedStatement, 2, name);
                                        SQLiteDataLoader.this.sqLite.execute(preparedStatement);
                                    }
                                });
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
