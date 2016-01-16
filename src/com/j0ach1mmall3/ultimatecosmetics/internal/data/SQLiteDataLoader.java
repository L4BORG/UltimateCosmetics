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
    private final Map<Player, Map<String, Integer>> ammo = new HashMap<>();
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
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " + this.stackerName + "(Player VARCHAR(36), Enabled TINYINT(1))");
        this.sqLite.execute("CREATE TABLE IF NOT EXISTS " + this.petNamesName + "(Player VARCHAR(36), PetName VARCHAR(64))");
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
                SQLiteDataLoader.this.ammo.put(player, map);
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
    public void getOfflineAmmo(final String uuid, final CallbackHandler<Map<String, Integer>> callbackHandler) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, uuid);
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        Map<String, Integer> map = new HashMap<>();
                        try {
                            if(resultSet.next()) {
                                for (GadgetStorage gadget : ((Main) SQLiteDataLoader.this.storage.getPlugin()).getGadgets().getGadgets()) {
                                    map.put(gadget.getIdentifier(), resultSet.getInt(gadget.getIdentifier()));
                                }
                            } else {
                                createAmmo(uuid);
                                for (GadgetStorage gadget : ((Main) SQLiteDataLoader.this.storage.getPlugin()).getGadgets().getGadgets()) {
                                    map.put(gadget.getIdentifier(), 0);
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            createAmmo(uuid);
                            for (GadgetStorage gadget : ((Main) SQLiteDataLoader.this.storage.getPlugin()).getGadgets().getGadgets()) {
                                map.put(gadget.getIdentifier(), 0);
                            }
                        }
                        callbackHandler.callback(map);
                    }
                });
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
        this.sqLite.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, uuid);
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(resultSet.next()) {
                                for(final Map.Entry<String, Integer> entry : map.entrySet()) {
                                    SQLiteDataLoader.this.sqLite.prepareStatement("UPDATE " + SQLiteDataLoader.this.ammoName + " SET " + entry.getKey() + "=? WHERE Player=?", new CallbackHandler<PreparedStatement>() {
                                        @Override
                                        public void callback(PreparedStatement preparedStatement) {
                                            SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, String.valueOf(entry.getValue()));
                                            SQLiteDataLoader.this.sqLite.setString(preparedStatement, 2, uuid);
                                            SQLiteDataLoader.this.sqLite.execute(preparedStatement);
                                        }
                                    });
                                }
                            } else createAmmo(uuid);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            createAmmo(uuid);
                        }
                    }
                });
            }
        });
    }

    private void createAmmo(final String uuid) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, uuid);
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if (!resultSet.next())
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
    public void giveBackQueue(final Player p) {
        this.sqLite.prepareStatement("SELECT * FROM " + this.queueName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                SQLiteDataLoader.this.sqLite.setString(preparedStatement, 1, p.getUniqueId().toString());
                SQLiteDataLoader.this.sqLite.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(resultSet.next()) {
                                final CosmeticsQueue queue = new CosmeticsQueue((Main) SQLiteDataLoader.this.storage.getPlugin(), Arrays.asList(resultSet.getString("Balloon"), resultSet.getString("Banner"), resultSet.getString("Bowtrail"), resultSet.getString("Gadget"), resultSet.getString("Hat"), resultSet.getString("Hearts"), resultSet.getString("Morph"), resultSet.getString("Mount"), resultSet.getString("Music"), resultSet.getString("Particles"), resultSet.getString("Pet"), resultSet.getString("Trail"), resultSet.getString("Outfit")));
                                Bukkit.getScheduler().callSyncMethod(SQLiteDataLoader.this.storage.getPlugin(), new Callable<Void>() {
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
                            if(!resultSet.next()) SQLiteDataLoader.this.sqLite.prepareStatement("INSERT INTO " + SQLiteDataLoader.this.queueName + " VALUES(?, '', '', '', '', '', '', '', '', '', '', '', '', '')", new CallbackHandler<PreparedStatement>() {
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
                            if(!resultSet.next()) SQLiteDataLoader.this.sqLite.prepareStatement("INSERT INTO " + SQLiteDataLoader.this.stackerName + " VALUES(?, 1)", new CallbackHandler<PreparedStatement>() {
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
                            if(!resultSet.next()) SQLiteDataLoader.this.sqLite.prepareStatement("INSERT INTO " + SQLiteDataLoader.this.petNamesName + " VALUES(?, '')", new CallbackHandler<PreparedStatement>() {
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
                            if(resultSet.next()) {
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
