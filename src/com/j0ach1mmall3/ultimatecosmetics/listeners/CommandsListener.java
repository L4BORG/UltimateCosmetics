package com.j0ach1mmall3.ultimatecosmetics.listeners;

import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.Config;
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
        if (message.equalsIgnoreCase(((Config) this.plugin.getBabies()).getCosmeticsCommand())) {
            e.setCancelled(true);
            this.plugin.getGuiHandler().openMainGui(p);
            if(((Config) this.plugin.getBabies()).getGuiOpenSound() != null) Sounds.playSound(p, ((Config) this.plugin.getBabies()).getGuiOpenSound());
            return;
        }
        for(CosmeticType type : CosmeticType.values()) {
            CosmeticConfig config = this.plugin.getConfigByType(type);
            if(config != null && config.getCommand().equalsIgnoreCase(message)) {
                e.setCancelled(true);
                if(((Config) this.plugin.getBabies()).getWorldsBlacklist().contains(p.getWorld().getName()) || config.getWorldsBlacklist().contains(p.getWorld().getName())) this.plugin.informPlayerNotEnabled(p);
                else {
                    this.plugin.getGuiHandler().openGui(p, type, 0);
                    if(((Config) this.plugin.getBabies()).getGuiOpenSound() != null) Sounds.playSound(p, ((Config) this.plugin.getBabies()).getGuiOpenSound());
                }
                break;
            }
        }
    }
}
