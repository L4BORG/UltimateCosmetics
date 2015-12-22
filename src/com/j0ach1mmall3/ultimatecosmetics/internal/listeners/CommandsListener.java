package com.j0ach1mmall3.ultimatecosmetics.internal.listeners;

import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.*;
import com.j0ach1mmall3.ultimatecosmetics.internal.gui.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * Created by j0ach1mmall3 on 18:26 20/08/2015 using IntelliJ IDEA.
 */
public final class CommandsListener implements Listener {
    private final Main plugin;

    public CommandsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent e) {
        String message = e.getCommand();
        CommandSender sender = e.getSender();
        if("reload".equalsIgnoreCase(message) || "bukkit:reload".equalsIgnoreCase(message) || message.startsWith("reload ") || message.startsWith("bukkit:reload ")) {
            sender.sendMessage(ChatColor.RED + "UltimateCosmetics does NOT support reloads! Please restart the server instead!");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();
        Player p = e.getPlayer();
        if("reload".equalsIgnoreCase(message) || "bukkit:reload".equalsIgnoreCase(message) || message.startsWith("reload ") || message.startsWith("bukkit:reload ")) {
            p.sendMessage(ChatColor.RED + "UltimateCosmetics does NOT support reloads! Please restart the server instead!");
            e.setCancelled(true);
            return;
        }
        //Debug boolean if ever needed
        boolean debug = false;
        if(debug) return;
        Config config = this.plugin.getBabies();
        if (message.equalsIgnoreCase(config.getCosmeticsCommand())) {
            e.setCancelled(true);
            if (config.getGuiOpenSound() != null)
                Sounds.playSound(p, config.getGuiOpenSound());
            CosmeticsGuiHandler.open(p);
            return;
        }
        Balloons balloons = this.plugin.getBalloons();
        if (message.equalsIgnoreCase(balloons.getCommand())) {
            e.setCancelled(true);
            if (balloons.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !balloons.getWorldsBlacklist().contains(p.getWorld().getName())) BalloonsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Banners banners = this.plugin.getBanners();
        if (message.equalsIgnoreCase(banners.getCommand())) {
            e.setCancelled(true);
            if (banners.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !banners.getWorldsBlacklist().contains(p.getWorld().getName())) BannersGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Bowtrails bowtrails = this.plugin.getBowtrails();
        if (message.equalsIgnoreCase(bowtrails.getCommand())) {
            e.setCancelled(true);
            if (bowtrails.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !bowtrails.getWorldsBlacklist().contains(p.getWorld().getName())) BowtrailsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Fireworks fireworks = this.plugin.getFireworks();
        if (message.equalsIgnoreCase(fireworks.getCommand())) {
            e.setCancelled(true);
            if (fireworks.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !fireworks.getWorldsBlacklist().contains(p.getWorld().getName())) FireworksGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Gadgets gadgets = this.plugin.getGadgets();
        if (message.equalsIgnoreCase(gadgets.getCommand())) {
            e.setCancelled(true);
            if (gadgets.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !gadgets.getWorldsBlacklist().contains(p.getWorld().getName())) GadgetsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Hats hats = this.plugin.getHats();
        if (message.equalsIgnoreCase(hats.getCommand())) {
            e.setCancelled(true);
            if (hats.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !hats.getWorldsBlacklist().contains(p.getWorld().getName())) HatsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Hearts hearts = this.plugin.getHearts();
        if (message.equalsIgnoreCase(hearts.getCommand())) {
            e.setCancelled(true);
            if (hearts.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !hearts.getWorldsBlacklist().contains(p.getWorld().getName())) HeartsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Morphs morphs = this.plugin.getMorphs();
        if (message.equalsIgnoreCase(morphs.getCommand())) {
            e.setCancelled(true);
            if (morphs.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !morphs.getWorldsBlacklist().contains(p.getWorld().getName())) MorphsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Mounts mounts = this.plugin.getMounts();
        if (message.equalsIgnoreCase(mounts.getCommand())) {
            e.setCancelled(true);
            if (mounts.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !mounts.getWorldsBlacklist().contains(p.getWorld().getName())) MountsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Music music = this.plugin.getMusic();
        if (message.equalsIgnoreCase(music.getCommand())) {
            e.setCancelled(true);
            if (music.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !music.getWorldsBlacklist().contains(p.getWorld().getName())) MusicGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Particles particles = this.plugin.getParticles();
        if (message.equalsIgnoreCase(particles.getCommand())) {
            e.setCancelled(true);
            if (particles.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !particles.getWorldsBlacklist().contains(p.getWorld().getName())) ParticlesGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Pets pets = this.plugin.getPets();
        if (message.equalsIgnoreCase(pets.getCommand())) {
            e.setCancelled(true);
            if (pets.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !pets.getWorldsBlacklist().contains(p.getWorld().getName())) PetsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Trails trails = this.plugin.getTrails();
        if (message.equalsIgnoreCase(trails.getCommand())) {
            e.setCancelled(true);
            if (trails.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !trails.getWorldsBlacklist().contains(p.getWorld().getName())) TrailsGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
            return;
        }
        Wardrobe wardrobe = this.plugin.getWardrobe();
        if (message.equalsIgnoreCase(wardrobe.getCommand())) {
            e.setCancelled(true);
            if (wardrobe.isEnabled() && !config.getWorldsBlacklist().contains(p.getWorld().getName()) && !wardrobe.getWorldsBlacklist().contains(p.getWorld().getName())) WardrobeGuiHandler.open(p, 0);
            else this.plugin.informPlayerNotEnabled(p);
        }
    }
}
