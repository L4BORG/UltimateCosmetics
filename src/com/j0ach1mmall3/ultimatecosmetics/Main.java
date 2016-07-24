package com.j0ach1mmall3.ultimatecosmetics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

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
import com.j0ach1mmall3.ultimatecosmetics.commands.GiveAmmoCommandHandler;
import com.j0ach1mmall3.ultimatecosmetics.commands.GiveCosmeticCommandHandler;
import com.j0ach1mmall3.ultimatecosmetics.commands.RemoveAllCosmeticsCommandHandler;
import com.j0ach1mmall3.ultimatecosmetics.commands.RemoveAmmoCommandHandler;
import com.j0ach1mmall3.ultimatecosmetics.commands.RemoveCosmeticCommandHandler;
import com.j0ach1mmall3.ultimatecosmetics.commands.StackerCommandHandler;
import com.j0ach1mmall3.ultimatecosmetics.commands.UltimateCosmeticsCommandHandler;
import com.j0ach1mmall3.ultimatecosmetics.config.Config;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import com.j0ach1mmall3.ultimatecosmetics.config.Lang;
import com.j0ach1mmall3.ultimatecosmetics.config.Misc;
import com.j0ach1mmall3.ultimatecosmetics.data.DataLoader;
import com.j0ach1mmall3.ultimatecosmetics.data.FileDataLoader;
import com.j0ach1mmall3.ultimatecosmetics.data.MongoDBDataLoader;
import com.j0ach1mmall3.ultimatecosmetics.data.MySQLDataLoader;
import com.j0ach1mmall3.ultimatecosmetics.data.SQLiteDataLoader;
import com.j0ach1mmall3.ultimatecosmetics.listeners.CommandsListener;
import com.j0ach1mmall3.ultimatecosmetics.listeners.EntityListener;
import com.j0ach1mmall3.ultimatecosmetics.listeners.PlayerListener;
import com.j0ach1mmall3.ultimatecosmetics.listeners.ProtocolLibListener;
import com.j0ach1mmall3.ultimatecosmetics.listeners.ProtocolListener;
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
import org.yaml.snakeyaml.error.YAMLException;

/**
 * @author j0ach1mmall3
 * @since 17/08/2015
 */
public final class Main extends ModularizedPlugin<Config> {
    public static final String BUKKIT_VERSION = Bukkit.getBukkitVersion().split("\\-")[0];
    public static final String MINECRAFT_VERSION = ReflectionAPI.getNmsVersion();

    private final Set<Entity> entitiesQueue = new HashSet<>();

    private boolean protocolLib;
    private CosmeticsAPI api;
    private Lang lang;
    private StorageConfigLoader<Main> storage;
    private DataLoader dataLoader;
    private Misc misc;
    private GuiHandler guiHandler;
    private Enchantment glow;

