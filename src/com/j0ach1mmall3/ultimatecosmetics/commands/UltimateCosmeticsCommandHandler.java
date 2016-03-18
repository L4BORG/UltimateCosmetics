package com.j0ach1mmall3.ultimatecosmetics.commands;

import com.j0ach1mmall3.jlib.commands.CommandHandler;
import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.logging.JLogger;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.plugin.modularization.PluginModule;
import com.j0ach1mmall3.jlib.storage.StorageAction;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class UltimateCosmeticsCommandHandler extends CommandHandler {
    private final Main plugin;

    public UltimateCosmeticsCommandHandler(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    protected boolean handleCommand(final CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
            commandSender.sendMessage(ChatColor.GOLD + this.plugin.getDescription().getName() + ' ' + ChatColor.DARK_PURPLE + this.plugin.getDescription().getVersion());
            commandSender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.DARK_PURPLE + this.plugin.getDescription().getAuthors().get(0));
            commandSender.sendMessage(ChatColor.GOLD + this.plugin.getDescription().getDescription());
            commandSender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
            return true;
        }
        switch (strings[0].toLowerCase()) {
            case "reload":
                if(!commandSender.hasPermission("uc.reload")) {
                    commandSender.sendMessage(Placeholders.parse(this.plugin.getLang().getCommandNoPermission()));
                    return true;
                }
                this.plugin.reload();
                commandSender.sendMessage(ChatColor.GREEN + "Successfully reloaded the config!");
                return true;
            case "openmenu":
                if(!commandSender.hasPermission("uc.openmenu")) {
                    commandSender.sendMessage(Placeholders.parse(this.plugin.getLang().getCommandNoPermission()));
                    return true;
                }
                if(strings.length < 2) {
                    commandSender.sendMessage(ChatColor.RED + "Usage: /uc openmenu <player>");
                    return true;
                }
                Player player = General.getPlayerByName(strings[0], false);
                if(player == null) {
                    commandSender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                this.plugin.getGuiHandler().openMainGui(player);
                commandSender.sendMessage(ChatColor.GREEN + "Successfully opened the Main Menu for " + player.getName() + '!');
                return true;
            case "debug":
                if (!commandSender.hasPermission("uc.debug")) {
                    commandSender.sendMessage(Placeholders.parse(this.plugin.getLang().getCommandNoPermission()));
                    return true;
                }
                List<ConfigLoader> configs = Arrays.asList(this.plugin.getBabies(), this.plugin.getLang(), this.plugin.getMisc());
                for(PluginModule pluginModule : this.plugin.getEnabledModules()) {
                    configs.add(pluginModule.getConfig());
                }
                new JLogger(this.plugin).dumpDebug(
                        this.plugin.getDataLoader().getStorage().getActions().toArray(new StorageAction[this.plugin.getDataLoader().getStorage().getActions().size()]),
                        configs.toArray(new ConfigLoader[configs.size()]), new CallbackHandler<String>() {
                    @Override
                    public void callback(String o) {
                        commandSender.sendMessage(ChatColor.GREEN + "Debug dump can be found at " + ChatColor.GOLD + o);
                    }
                });
                return true;
        }
        return true;
    }
}
