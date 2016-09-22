package com.j0ach1mmall3.ultimatecosmetics.commands;

import com.j0ach1mmall3.jlib.commands.CommandHandler;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class GiveCosmeticCommandHandler extends CommandHandler<Main> {
    public GiveCosmeticCommandHandler(Main plugin) {
        super(plugin);
    }

    @Override
    protected boolean handleCommand(CommandSender commandSender, String[] strings) {
        if(strings.length < 3) {
            commandSender.sendMessage(this.command.getUsage());
            return true;
        }
        Player player = General.getPlayerByName(strings[0], false);
        if(player == null) {
            commandSender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }
        for(PluginModule<Main, CosmeticConfig> pluginModule : this.plugin.getModules()) {
            if(pluginModule.isEnabled() && pluginModule.getConfig().getCosmeticClass().getSimpleName().equalsIgnoreCase(strings[1])) {
                CosmeticStorage cosmeticStorage = pluginModule.getConfig().getByIdentifier(strings[2]);
                if(cosmeticStorage == null) {
                    commandSender.sendMessage(ChatColor.RED + "Invalid Cosmetic!");
                    return true;
                }
                Cosmetic cosmetic = pluginModule.getConfig().getCosmetic(cosmeticStorage, player);
                cosmetic.give(this.plugin);
                commandSender.sendMessage(ChatColor.GREEN + "Successfully gave " + strings[1] + ' ' + cosmeticStorage.getIdentifier() + " to " + player.getName());
            }
        }
        return true;
    }
}
