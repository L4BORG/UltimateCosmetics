package com.j0ach1mmall3.ultimatecosmetics.listeners;

import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();
        Player p = e.getPlayer();
        //Debug boolean if ever needed
        boolean debug = false;
        if(debug) return;
        if (message.equalsIgnoreCase(this.plugin.getBabies().getCosmeticsCommand())) {
            e.setCancelled(true);
            this.plugin.getGuiHandler().openMainGui(p);
            if(this.plugin.getBabies().getGuiOpenSound() != null) Sounds.playSound(p, this.plugin.getBabies().getGuiOpenSound());
            return;
        }
        for(PluginModule<Main, CosmeticConfig> pluginModule : this.plugin.getModules()) {
            if(pluginModule.isEnabled() && pluginModule.getConfig().getCommand().equalsIgnoreCase(message)) {
                e.setCancelled(true);
                if(this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName()) || pluginModule.getConfig().getWorldsBlacklist().contains(p.getWorld().getName())) Methods.informPlayerNoPermission(p, this.plugin.getLang().getNotEnabled());
                else {
                    this.plugin.getGuiHandler().openGui(p, pluginModule.getConfig(), 0);
                    if(this.plugin.getBabies().getGuiOpenSound() != null) Sounds.playSound(p, this.plugin.getBabies().getGuiOpenSound());
                }
                break;
            }
        }
    }
}
