package com.j0ach1mmall3.ultimatecosmetics;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.player.JLibPlayer;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class Methods {
    public static final Map<String, Integer> DEFAULT_AMMO = new HashMap<>();

    static {
        for(String s :  new String[]{"Enderbow", "EtherealPearl", "PaintballGun", "FlyingPig", "BatBlaster", "CATapult", "RailGun", "CryoTube", "Rocket", "PoopBomb", "GrapplingHook", "SelfDestruct", "SlimeVasion", "FunGun", "MelonThrower", "ColorBomb", "FireTrail", "DiamondShower", "GoldFountain", "PaintTrail"}) {
            DEFAULT_AMMO.put(s, 0);
        }
    }

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


    public static void playSafeParticle(Player player, Location location, Effect particle, int id, int data, float xOff, float yOff, float zOff, float speed, int amount, int viewDistance) {
        if (ReflectionAPI.verBiggerThan(1, 8)) player.spigot().playEffect(location, particle, id, data, xOff, yOff, zOff, speed, amount, viewDistance);
        else player.playEffect(location, particle, data);
    }

    public static void informPlayerNoPermission(CommandSender s, String message) {
        if(message.isEmpty()) return;
        if (s instanceof Player) {
            Player p = (Player) s;
            p.sendMessage(Placeholders.parse(message, p));
            new JLibPlayer(p).playNote(Instrument.BASS_DRUM, Note.Tone.A);
        } else s.sendMessage(Placeholders.parse(message, null));
    }

    public static void setPassenger(Entity ent, Entity passenger) {
        ent.setPassenger(passenger);
        if(passenger instanceof Player) {
            try {
                Object packetPlayOutMount = ReflectionAPI.getNmsClass("PacketPlayOutMount").getConstructor(ReflectionAPI.getNmsClass("Entity")).newInstance(ReflectionAPI.getHandle((Object) ent));
                ReflectionAPI.sendPacket((Player) passenger, packetPlayOutMount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}