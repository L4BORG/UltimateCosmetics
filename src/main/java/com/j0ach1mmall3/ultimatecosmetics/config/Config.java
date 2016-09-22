package com.j0ach1mmall3.ultimatecosmetics.config;

import com.j0ach1mmall3.jlib.gui.Gui;
import com.j0ach1mmall3.jlib.gui.GuiPage;
import com.j0ach1mmall3.jlib.gui.events.GuiClickEvent;
import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 20/08/2015
 */
public final class Config extends ConfigLoader<Main> {
    private final boolean updateChecker;
    private final JLogger.LogLevel logLevel;
    private final boolean removeCosmeticsOnWorldChange;
    private final boolean removeCosmeticsOnLogOut;
    private final List<String> worldsBlacklist;
    private final boolean disableBatSounds;
    private final boolean giveItemOnJoin;
    private final JLibItem joinItem;
    private final Sound guiOpenSound;
    private final Sound guiClickSound;
    private final String cosmeticsCommand;
    private final Gui gui;

    private final boolean auras;
    private final boolean balloons;
    private final boolean bannercapes;
    private final boolean banners;
    private final boolean blockpets;
    private final boolean bowtrails;
    private final boolean cloaks;
    private final boolean fireworks;
    private final boolean gadgets;
    private final boolean hats;
    private final boolean hearts;
    private final boolean morphs;
    private final boolean mounts;
    private final boolean music;
    private final boolean particles;
    private final boolean pets;
    private final boolean trails;
    private final boolean wardrobe;

    public Config(Main plugin) {
        super("config.yml", plugin);
        this.logLevel = JLogger.LogLevel.valueOf(this.config.getString("LogLevel"));
        this.updateChecker = this.config.getBoolean("UpdateChecker");
        if (!this.updateChecker) plugin.getjLogger().log(ChatColor.GOLD + "Update Checking is not enabled! You will not receive console notifications!", JLogger.LogLevel.EXTENDED);
        this.removeCosmeticsOnWorldChange = this.config.getBoolean("RemoveCosmeticsOnWorldChange");
        if (!this.removeCosmeticsOnWorldChange) plugin.getjLogger().log(ChatColor.GOLD + "Removing Cosmetics on World change is not enabled! Players will keep their Cosmetics when they change Worlds!", JLogger.LogLevel.EXTENDED);
        this.removeCosmeticsOnLogOut = this.config.getBoolean("RemoveCosmeticsOnLogOut");
        if (!this.removeCosmeticsOnLogOut) plugin.getjLogger().log(ChatColor.GOLD + "Removing Cosmetics on logout is not enabled! Players will keep their Cosmetics when they log out!", JLogger.LogLevel.EXTENDED);
        this.worldsBlacklist = this.config.getStringList("WorldsBlacklist");
        this.disableBatSounds = this.config.getBoolean("DisableBatSounds");
        this.giveItemOnJoin = this.config.getBoolean("GiveItemOnJoin");
        this.joinItem = this.storage.getJLibItem(this.config, "JoinItem");
        this.guiOpenSound = this.config.getString("GUIOpenSound").isEmpty() ? null : Sound.valueOf(this.config.getString("GUIOpenSound"));
        this.guiClickSound = this.config.getString("GUIClickSound").isEmpty() ? null : Sound.valueOf(this.config.getString("GUIClickSound"));
        this.cosmeticsCommand = this.config.getString("CosmeticsCommand");
        this.gui = this.loadGui();

        this.auras = this.getEnabled("Auras");
        this.balloons = this.getEnabled("Balloons");
        this.bannercapes = this.getEnabled("BannerCapes");
        this.banners = this.getEnabled("Banners");
        this.blockpets = this.getEnabled("BlockPets");
        this.bowtrails = this.getEnabled("BowTrails");
        this.cloaks = this.getEnabled("Cloaks");
        this.fireworks = this.getEnabled("Fireworks");
        this.gadgets = this.getEnabled("Gadgets");
        this.hats = this.getEnabled("Hats");
        this.hearts = this.getEnabled("Hearts");
        this.morphs = this.getEnabled("Morphs");
        this.mounts = this.getEnabled("Mounts");
        this.music = this.getEnabled("Music");
        this.particles = this.getEnabled("Particles");
        this.pets = this.getEnabled("Pets");
        this.trails = this.getEnabled("Trails");
        this.wardrobe = this.getEnabled("Wardrobe");

        plugin.getjLogger().log(ChatColor.GREEN + "Main config successfully loaded!", JLogger.LogLevel.EXTENDED);
    }

    public boolean isUpdateChecker() {
        return this.updateChecker;
    }

    public JLogger.LogLevel getLogLevel() {
        return this.logLevel;
    }

    public boolean isRemoveCosmeticsOnWorldChange() {
        return this.removeCosmeticsOnWorldChange;
    }

