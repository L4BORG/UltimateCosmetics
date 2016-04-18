package com.j0ach1mmall3.ultimatecosmetics.commands;

import com.j0ach1mmall3.jlib.commands.CommandHandler;
import com.j0ach1mmall3.jlib.player.tagchanger.TagChanger;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.listeners.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/04/2016
 */
public final class EarsCommandHandler extends CommandHandler {
    private final Main plugin;

    public EarsCommandHandler(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    protected boolean handleCommand(CommandSender commandSender, String[] strings) {
        Player p = (Player) commandSender;
        if(this.plugin.getMisc().getRunnable().containsPlayer(p)) {
            TagChanger.unregisterTag(p.getUniqueId());
            if(p.getPassenger() instanceof ArmorStand) p.getPassenger().remove();
            this.plugin.getMisc().getRunnable().removePlayer(p);
        } else {
            TagChanger.registerTag(p.getUniqueId(), "deadmau5");
            ArmorStand armorStand = p.getWorld().spawn(p.getLocation(), ArmorStand.class);
            armorStand.setMarker(false);
            armorStand.setVisible(true);
            armorStand.setArms(false);
            armorStand.setBasePlate(false);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setCustomName(p.getName());
            armorStand.setCustomNameVisible(true);
            armorStand.setNoDamageTicks(Integer.MAX_VALUE);
            if(p.getPassenger() != null) p.getPassenger().leaveVehicle();
            PlayerListener.setPassenger(p, null);
            PlayerListener.setPassenger(p, armorStand);
            this.plugin.getMisc().getRunnable().addPlayer(p);
        }
        String toggled = this.plugin.getLang().getToggled();
        if(!toggled.isEmpty()) p.sendMessage(toggled.replace("%statuscolor%", (this.plugin.getMisc().getRunnable().containsPlayer(p) ? ChatColor.GREEN : ChatColor.RED).toString()).replace("%type%", "Ears"));
        return true;
    }
}
