package com.j0ach1mmall3.ultimatecosmetics;

import com.j0ach1mmall3.jlib.commands.Command;
import com.j0ach1mmall3.jlib.inventory.CustomEnchantment;
import com.j0ach1mmall3.jlib.logging.DebugInfo;
import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.StorageLoader;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.jlib.storage.file.yaml.StorageConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.commands.*;
import com.j0ach1mmall3.ultimatecosmetics.config.Config;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import com.j0ach1mmall3.ultimatecosmetics.config.Lang;
import com.j0ach1mmall3.ultimatecosmetics.config.Misc;
import com.j0ach1mmall3.ultimatecosmetics.data.*;
import com.j0ach1mmall3.ultimatecosmetics.listeners.*;
import com.j0ach1mmall3.ultimatecosmetics.modules.auras.AurasModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.balloons.BalloonsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes.BannerCapesModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.banners.BannersModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.blockpets.BlockPetsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.bowtrails.BowTrailsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.cloaks.CloaksModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.fireworks.FireworksModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.gadgets.GadgetsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.hats.HatsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.hearts.HeartsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.morphs.MorphsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.mounts.MountsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.music.MusicModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.particles.ParticlesModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.pets.PetsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.trails.TrailsModule;
import com.j0ach1mmall3.ultimatecosmetics.modules.wardrobe.WardrobeModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author j0ach1mmall3
 * @since 17/08/2015
 */
public final class Main extends ModularizedPlugin<Config> {
    public static final String BUKKIT_VERSION = Bukkit.getBukkitVersion().split("\\-")[0];
    public static final String MINECRAFT_VERSION = ReflectionAPI.getNmsVersion();

    private final Set<Entity> entitiesQueue = new HashSet<>();

    private CosmeticsAPI api;
    private Lang lang;
    private StorageConfigLoader<Main> storage;
    private DataLoader dataLoader;
    private Misc misc;
    private Enchantment glow;

    @Override
    public void onEnable() {
        this.load();

        this.storage = new StorageConfigLoader(this);
        switch (this.storage.getType()) {
            case FILE:
                this.dataLoader = new FileDataLoader(this);
                break;
            case MYSQL:
                this.dataLoader = new MySQLDataLoader(this);
                break;
            case SQLITE:
                this.dataLoader = new SQLiteDataLoader(this);
                break;
            case MONGODB:
                this.dataLoader = new MongoDBDataLoader(this);
                break;
            default:
                this.dataLoader = new FileDataLoader(this);
                break;
        }

        this.api = new CosmeticsAPI();

        if (this.config.isUpdateChecker()) this.checkUpdate(5885);

        this.jLogger.log(ChatColor.GREEN + "Registering Commands...", JLogger.LogLevel.NORMAL);
        new UltimateCosmeticsCommandHandler(this).registerCommand(new Command("UltimateCosmetics", ChatColor.RED + "/ultimatecosmetics <arguments>", this.lang.getCommandNoPermission()));
        new GiveCosmeticCommandHandler(this).registerCommand(new Command("GiveCosmetic", "uc.givecosmetic", ChatColor.RED + "/givecosmetic <player> <cosmetictype> <cosmetic>", this.lang.getCommandNoPermission()));
        new RemoveCosmeticCommandHandler(this).registerCommand(new Command("RemoveCosmetic", "uc.removecosmetic", ChatColor.RED + "/givecosmetic <player> <cosmetictype>", this.lang.getCommandNoPermission()));
        new GiveAmmoCommandHandler(this).registerCommand(new Command("GiveAmmo", "uc.giveammo", ChatColor.RED + "/giveammo <player> <gadget> <amount>", this.lang.getCommandNoPermission()));
        new RemoveAmmoCommandHandler(this).registerCommand(new Command("RemoveAmmo", "uc.removeammo", ChatColor.RED + "/removeammo <player> <gadget> <amount>", this.lang.getCommandNoPermission()));
        new StackerCommandHandler(this).registerCommand(new Command("Stacker", "uc.stacker", new ArrayList<String>(), ChatColor.RED + "/stacker", false, this.lang.getCommandNoPermission()));
        new RemoveAllCosmeticsCommandHandler(this).registerCommand(new Command("RemoveAllCosmetics", "uc.removeallcosmetics", ChatColor.RED + "/removeallcosmetics <player>", this.lang.getCommandNoPermission()));

        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            this.jLogger.log(ChatColor.GOLD + "ProtocolLib not found, falling back to TinyProtocol", JLogger.LogLevel.NORMAL);
            if(ReflectionAPI.verBiggerThan(1, 8)) {
                try {
                    new ProtocolListener(this);
                } catch (RuntimeException e) {
                    this.jLogger.log(ChatColor.RED + "Failed to set up TinyProtocol, did you /reload? Disabling Bat sounds will NOT work!", JLogger.LogLevel.MINIMAL);
                    if (this.jLogger.getLogLevel() == JLogger.LogLevel.EXTENDED || this.jLogger.getLogLevel() == JLogger.LogLevel.ALL)
                        e.printStackTrace();
                }
            }
        } else {
            this.jLogger.log(ChatColor.GREEN + "Switching over to ProtocolLib", JLogger.LogLevel.NORMAL);
            new ProtocolLibListener(this);
        }

        for(Player p : Bukkit.getOnlinePlayers()) {
            this.dataLoader.getAmmoCache().load(p);
        }