    public boolean isRemoveCosmeticsOnLogOut() {
        return this.removeCosmeticsOnLogOut;
    }

    public List<String> getWorldsBlacklist() {
        return this.worldsBlacklist;
    }

    public boolean isDisableBatSounds() {
        return this.disableBatSounds;
    }

    public boolean isGiveItemOnJoin() {
        return this.giveItemOnJoin;
    }

    public JLibItem getJoinItem() {
        return this.joinItem;
    }

    public Sound getGuiOpenSound() {
        return this.guiOpenSound;
    }

    public Sound getGuiClickSound() {
        return this.guiClickSound;
    }

    public String getCosmeticsCommand() {
        return this.cosmeticsCommand;
    }

    public Gui getGui() {
        return this.gui;
    }

    public boolean isAuras() {
        return this.auras;
    }

    public boolean isBalloons() {
        return this.balloons;
    }

    public boolean isBannercapes() {
        return this.bannercapes;
    }

    public boolean isBanners() {
        return this.banners;
    }

    public boolean isBlockpets() {
        return this.blockpets;
    }

    public boolean isBowtrails() {
        return this.bowtrails;
    }

    public boolean isCloaks() {
        return this.cloaks;
    }

    public boolean isFireworks() {
        return this.fireworks;
    }

    public boolean isGadgets() {
        return this.gadgets;
    }

    public boolean isHats() {
        return this.hats;
    }

    public boolean isHearts() {
        return this.hearts;
    }

    public boolean isMorphs() {
        return this.morphs;
    }

    public boolean isMounts() {
        return this.mounts;
    }

    public boolean isMusic() {
        return this.music;
    }

    public boolean isParticles() {
        return this.particles;
    }

    public boolean isPets() {
        return this.pets;
    }

    public boolean isTrails() {
        return this.trails;
    }

    public boolean isWardrobe() {
        return this.wardrobe;
    }

    private boolean getEnabled(String cosmetic) {
        boolean enabled = this.config.getBoolean("Cosmetics." + cosmetic);
        switch (cosmetic) {
            case "Auras":
                if(enabled && !ReflectionAPI.verBiggerThan(1, 9)) {
                    this.storage.getPlugin().getjLogger().log(ChatColor.RED + "Auras are enabled in the config, but you are running 1.8 or lower. Adjusting that value.", JLogger.LogLevel.MINIMAL);
                    enabled = false;
                }
                break;
            case "BannerCapes":
                if(enabled && !ReflectionAPI.verBiggerThan(1, 8)) {
                    this.storage.getPlugin().getjLogger().log(ChatColor.RED + "BannerCapes are enabled in the config, but you are running 1.7 or lower. Adjusting that value.", JLogger.LogLevel.MINIMAL);
                    enabled = false;
                }
                break;
            case "Banners":
                if(enabled && !ReflectionAPI.verBiggerThan(1, 8)) {
                    this.storage.getPlugin().getjLogger().log(ChatColor.RED + "Banners are enabled in the config, but you are running 1.7 or lower. Adjusting that value.", JLogger.LogLevel.MINIMAL);
                    enabled = false;
                }
                break;
            case "Morphs":
                if(enabled && this.storage.getPlugin().getServer().getPluginManager().getPlugin("LibsDisguises") == null) {
                    this.storage.getPlugin().getjLogger().log(ChatColor.RED + "Morphs are enabled in the config, but LibsDisguises isn't installed. Adjusting that value.", JLogger.LogLevel.MINIMAL);
                    enabled = false;
                }
                break;
        }
        return enabled;
    }

    private Gui loadGui() {
        String name = Placeholders.parse(this.config.getString("CosmeticsGUI.Name"));
        int size = this.config.getInt("CosmeticsGUI.Size");
        GuiPage guiPage = new GuiPage(name, size / 9);
        for(final String s : this.storage.getKeys("CosmeticsGUI.Items")) {
            JLibItem jLibItem = this.storage.getJLibItem(this.config, "CosmeticsGUI.Items." + s);
            jLibItem.setGuiClickHandler(new CallbackHandler<GuiClickEvent>() {
                @Override
                public void callback(GuiClickEvent o) {
                    String cmd = Config.this.config.getString("CosmeticsGUI.Items." + s + ".Command");
                    PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(o.getPlayer(), cmd);
                    Bukkit.getPluginManager().callEvent(event);
                    if(!event.isCancelled()) o.getPlayer().performCommand(cmd);
                }
            });
            guiPage.addItem(jLibItem);
        }
        return new Gui(guiPage);
    }

    private String getCommandBySlot(int slot) {
        for (String s : this.storage.getKeys("CosmeticsGUI.Items")) {
            if (this.config.getInt("CosmeticsGUI.Items." + s + ".Position") == slot) return this.config.getString("CosmeticsGUI.Items." + s + ".Command");
        }
        return null;
    }
}
