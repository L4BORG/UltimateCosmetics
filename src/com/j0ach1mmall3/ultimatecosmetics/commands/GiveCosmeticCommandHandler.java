package com.j0ach1mmall3.ultimatecosmetics.commands;

import com.j0ach1mmall3.jlib.commands.CommandHandler;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class GiveCosmeticCommandHandler extends CommandHandler {
    private final Main plugin;

    public GiveCosmeticCommandHandler(Main plugin) {
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
        CosmeticType type;
        try {
            type = CosmeticType.valueOf(strings[1].toUpperCase());
        } catch (Exception e) {
            commandSender.sendMessage(ChatColor.RED + "Invalid CosmeticType!");
            return true;
        }
        CosmeticConfig config = this.plugin.getConfigByType(type);
        if(config == null) {
            commandSender.sendMessage(ChatColor.RED + "CosmeticType isn't enabled!");
            return true;
        }
        CosmeticStorage cosmeticStorage = config.getByIdentifier(strings[2]);
        if(cosmeticStorage == null) {
            commandSender.sendMessage(ChatColor.RED + "Invalid Cosmetic!");
            return true;
        }
        Cosmetic cosmetic = config.getCosmetic(cosmeticStorage, player);
        cosmetic.give();
        commandSender.sendMessage(ChatColor.GREEN + "Successfully gave " + type.name() + ' ' + cosmeticStorage.getIdentifier() + " to " + player.getName());
        return true;
    }
}
