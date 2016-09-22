package com.j0ach1mmall3.ultimatecosmetics.commands;

import com.j0ach1mmall3.jlib.commands.CommandHandler;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class RemoveCosmeticCommandHandler extends CommandHandler<Main> {
    public RemoveCosmeticCommandHandler(Main plugin) {
        super(plugin);
    }

    @Override
    protected boolean handleCommand(CommandSender commandSender, String[] strings) {
        if(strings.length < 2) {
            commandSender.sendMessage(this.command.getUsage());
            return true;
        }
        Player player = General.getPlayerByName(strings[0], false);
        if(player == null) {
            commandSender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }
        for(Cosmetic cosmetic : this.plugin.getApi().getCosmetics(player)) {
            if(cosmetic.getClass().getSimpleName().equalsIgnoreCase(strings[1])) cosmetic.remove(this.plugin);
        }
        commandSender.sendMessage(ChatColor.GREEN + "Successfully removed " + player.getName() + "'s " + strings[1] + '!');
        return true;
    }
}