        CustomEnchantment ce = new CustomEnchantment("COSMETICGLOW", null, null, 1, 10);
        ce.register();
        this.glow = ce.getEnchantment();

        this.jLogger.log(ChatColor.GREEN + "Done!", JLogger.LogLevel.NORMAL);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        for(Player p : Bukkit.getOnlinePlayers()) {
            for(Cosmetic cosmetic : this.api.getCosmetics(p)) {
                cosmetic.remove(this);
            }
        }

        for(Entity entity : this.entitiesQueue) {
            entity.remove();
        }

        for(PluginModule module : this.modules) {
            module.setEnabled(false);
        }

        this.modules.clear();

        for(Player p : Bukkit.getOnlinePlayers()) {
            this.dataLoader.getAmmoCache().unload(p);
        }

        this.dataLoader.disconnectLoader();
    }

    public void queueEntity(Entity entity) {
        this.entitiesQueue.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entitiesQueue.remove(entity);
    }

    public Set<Entity> getEntitiesQueue() {
        return this.entitiesQueue;
    }

    public CosmeticsAPI getApi() {
        return this.api;
    }

    public Lang getLang() {
        return this.lang;
    }

    public Misc getMisc() {
        return this.misc;
    }

    public DataLoader getDataLoader() {
        return this.dataLoader;
    }

    public StorageConfigLoader getStorage() {
        return this.storage;
    }

    public Enchantment getGlow() {
        return this.glow;
    }

    public <C extends CosmeticConfig> C getConfig(Class<C> clazz) {
        return (C) this.getConfig(clazz.getSimpleName());
    }

    public CosmeticConfig getConfig(String simpleName) {
        for(PluginModule<Main, CosmeticConfig> pluginModule : this.modules) {
            CosmeticConfig cosmeticConfig = pluginModule.getConfig();
            if(pluginModule.isEnabled() && cosmeticConfig.getClass().getSimpleName().equalsIgnoreCase(simpleName)) return cosmeticConfig;
        }
        return null;
    }

    public void reload() {
        HandlerList.unregisterAll(this);

        for(Player p : Bukkit.getOnlinePlayers()) {
            this.dataLoader.getAmmoCache().unload(p);
            for(Cosmetic cosmetic : this.api.getCosmetics(p)) {
                cosmetic.remove(this);
            }
        }

        for(Entity entity : this.entitiesQueue) {
            entity.remove();
        }

        for(PluginModule module : this.modules) {
            module.setEnabled(false);
        }

        this.modules.clear();

        this.load();
    }

    private void load() {
        this.config = new Config(this);
        this.lang = new Lang(this);
        this.misc = new Misc(this);

        this.jLogger.setLogLevel(this.config.getLogLevel());

        this.jLogger.log(ChatColor.GREEN + "Loading Modules...", JLogger.LogLevel.NORMAL);
        PluginModule module = new AurasModule(this);
        module.setEnabled(this.config.isAuras());
        this.registerModule(module);

        module = new BalloonsModule(this);
        module.setEnabled(this.config.isBalloons());
        this.registerModule(module);

        module = new BannerCapesModule(this);
        module.setEnabled(this.config.isBannercapes());
        this.registerModule(module);

        module = new BannersModule(this);
        module.setEnabled(this.config.isBanners());
        this.registerModule(module);

        module = new BlockPetsModule(this);
        module.setEnabled(this.config.isBlockpets());
        this.registerModule(module);

        module = new BowTrailsModule(this);
        module.setEnabled(this.config.isBowtrails());
        this.registerModule(module);

        module = new CloaksModule(this);
        module.setEnabled(this.config.isCloaks());
        this.registerModule(module);

        module = new FireworksModule(this);
        module.setEnabled(this.config.isFireworks());
        this.registerModule(module);

        module = new GadgetsModule(this);
        module.setEnabled(this.config.isGadgets());
        this.registerModule(module);

        module = new HatsModule(this);
        module.setEnabled(this.config.isHats());
        this.registerModule(module);

        module = new HeartsModule(this);
        module.setEnabled(this.config.isHearts());
        this.registerModule(module);

        module = new MorphsModule(this);
        module.setEnabled(this.config.isMorphs());
        this.registerModule(module);

        module = new MountsModule(this);
        module.setEnabled(this.config.isMounts());
        this.registerModule(module);

        module = new MusicModule(this);
        module.setEnabled(this.config.isMusic());
        this.registerModule(module);

        module = new ParticlesModule(this);
        module.setEnabled(this.config.isParticles());
        this.registerModule(module);

        module = new PetsModule(this);
        module.setEnabled(this.config.isPets());
        this.registerModule(module);

        module = new TrailsModule(this);
        module.setEnabled(this.config.isTrails());
        this.registerModule(module);

        module = new WardrobeModule(this);
        module.setEnabled(this.config.isWardrobe());
        this.registerModule(module);

        List<ConfigLoader> configs = new ArrayList<>();
        configs.add(this.config);
        configs.add(this.lang);
        configs.add(this.misc);
        for(PluginModule m : this.modules) {
            configs.add(m.getConfig());
        }

        this.registerDebugInfo(new DebugInfo((StorageLoader) this.dataLoader, configs.toArray(new ConfigLoader[configs.size()])));

        this.jLogger.log(ChatColor.GREEN + "Registering Listeners...", JLogger.LogLevel.NORMAL);

        new CommandsListener(this);
        new PlayerListener(this);
        new EntityListener(this);
    }
}
