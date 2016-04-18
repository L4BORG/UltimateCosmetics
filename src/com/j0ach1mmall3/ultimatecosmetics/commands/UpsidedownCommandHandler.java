package com.j0ach1mmall3.ultimatecosmetics.commands;

import com.j0ach1mmall3.jlib.commands.CommandHandler;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.command.CommandSender;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/04/2016
 */
public final class UpsidedownCommandHandler extends CommandHandler {
    private final Main plugin;

    public UpsidedownCommandHandler(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    protected boolean handleCommand(CommandSender commandSender, String[] strings) {
        return true;
    }
}
