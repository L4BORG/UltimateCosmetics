package com.j0ach1mmall3.ultimatecosmetics;

import com.j0ach1mmall3.jlib.commands.Command;
import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomEnchantment;
import com.j0ach1mmall3.jlib.logging.DebugInfo;
import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.methods.Notes;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.plugin.modularization.ModularizedPlugin;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.StorageLoader;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.jlib.storage.file.yaml.StorageConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
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
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author j0ach1mmall3
 * @since 17/08/2015
 */
public final class Main extends ModularizedPlugin {
    public static final String BUKKIT_VERSION = Bukkit.getBukkitVersion().split("\\-")[0];
    public static final String MINECRAFT_VERSION = ReflectionAPI.getNmsVersion();

    private final Set<Entity> entitiesQueue = new HashSet<>();

    private boolean protocolLib;
    private CosmeticsAPI api;
    private Lang lang;
    private StorageConfigLoader storage;
    private DataLoader dataLoader;
    private Misc misc;
    private GuiHandler guiHandler;
    private Enchantment glow;

    @Override
    public void onEnable() {
        if(this.dataLoader != null) this.dataLoader.disconnectLoader();

        this.config = new Config(this);
        this.lang = new Lang(this);

        this.jLogger.setLogLevel(((Config) this.config).getLogLevel());

        this.api = new CosmeticsAPI(this);

        this.jLogger.log(ChatColor.GOLD + "You are running Bukkit version " + BUKKIT_VERSION + " (MC " + MINECRAFT_VERSION + ')', JLogger.LogLevel.EXTENDED);

        if (((Config) this.config).isUpdateChecker()) this.checkUpdate(5885);

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

    public Map<String, Integer> getDefaultAmmo() {
        Map<String, Integer> ammo = new HashMap<>();
        for (String s : Arrays.asList("Enderbow", "EtherealPearl", "PaintballGun", "FlyingPig", "BatBlaster", "CATapult", "RailGun", "CryoTube", "Rocket", "PoopBomb", "GrapplingHook", "SelfDestruct", "Slimevasion", "FunGun", "MelonThrower", "ColorBomb", "FireTrail", "DiamondShower", "GoldFountain", "PaintTrail")) {
            ammo.put(s, 0);
        }
        return ammo;
    }

    public void reload() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            this.dataLoader.getAmmoCache().unload(p);
            for(Cosmetic cosmetic : this.api.getCosmetics(p)) {
                cosmetic.remove();
            }
        }

        PlayerCommandPreprocessEvent.getHandlerList().unregister(this);
        PlayerToggleFlightEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        PlayerInteractEntityEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerChangedWorldEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        PlayerKickEvent.getHandlerList().unregister(this);
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
        PlayerDropItemEvent.getHandlerList().unregister(this);
        PlayerPickupItemEvent.getHandlerList().unregister(this);


        CreatureSpawnEvent.getHandlerList().unregister(this);

        VehicleExitEvent.getHandlerList().unregister(this);

        HangingBreakByEntityEvent.getHandlerList().unregister(this);

        ProjectileHitEvent.getHandlerList().unregister(this);

        ItemDespawnEvent.getHandlerList().unregister(this);

        EntityChangeBlockEvent.getHandlerList().unregister(this);
        EntityUnleashEvent.getHandlerList().unregister(this);
        EntityDismountEvent.getHandlerList().unregister(this);
        EntityPortalEvent.getHandlerList().unregister(this);
        EntityDamageEvent.getHandlerList().unregister(this);
        EntityChangeBlockEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
        EntityTargetEvent.getHandlerList().unregister(this);
        EntityTeleportEvent.getHandlerList().unregister(this);
        EntityCombustEvent.getHandlerList().unregister(this);
        EntityTameEvent.getHandlerList().unregister(this);
        EntityRegainHealthEvent.getHandlerList().unregister(this);
        EntityExplodeEvent.getHandlerList().unregister(this);
        EntityInteractEvent.getHandlerList().unregister(this);

        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryPickupItemEvent.getHandlerList().unregister(this);
        InventoryOpenEvent.getHandlerList().unregister(this);


        this.config = new Config(this);
        this.lang = new Lang(this);
        this.misc = new Misc(this);

        for(PluginModule module : this.modules) {
            module.setEnabled(false);
        }
        this.modules.clear();
        this.loadModules();

        List<ConfigLoader> configs = Arrays.asList(this.config, this.lang, this.misc);
        for(PluginModule module : this.modules) {
            configs.add(module.getConfig());
        }

        this.registerDebugInfo(new DebugInfo((StorageLoader) this.dataLoader, configs.toArray(new ConfigLoader[configs.size()])));

