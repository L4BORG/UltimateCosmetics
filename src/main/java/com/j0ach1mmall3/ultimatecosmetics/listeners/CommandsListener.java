package com.j0ach1mmall3.ultimatecosmetics.listeners;

import com.j0ach1mmall3.jlib.player.JLibPlayer;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 20/08/2015
 */
public final class CommandsListener implements Listener {
    private final Main plugin;

    public CommandsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();
        Player p = e.getPlayer();
        if (message.equalsIgnoreCase(this.plugin.getBabies().getCosmeticsCommand())) {
            e.setCancelled(true);
            this.plugin.getBabies().getGui().open(p);
            if(this.plugin.getBabies().getGuiOpenSound() != null) new JLibPlayer(p).playSound(this.plugin.getBabies().getGuiOpenSound());
            return;
        }
        for(PluginModule<Main, CosmeticConfig> pluginModule : this.plugin.getModules()) {
            if(pluginModule.isEnabled() && pluginModule.getConfig().getCommand().equalsIgnoreCase(message)) {
                e.setCancelled(true);
                if(this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName()) || pluginModule.getConfig().getWorldsBlacklist().contains(p.getWorld().getName())) Methods.informPlayerNoPermission(p, this.plugin.getLang().getNotEnabled());
                else {
                    pluginModule.getConfig().getGui(p).open(p);
                    if(this.plugin.getBabies().getGuiOpenSound() != null) new JLibPlayer(p).playSound(this.plugin.getBabies().getGuiOpenSound());
                }
                break;
            }
        }
    }
}
