package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.mysql.MySQLLoader;
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
public final class MySQLDataLoader extends MySQLLoader implements DataLoader {
    private final Map<String, Map<String, Integer>> ammo = new HashMap<>();
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
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.ammoName + "(Player VARCHAR(36), Enderbow INT(5), EtherealPearl INT(5), PaintballGun INT(5), FlyingPig INT(5), BatBlaster INT(5), CATapult INT(5), RailGun INT(5), CryoTube INT(5), Rocket INT(5), PoopBomb INT(5), GrapplingHook INT(5), SelfDestruct INT(5), SlimeVasion INT(5), FunGun INT(5), MelonThrower INT(5), ColorBomb INT(5), FireTrail INT(5), DiamondShower INT(5), GoldFountain INT(5), PaintTrail INT(5))");
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.queueName + "(Player VARCHAR(36), Balloon VARCHAR(64), Banner VARCHAR(64), Bowtrail VARCHAR(64), Gadget VARCHAR(64), Hat VARCHAR(64), Hearts VARCHAR(64), Morph VARCHAR(64), Mount VARCHAR(64), Music VARCHAR(64), Particles VARCHAR(64), Pet VARCHAR(64), Trail VARCHAR(64), Outfit VARCHAR(64))");
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.stackerName + "(Player VARCHAR(36), Enabled TINYINT(1))");
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.petNamesName + "(Player VARCHAR(36), PetName VARCHAR(64))");
    }

    @Override
    public void disconnectLoader() {
        disconnect();
    }

    @Override
    public void loadAmmo(final String uuid) {
        final Map<String, Integer> gadgetAmmo = new HashMap<>();
        this.mySQL.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, uuid);
                MySQLDataLoader.this.mySQL.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(resultSet.next()) {
                                for (GadgetStorage gadget : ((Main) MySQLDataLoader.this.plugin).getGadgets().getGadgets()) {
                                    gadgetAmmo.put(gadget.getIdentifier(), resultSet.getInt(gadget.getIdentifier()));
                                }
                                MySQLDataLoader.this.ammo.put(uuid, gadgetAmmo);
                            } else {
                                createAmmo(uuid);
                                for (GadgetStorage gadget : ((Main) MySQLDataLoader.this.plugin).getGadgets().getGadgets()) {
                                    gadgetAmmo.put(gadget.getIdentifier(), 0);
                                }
                                MySQLDataLoader.this.ammo.put(uuid, gadgetAmmo);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            createAmmo(uuid);
                            for (GadgetStorage gadget : ((Main) MySQLDataLoader.this.plugin).getGadgets().getGadgets()) {
                                gadgetAmmo.put(gadget.getIdentifier(), 0);
                            }
                            MySQLDataLoader.this.ammo.put(uuid, gadgetAmmo);
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
            this.mySQL.prepareStatement("UPDATE " + this.ammoName + " SET " + entry.getKey() + "=? WHERE Player=?", new CallbackHandler<PreparedStatement>() {
                @Override
                public void callback(PreparedStatement preparedStatement) {
                    MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, String.valueOf(entry.getValue()));
                    MySQLDataLoader.this.mySQL.setString(preparedStatement, 2, uuid);
                    MySQLDataLoader.this.mySQL.execute(preparedStatement);
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
        this.mySQL.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, uuid);
                MySQLDataLoader.this.mySQL.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if (!resultSet.next())
                                MySQLDataLoader.this.mySQL.prepareStatement("INSERT INTO " + MySQLDataLoader.this.ammoName + " VALUES(?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)", new CallbackHandler<PreparedStatement>() {
                                    @Override
                                    public void callback(PreparedStatement preparedStatement) {
                                        MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, uuid);
                                        MySQLDataLoader.this.mySQL.execute(preparedStatement);
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
        this.mySQL.prepareStatement("SELECT * FROM " + this.queueName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                MySQLDataLoader.this.mySQL.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(resultSet.next()) {
                                final CosmeticsQueue queue = new CosmeticsQueue((Main) MySQLDataLoader.this.plugin, Arrays.asList(resultSet.getString("Balloon"), resultSet.getString("Banner"), resultSet.getString("Bowtrail"), resultSet.getString("Gadget"), resultSet.getString("Hat"), resultSet.getString("Hearts"), resultSet.getString("Morph"), resultSet.getString("Mount"), resultSet.getString("Music"), resultSet.getString("Particles"), resultSet.getString("Pet"), resultSet.getString("Trail"), resultSet.getString("Outfit")));
                                Bukkit.getScheduler().callSyncMethod(MySQLDataLoader.this.plugin, new Callable<Void>() {
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
    public void updateQueue(final Player p, CosmeticsQueue queue) {
        final List<String> list = queue.asList();
        this.mySQL.prepareStatement("UPDATE " + this.queueName + " SET Balloon=?, Banner=?, Bowtrail=?, Gadget=?, Hat=?, Hearts=?, Morph=?, Mount=?, Music=?, Particles=?, Pet=?, Trail=?, Outfit=? WHERE Player=?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                for(int i=0;i<13;i++) {
                    MySQLDataLoader.this.mySQL.setString(preparedStatement, i+1, list.get(i));
                }
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 14, p.getUniqueId().toString());
                MySQLDataLoader.this.mySQL.execute(preparedStatement);
            }
        });
    }

    @Override
    public void createQueue(final Player p) {
        this.mySQL.prepareStatement("SELECT * FROM " + this.queueName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                MySQLDataLoader.this.mySQL.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(!resultSet.next()) MySQLDataLoader.this.mySQL.prepareStatement("INSERT INTO " + MySQLDataLoader.this.queueName + " VALUES(?, '', '', '', '', '', '', '', '', '', '', '', '', '')", new CallbackHandler<PreparedStatement>() {
                                @Override
                                public void callback(PreparedStatement preparedStatement) {
                                    MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                                    MySQLDataLoader.this.mySQL.execute(preparedStatement);
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
        this.mySQL.prepareStatement("SELECT * FROM " + this.stackerName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                MySQLDataLoader.this.mySQL.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
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
        this.mySQL.prepareStatement("UPDATE " + this.stackerName + " SET Enabled=? WHERE Player=?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setBoolean(preparedStatement, 1, stacker);
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 2, p.getUniqueId().toString());
                MySQLDataLoader.this.mySQL.execute(preparedStatement);
            }
        });
    }

    @Override
    public void createStacker(final Player p) {
        this.mySQL.prepareStatement("SELECT * FROM " + this.stackerName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                MySQLDataLoader.this.mySQL.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(!resultSet.next()) MySQLDataLoader.this.mySQL.prepareStatement("INSERT INTO " + MySQLDataLoader.this.stackerName + " VALUES(?, 1)", new CallbackHandler<PreparedStatement>() {
                                @Override
                                public void callback(PreparedStatement preparedStatement) {
                                    MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                                    MySQLDataLoader.this.mySQL.execute(preparedStatement);
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
        this.mySQL.prepareStatement("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                MySQLDataLoader.this.mySQL.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
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
    public void setPetName(final Player p, final String name) {
        this.mySQL.prepareStatement("SELECT * FROM " + this.petNamesName + " WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
            @Override
            public void callback(PreparedStatement preparedStatement) {
                MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                MySQLDataLoader.this.mySQL.executeQuerry(preparedStatement, new CallbackHandler<ResultSet>() {
                    @Override
                    public void callback(ResultSet resultSet) {
                        try {
                            if(resultSet.next()) {
                                MySQLDataLoader.this.mySQL.prepareStatement("UPDATE " + MySQLDataLoader.this.petNamesName + " SET PetName = ? WHERE Player = ?", new CallbackHandler<PreparedStatement>() {
                                    @Override
                                    public void callback(PreparedStatement preparedStatement) {
                                        MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, name);
                                        MySQLDataLoader.this.mySQL.setString(preparedStatement, 2, p.getUniqueId().toString());
                                        MySQLDataLoader.this.mySQL.execute(preparedStatement);
                                    }
                                });
                            } else {
                                MySQLDataLoader.this.mySQL.prepareStatement("INSERT INTO " + MySQLDataLoader.this.petNamesName + " VALUES(?, ?)", new CallbackHandler<PreparedStatement>() {
                                    @Override
                                    public void callback(PreparedStatement preparedStatement) {
                                        MySQLDataLoader.this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
                                        MySQLDataLoader.this.mySQL.setString(preparedStatement, 2, name);
                                        MySQLDataLoader.this.mySQL.execute(preparedStatement);
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
