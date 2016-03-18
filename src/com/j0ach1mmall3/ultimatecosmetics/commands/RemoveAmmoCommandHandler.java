package com.j0ach1mmall3.ultimatecosmetics.commands;

import com.j0ach1mmall3.jlib.commands.CommandHandler;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class RemoveAmmoCommandHandler extends CommandHandler {
    private final Main plugin;

    public RemoveAmmoCommandHandler(Main plugin) {
        this.plugin = plugin;
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
        this.plugin.getDataLoader().takeAmmo(strings[1], player, Parsing.parseInt(strings[2]));
        commandSender.sendMessage(ChatColor.GREEN + "Successfully removed " + Parsing.parseInt(strings[2]) + ' ' + strings[1] + " Ammo from " + player.getName() + '!');
        return true;
    }
}
