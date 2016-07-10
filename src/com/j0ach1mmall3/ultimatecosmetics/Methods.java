package com.j0ach1mmall3.ultimatecosmetics;

import java.util.HashMap;
import java.util.Map;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.methods.Notes;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class Methods {
    public static final Map<String, Integer> DEFAULT_AMMO = new HashMap<String, Integer>() {{
        for(String s :  new String[]{"Enderbow", "EtherealPearl", "PaintballGun", "FlyingPig", "BatBlaster", "CATapult", "RailGun", "CryoTube", "Rocket", "PoopBomb", "GrapplingHook", "SelfDestruct", "Slimevasion", "FunGun", "MelonThrower", "ColorBomb", "FireTrail", "DiamondShower", "GoldFountain", "PaintTrail"}) {
            this.put(s, 0);
        }
    }};

    private Methods() {
    }

    public static boolean isNoPermissionItem(ItemStack noPermissionItem, ItemStack item) {
        return item.hasItemMeta() && noPermissionItem.getType() == item.getType() && noPermissionItem.getDurability() == item.getDurability() && noPermissionItem.getItemMeta().getLore().equals(item.getItemMeta().getLore());
    }

    public static void broadcastSafeParticle(Location location, Effect particle, int id, int data, float speed, int amount, int viewDistance) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            playSafeParticle(p, location, particle, id, data, speed, amount, viewDistance);
        }
    }

    public static void broadcastSafeParticle(Location location, Effect particle, int id, int data, float xOff, float yOff, float zOff, float speed, int amount, int viewDistance) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            playSafeParticle(p, location, particle, id, data, xOff, yOff, zOff, speed, amount, viewDistance);
        }
    }

    public static void playSafeParticle(Player player, Location location, Effect particle, int id, int data, float speed, int amount, int viewDistance) {
        playSafeParticle(player, location, particle, id, data, 0.0F, 0.0F, 0.0F, speed, amount, viewDistance);
    }

    @SuppressWarnings("deprecation")
    public static void playSafeParticle(Player player, Location location, Effect particle, int id, int data, float xOff, float yOff, float zOff, float speed, int amount, int viewDistance) {
        if (ReflectionAPI.verBiggerThan(1, 8)) player.spigot().playEffect(location, particle, id, data, xOff, yOff, zOff, speed, amount, viewDistance);
        else player.playEffect(location, particle, data);
    }

    public static void informPlayerNoPermission(CommandSender s, String message) {
        if(message.isEmpty()) return;
        if (s instanceof Player) {
            Player p = (Player) s;
            p.sendMessage(Placeholders.parse(message, p));
            Notes.playNote(p, Instrument.BASS_DRUM, Note.Tone.A);
        } else s.sendMessage(Placeholders.parse(message, null));
    }
}
