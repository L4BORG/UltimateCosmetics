package com.j0ach1mmall3.ultimatecosmetics.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.GUI;
import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 20/08/2015
 */
public final class Config extends ConfigLoader {
    private final boolean updateChecker;
    private final JLogger.LogLevel logLevel;
    private final boolean removeCosmeticsOnWorldChange;
    private final boolean removeCosmeticsOnLogOut;
    private final List<String> worldsBlacklist;
    private final boolean giveItemOnJoin;
    private final GuiItem joinItem;
    private Sound guiOpenSound;
    private Sound guiClickSound;
    private final String cosmeticsCommand;

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
        this.giveItemOnJoin = this.config.getBoolean("GiveItemOnJoin");
        this.joinItem = this.customConfig.getGuiItemNew(this.config, "JoinItem");
        if (!this.config.getString("GUIOpenSound").isEmpty()) this.guiOpenSound = Sound.valueOf(this.config.getString("GUIOpenSound"));
        if (!this.config.getString("GUIClickSound").isEmpty()) this.guiClickSound = Sound.valueOf(this.config.getString("GUIClickSound"));
        this.cosmeticsCommand = this.config.getString("CosmeticsCommand");

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

    private boolean getEnabled(String cosmetic) {
        boolean enabled = this.config.getBoolean("Cosmetics." + cosmetic);
        switch (cosmetic) {
            case "Auras":
                if(enabled && !ReflectionAPI.verBiggerThan(1, 9)) {
                    ((Main) this.storage.getPlugin()).getjLogger().log(ChatColor.RED + "Auras are enabled in the config, but you are running 1.8 or lower. Adjusting that value.", JLogger.LogLevel.MINIMAL);
                    enabled = false;
                }
                break;
            case "BannerCapes":
                if(enabled && !ReflectionAPI.verBiggerThan(1, 8)) {
                    ((Main) this.storage.getPlugin()).getjLogger().log(ChatColor.RED + "BannerCapes are enabled in the config, but you are running 1.7 or lower. Adjusting that value.", JLogger.LogLevel.MINIMAL);
                    enabled = false;
                }
                break;
            case "Banners":
                if(enabled && !ReflectionAPI.verBiggerThan(1, 8)) {
                    ((Main) this.storage.getPlugin()).getjLogger().log(ChatColor.RED + "Banners are enabled in the config, but you are running 1.7 or lower. Adjusting that value.", JLogger.LogLevel.MINIMAL);
                    enabled = false;
                }
                break;
            case "Morphs":
                if(enabled && this.storage.getPlugin().getServer().getPluginManager().getPlugin("LibsDisguises") == null) {
                    ((Main) this.storage.getPlugin()).getjLogger().log(ChatColor.RED + "Morphs are enabled in the config, but LibsDisguises isn't installed. Adjusting that value.", JLogger.LogLevel.MINIMAL);
                    enabled = false;
                }
                break;
        }
        return enabled;
    }

    private GUI loadGui() {
        String name = Placeholders.parse(this.config.getString("CosmeticsGUI.Name"));
        int size = this.config.getInt("CosmeticsGUI.Size");
        GUI gui = new GUI(name, size);
        for(String s : this.customConfig.getKeys("CosmeticsGUI.Items")) {
            gui.setItem(this.customConfig.getGuiItemNew(this.config, "CosmeticsGUI.Items." + s));
        }
        return gui;
    }

    public String getCommandBySlot(int slot) {
        for (String s : this.customConfig.getKeys("CosmeticsGUI.Items")) {
            if (this.config.getInt("CosmeticsGUI.Items." + s + ".Position") == slot) return this.config.getString("CosmeticsGUI.Items." + s + ".Command");
        }
        return "";
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

    public boolean isGiveItemOnJoin() {
        return this.giveItemOnJoin;
    }

    public GuiItem getJoinItem() {
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

    public GUI getCosmeticsGui() {
        return this.loadGui();
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
}