    @Override
    public void onEnable() {
        Bukkit.getLogger().setFilter(new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                return !(record.getThrown() instanceof YAMLException);
            }
        });

        if(this.dataLoader != null) this.dataLoader.disconnectLoader();

        this.config = new Config(this);
        this.lang = new Lang(this);

        this.jLogger.setLogLevel(this.config.getLogLevel());

        this.api = new CosmeticsAPI(this);

        this.jLogger.log(ChatColor.GOLD + "You are running Bukkit version " + BUKKIT_VERSION + " (MC " + MINECRAFT_VERSION + ')', JLogger.LogLevel.EXTENDED);

        if (this.config.isUpdateChecker()) this.checkUpdate(5885);

        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            this.protocolLib = true;
            this.jLogger.log(ChatColor.GREEN + "Switching over to ProtocolLib", JLogger.LogLevel.NORMAL);
        } else this.jLogger.log(ChatColor.GOLD + "ProtocolLib not found, falling back to TinyProtocol", JLogger.LogLevel.NORMAL);

        this.misc = new Misc(this);

        this.jLogger.log(ChatColor.GREEN + "Registering Commands...", JLogger.LogLevel.NORMAL);
        new UltimateCosmeticsCommandHandler(this).registerCommand(new Command(this, "UltimateCosmetics", ChatColor.RED + "/ultimatecosmetics <arguments>", this.lang.getCommandNoPermission()));
        new GiveCosmeticCommandHandler(this).registerCommand(new Command(this, "GiveCosmetic", "uc.givecosmetic", ChatColor.RED + "/givecosmetic <player> <cosmetictype> <cosmetic>", this.lang.getCommandNoPermission()));
        new RemoveCosmeticCommandHandler(this).registerCommand(new Command(this, "RemoveCosmetic", "uc.removecosmetic", ChatColor.RED + "/givecosmetic <player> <cosmetictype>", this.lang.getCommandNoPermission()));
        new GiveAmmoCommandHandler(this).registerCommand(new Command(this, "GiveAmmo", "uc.giveammo", ChatColor.RED + "/giveammo <player> <gadget> <amount>", this.lang.getCommandNoPermission()));
        new RemoveAmmoCommandHandler(this).registerCommand(new Command(this, "RemoveAmmo", "uc.removeammo", ChatColor.RED + "/removeammo <player> <gadget> <amount>", this.lang.getCommandNoPermission()));
        new StackerCommandHandler(this).registerCommand(new Command(this, "Stacker", "uc.stacker", ChatColor.RED + "/stacker", false, this.lang.getCommandNoPermission()));
        new RemoveAllCosmeticsCommandHandler(this).registerCommand(new Command(this, "RemoveAllCosmetics", "uc.removeallcosmetics", ChatColor.RED + "/removeallcosmetics <player>", this.lang.getCommandNoPermission()));
        //new UpsidedownCommandHandler(this).registerCommand(new Command(this, "Upsidedown", this.misc.getUpsideDownPermission(), ChatColor.RED + "/upsidedown", false, this.lang.getCommandNoPermission()));
        //new EarsCommandHandler(this).registerCommand(new Command(this, "Ears", this.misc.getEarsPermission(), ChatColor.RED + "/ears", false, this.lang.getCommandNoPermission()));

        this.jLogger.log(ChatColor.GREEN + "Loading Configs...", JLogger.LogLevel.NORMAL);
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

        this.loadModules();

        this.jLogger.log(ChatColor.GREEN + "Registering Listeners...", JLogger.LogLevel.NORMAL);
        new CommandsListener(this);
        new PlayerListener(this);
        new EntityListener(this);

        this.guiHandler = new GuiHandler(this);

        if (this.protocolLib) {
            new ProtocolLibListener(this);
        } else if(!BUKKIT_VERSION.startsWith("1.7")) {
            try {
                new ProtocolListener(this);
            } catch (RuntimeException e) {
                this.jLogger.log(ChatColor.RED + "Failed to set up TinyProtocol, did you /reload? Disabling Bat sounds will NOT work!", JLogger.LogLevel.MINIMAL);
                if (this.jLogger.getLogLevel() == JLogger.LogLevel.EXTENDED || this.jLogger.getLogLevel() == JLogger.LogLevel.ALL) e.printStackTrace();
            }
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
        for(Player p : Bukkit.getOnlinePlayers()) {
            this.dataLoader.getAmmoCache().unload(p);
            for(Cosmetic cosmetic : this.api.getCosmetics(p)) {
                cosmetic.remove();
            }
        }
        for(Entity entity : this.entitiesQueue) {
            entity.remove();
        }
        for(PluginModule module : this.modules) {
            module.setEnabled(false);
        }
    }

    public void reload() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            this.dataLoader.getAmmoCache().unload(p);
            for(Cosmetic cosmetic : this.api.getCosmetics(p)) {
                cosmetic.remove();
            }
        }

        HandlerList.unregisterAll(this);

        this.config = new Config(this);
        this.lang = new Lang(this);
        this.misc = new Misc(this);

        for(PluginModule module : this.modules) {
            module.setEnabled(false);
        }
        this.modules.clear();
        this.loadModules();

        List<ConfigLoader> configs = new ArrayList<>();
        configs.add(this.config);
        configs.add(this.lang);
        configs.add(this.misc);
        for(PluginModule module : this.modules) {
            configs.add(module.getConfig());
        }

        this.registerDebugInfo(new DebugInfo((StorageLoader) this.dataLoader, configs.toArray(new ConfigLoader[configs.size()])));

        new CommandsListener(this);
        new PlayerListener(this);
        new EntityListener(this);
        new GuiHandler(this);
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

    public GuiHandler getGuiHandler() {
        return this.guiHandler;
    }

    public Enchantment getGlow() {
        return this.glow;
    }

    private void loadModules() {
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
    }

    public CosmeticConfig getConfigByIdentifier(String identifier) {
        for(PluginModule<Main, CosmeticConfig> pluginModule : this.modules) {
            CosmeticConfig cosmeticConfig = pluginModule.getConfig();
            if(pluginModule.isEnabled() && cosmeticConfig.getCosmeticClass().getSimpleName().equalsIgnoreCase(identifier)) return cosmeticConfig;
        }
        return null;
    }
}
