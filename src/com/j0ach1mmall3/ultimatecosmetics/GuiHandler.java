package com.j0ach1mmall3.ultimatecosmetics;

import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.Config;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 3/10/2015
 */
public final class GuiHandler implements Listener {
    private final Main plugin;
    private final Map<Player, Integer> pages = new HashMap<>();

    public GuiHandler(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(!this.pages.containsKey(p)) return;
        if(((Config) this.plugin.getBabies()).getCosmeticsGui().hasClicked(e)) {
            e.setCancelled(true);
            String cmd = ((Config) this.plugin.getBabies()).getCommandBySlot(e.getSlot());
            PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(p, cmd);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()) p.performCommand(cmd);
        }
        for(CosmeticType type : CosmeticType.values()) {
            CosmeticConfig config = this.plugin.getConfigByType(type);
            if(config != null) config.handleClick(type, e, this.pages.get(p));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        this.pages.remove(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        this.pages.remove(e.getPlayer());
    }

    public void openMainGui(Player player) {
        Config config = (Config) this.plugin.getBabies();
        config.getCosmeticsGui().open(player);
        this.pages.put(player, 0);
    }

    public void openGui(Player player, CosmeticType cosmeticType, int page) {
        CosmeticConfig config = this.plugin.getConfigByType(cosmeticType);
        if(config != null) {
            config.openGui(cosmeticType, player, page);
            this.pages.put(player, page);
        }
    }
}
