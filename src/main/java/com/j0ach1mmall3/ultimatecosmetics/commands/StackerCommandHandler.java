package com.j0ach1mmall3.ultimatecosmetics.commands;

import com.j0ach1mmall3.jlib.commands.CommandHandler;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class StackerCommandHandler extends CommandHandler<Main> {
    public StackerCommandHandler(Main plugin) {
        super(plugin);
    }

    @Override
    protected boolean handleCommand(CommandSender commandSender, String[] strings) {
        final Player p = (Player) commandSender;
        this.plugin.getDataLoader().getStacker(p, new CallbackHandler<Boolean>() {
            @Override
            public void callback(Boolean o) {
                String toggled = StackerCommandHandler.this.plugin.getLang().getToggled();
                if(!toggled.isEmpty()) p.sendMessage(toggled.replace("%statuscolor%", (o ? ChatColor.RED : ChatColor.GREEN).toString()).replace("%type%", "Stacker"));
                StackerCommandHandler.this.plugin.getDataLoader().setStacker(p, !o);
            }
        });
        return true;
    }
}
