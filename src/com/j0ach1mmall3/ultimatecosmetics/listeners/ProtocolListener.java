package com.j0ach1mmall3.ultimatecosmetics.listeners;

import com.comphenix.tinyprotocol.TinyProtocol;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import com.j0ach1mmall3.ultimatecosmetics.modules.balloons.Balloons;
import com.j0ach1mmall3.ultimatecosmetics.modules.gadgets.Gadgets;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 6/09/2015
 */
public final class ProtocolListener extends TinyProtocol {
    private final Main plugin;

    public ProtocolListener(Main plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public Object onPacketOutAsync(Player reciever, Channel channel, Object packet) {
        if (ReflectionAPI.getNmsClass("PacketPlayOutNamedSoundEffect").isInstance(packet)) {
            try {
                String name;
                Field f = packet.getClass().getDeclaredField("a");
                f.setAccessible(true);
                if(ReflectionAPI.verBiggerThan(1, 9)) {
                    Object soundEffect = f.get(packet);
                    Field b = soundEffect.getClass().getDeclaredField("b");
                    b.setAccessible(true);
                    Object key = b.get(soundEffect);
                    name = ((String) key.getClass().getMethod("toString").invoke(key)).split(":")[1];
                } else name = (String) f.get(packet);
                CosmeticConfig balloons = this.plugin.getConfigByType(CosmeticType.BALLOON);
                CosmeticConfig gadgets = this.plugin.getConfigByType(CosmeticType.GADGET);
                if ("mob.bat.idle".equals(name) && ((balloons != null && ((Balloons) balloons).isDisableBatSounds()) || (gadgets != null && ((Gadgets) gadgets).isDisableBatSounds()))) return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return packet;
        }
        return super.onPacketOutAsync(reciever, channel, packet);
    }
}
