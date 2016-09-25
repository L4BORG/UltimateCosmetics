package com.j0ach1mmall3.ultimatecosmetics.config;

import com.j0ach1mmall3.jlib.gui.Gui;
import com.j0ach1mmall3.jlib.gui.GuiPage;
import com.j0ach1mmall3.jlib.gui.MultiPageGui;
import com.j0ach1mmall3.jlib.gui.events.GuiClickEvent;
import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.player.JLibPlayer;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public abstract class CosmeticConfig<C extends CosmeticStorage> extends ConfigLoader<Main> {
    private final List<String> worldsBlacklist;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final String guiName;
    private final int guiSize;
    private final JLibItem noPermissionItem;
    private final boolean noPermissionItemEnabled;
    private final JLibItem removeItem;
    private final JLibItem homeItem;
    private final JLibItem previousItem;
    private final JLibItem nextItem;
    protected final List<C> cosmetics;

    protected CosmeticConfig(String config, final Main plugin, String section) {
        super(config, plugin);
        this.worldsBlacklist = this.config.getStringList("WorldsBlacklist");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = this.config.getString("GiveSound") == null ? null : Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = this.config.getString("RemoveSound") == null ? null : Sound.valueOf(this.config.getString("RemoveSound"));
        this.guiName = this.config.getString("GUIName");
        this.guiSize = this.config.getInt("GUISize");
        this.noPermissionItem = this.storage.getJLibItem(this.config, "NoPermissionItem");
        this.noPermissionItemEnabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.removeItem = this.storage.getJLibItem(this.config, "RemoveItem");
        this.removeItem.setGuiClickHandler(new CallbackHandler<GuiClickEvent>() {
            @Override
            public void callback(GuiClickEvent o) {
                Player p = o.getPlayer();
                for(Cosmetic cosmetic : plugin.getApi().getCosmetics(p, CosmeticConfig.this.getCosmeticClass())) {
                    cosmetic.remove(plugin);
                }
                if(CosmeticConfig.this.removeSound != null) new JLibPlayer(p).playSound(CosmeticConfig.this.removeSound);
            }
        });
        this.homeItem = this.storage.getJLibItem(this.config, "HomeItem");
        this.homeItem.setGuiClickHandler(new CallbackHandler<GuiClickEvent>() {
            @Override
            public void callback(GuiClickEvent o) {
                Player p = o.getPlayer();
                plugin.getBabies().getGui().open(p, 0);
                if(plugin.getBabies().getGuiClickSound() != null) new JLibPlayer(p).playSound(plugin.getBabies().getGuiClickSound());
            }
        });
        this.previousItem = this.storage.getJLibItem(this.config, "PreviousItem");
        this.previousItem.setGuiClickHandler(new CallbackHandler<GuiClickEvent>() {
            @Override
            public void callback(GuiClickEvent o) {
                if(plugin.getBabies().getGuiClickSound() != null) new JLibPlayer(o.getPlayer()).playSound(plugin.getBabies().getGuiClickSound());
            }
        });
        this.nextItem = this.storage.getJLibItem(this.config, "NextItem");
        this.nextItem.setGuiClickHandler(new CallbackHandler<GuiClickEvent>() {
            @Override
            public void callback(GuiClickEvent o) {
                if(plugin.getBabies().getGuiClickSound() != null) new JLibPlayer(o.getPlayer()).playSound(plugin.getBabies().getGuiClickSound());
            }
        });
        this.cosmetics = this.loadCosmetics(section);
        plugin.getjLogger().log(ChatColor.GREEN + section + " config successfully loaded!", JLogger.LogLevel.EXTENDED);
    }

    public List<String> getWorldsBlacklist() {
        return this.worldsBlacklist;
    }

    public String getCommand() {
        return this.command;
    }

    public String getNoPermissionMessage() {
        return this.noPermissionMessage;
    }

    public Sound getGiveSound() {
        return this.giveSound;
    }

    public Sound getRemoveSound() {
        return this.removeSound;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public int getGuiSize() {
        return this.guiSize;
    }

    public JLibItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public boolean isNoPermissionItemEnabled() {
        return this.noPermissionItemEnabled;
    }

    public JLibItem getRemoveItem() {
        return this.removeItem;
    }

    public JLibItem getHomeItem() {
        return this.homeItem;
    }

    public JLibItem getPreviousItem() {
        return this.previousItem;
    }

    public JLibItem getNextItem() {
        return this.nextItem;
    }

    public List<C> getCosmetics() {
        return this.cosmetics;
    }

    public final JLibItem getNoPermissionItem(C cosmeticStorage, int guiPosition) {
        ItemStack itemStack = this.noPermissionItem.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if("%cosmeticsname%".equals(itemMeta.getDisplayName())) itemMeta.setDisplayName(cosmeticStorage.getjLibItem().getItemStack().getItemMeta().getDisplayName());
        itemStack.setItemMeta(itemMeta);
        return new JLibItem(itemStack, guiPosition, new CallbackHandler<GuiClickEvent>() {
            @Override
            public void callback(GuiClickEvent o) {
                Methods.informPlayerNoPermission(o.getPlayer(), CosmeticConfig.this.noPermissionMessage);
            }
        });
    }

    public final C getByIdentifier(String identifier) {
        for(C cosmeticStorage : this.cosmetics) {
            if(cosmeticStorage.getIdentifier().equals(identifier)) return cosmeticStorage;
        }
        return null;
    }

    public final C getByPosition(int page, int position) {
        for(C cosmeticStorage : this.cosmetics) {
            if(cosmeticStorage.getjLibItem().getGuiPosition() == position + page * this.guiSize) return cosmeticStorage;
        }
        return null;
    }

    public final Gui getGui(Player player) {
        final Main plugin = this.storage.getPlugin();
        List<GuiPage> pages = new ArrayList<>();
        for(C cosmeticStorage : this.cosmetics) {
            JLibItem jLibItem = cosmeticStorage.getjLibItem();
            jLibItem = new JLibItem(jLibItem.getItemStack().clone(), jLibItem.isAsteriskItem(), jLibItem.getGuiPosition(), jLibItem.getGuiClickHandler());
            int page = jLibItem.getGuiPosition() / this.guiSize;
            int position = jLibItem.getGuiPosition() - page * this.guiSize;
            while (page >= pages.size()) {
                pages.add(new GuiPage(Placeholders.parse(this.guiName, player), this.guiSize / 9));
            }

            for(Cosmetic cosmetic : plugin.getApi().getCosmetics(player, this.getCosmeticClass())) {
                if(cosmetic.getCosmeticStorage().getIdentifier().equals(cosmeticStorage.getIdentifier())) jLibItem.getItemStack().addEnchantment(plugin.getGlow(), 1);
            }

            jLibItem.setGuiClickHandler(new CallbackHandler<GuiClickEvent>() {
                @Override
                public void callback(GuiClickEvent o) {
                    Player p = o.getPlayer();
                    C cosmeticStorage = CosmeticConfig.this.getByPosition(o.getGuiPage(), o.getInventoryClickEvent().getSlot());
                    if(cosmeticStorage != null) {
                        if(new JLibPlayer(p).hasCustomPermission(cosmeticStorage.getPermission())) {
                            CosmeticConfig.this.getCosmetic(cosmeticStorage, p).give(plugin);
                            if(CosmeticConfig.this.giveSound != null) new JLibPlayer(p).playSound(CosmeticConfig.this.giveSound);
                        } else Methods.informPlayerNoPermission(o.getPlayer(), CosmeticConfig.this.noPermissionMessage);
                    }
                }
            });

            if (this.noPermissionItemEnabled && !new JLibPlayer(player).hasCustomPermission(cosmeticStorage.getPermission())) jLibItem = this.getNoPermissionItem(cosmeticStorage, position);

            pages.get(page).addItem(position, jLibItem);
        }
        for(GuiPage guiPage : pages) {
            guiPage.addItem(this.removeItem);
            guiPage.addItem(this.homeItem);
        }
        return new MultiPageGui(this.previousItem, this.nextItem, pages.toArray(new GuiPage[pages.size()]));
    }

    public abstract Class<? extends Cosmetic> getCosmeticClass();

    public abstract Cosmetic getCosmetic(C cosmeticStorage, Player player);

    protected C getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return (C) new CosmeticStorage(
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getJLibItem(this.config, path)
        );
    }

    private List<C> loadCosmetics(String section) {
        List<C> cosmeticStorages = new ArrayList<>();
        for(String s : this.storage.getKeys(section)) {
            cosmeticStorages.add(this.getCosmeticStorageByIdentifier(section, s));
        }
        return cosmeticStorages;
    }
}