        new CommandsListener(this);
        new PlayerListener(this);
        new EntityListener(this);
    }

    public void informPlayerNoPermission(CommandSender s, String message) {
        if(message.isEmpty()) return;
        if (s instanceof Player) {
            Player p = (Player) s;
            p.sendMessage(Placeholders.parse(message, p));
            Notes.playNote(p, Instrument.BASS_DRUM, Note.Tone.A);
        } else s.sendMessage(Placeholders.parse(message, null));
    }

    public void informPlayerNotEnabled(CommandSender s) {
        String notEnabled = this.lang.getNotEnabled();
        if(notEnabled.isEmpty()) return;
        if (s instanceof Player) {
            Player p = (Player) s;
            p.sendMessage(Placeholders.parse(notEnabled, p));
            Notes.playNote(p, Instrument.BASS_DRUM, Note.Tone.A);
        } else s.sendMessage(Placeholders.parse(notEnabled, null));
    }

    public CosmeticConfig getConfigByType(CosmeticType type) {
        switch (type) {
            case AURA:
                return ((Config) this.config).isAuras() ? (CosmeticConfig) this.modules.get(0).getConfig() : null;
            case BALLOON:
                return ((Config) this.config).isBalloons() ? (CosmeticConfig) this.modules.get(1).getConfig() : null;
            case BANNERCAPE:
                return ((Config) this.config).isBannercapes() ? (CosmeticConfig) this.modules.get(2).getConfig() : null;
            case BANNER:
                return ((Config) this.config).isBanners() ? (CosmeticConfig) this.modules.get(3).getConfig() : null;
            case BLOCKPET:
                return ((Config) this.config).isBlockpets() ? (CosmeticConfig) this.modules.get(4).getConfig() : null;
            case BOWTRAIL:
                return ((Config) this.config).isBowtrails() ? (CosmeticConfig) this.modules.get(5).getConfig() : null;
            case CLOAK:
                return ((Config) this.config).isCloaks() ? (CosmeticConfig) this.modules.get(6).getConfig() : null;
            case FIREWORK:
                return ((Config) this.config).isFireworks() ? (CosmeticConfig) this.modules.get(7).getConfig() : null;
            case GADGET:
                return ((Config) this.config).isGadgets() ? (CosmeticConfig) this.modules.get(8).getConfig() : null;
            case HAT:
                return ((Config) this.config).isHats() ? (CosmeticConfig) this.modules.get(9).getConfig() : null;
            case HEART:
                return ((Config) this.config).isHearts() ? (CosmeticConfig) this.modules.get(10).getConfig() : null;
            case MORPH:
                return ((Config) this.config).isMorphs() ? (CosmeticConfig) this.modules.get(11).getConfig() : null;
            case MOUNT:
                return ((Config) this.config).isMounts() ? (CosmeticConfig) this.modules.get(12).getConfig() : null;
            case MUSIC:
                return ((Config) this.config).isMusic() ? (CosmeticConfig) this.modules.get(13).getConfig() : null;
            case PARTICLE:
                return ((Config) this.config).isParticles() ? (CosmeticConfig) this.modules.get(14).getConfig() : null;
            case PET:
                return ((Config) this.config).isPets() ? (CosmeticConfig) this.modules.get(15).getConfig() : null;
            case TRAIL:
                return ((Config) this.config).isTrails() ? (CosmeticConfig) this.modules.get(16).getConfig() : null;
            case OUTFIT:
                return ((Config) this.config).isWardrobe() ? (CosmeticConfig) this.modules.get(17).getConfig() : null;
        }
        return null;
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
        module.setEnabled(((Config) this.config).isAuras());
        this.registerModule(module);

        module = new BalloonsModule(this);
        module.setEnabled(((Config) this.config).isBalloons());
        this.registerModule(module);

        module = new BannerCapesModule(this);
        module.setEnabled(((Config) this.config).isBannercapes());
        this.registerModule(module);

        module = new BannersModule(this);
        module.setEnabled(((Config) this.config).isBanners());
        this.registerModule(module);

        module = new BlockPetsModule(this);
        module.setEnabled(((Config) this.config).isBlockpets());
        this.registerModule(module);

        module = new BowTrailsModule(this);
        module.setEnabled(((Config) this.config).isBowtrails());
        this.registerModule(module);

        module = new CloaksModule(this);
        module.setEnabled(((Config) this.config).isCloaks());
        this.registerModule(module);

        module = new FireworksModule(this);
        module.setEnabled(((Config) this.config).isFireworks());
        this.registerModule(module);

        module = new GadgetsModule(this);
        module.setEnabled(((Config) this.config).isGadgets());
        this.registerModule(module);

        module = new HatsModule(this);
        module.setEnabled(((Config) this.config).isHats());
        this.registerModule(module);

        module = new HeartsModule(this);
        module.setEnabled(((Config) this.config).isHearts());
        this.registerModule(module);

        module = new MorphsModule(this);
        module.setEnabled(((Config) this.config).isMorphs());
        this.registerModule(module);

        module = new MountsModule(this);
        module.setEnabled(((Config) this.config).isMounts());
        this.registerModule(module);

        module = new MusicModule(this);
        module.setEnabled(((Config) this.config).isMusic());
        this.registerModule(module);

        module = new ParticlesModule(this);
        module.setEnabled(((Config) this.config).isParticles());
        this.registerModule(module);

        module = new PetsModule(this);
        module.setEnabled(((Config) this.config).isPets());
        this.registerModule(module);

        module = new TrailsModule(this);
        module.setEnabled(((Config) this.config).isTrails());
        this.registerModule(module);

        module = new WardrobeModule(this);
        module.setEnabled(((Config) this.config).isWardrobe());
        this.registerModule(module);
    }
}
