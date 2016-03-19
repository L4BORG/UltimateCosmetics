package com.j0ach1mmall3.ultimatecosmetics.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import com.j0ach1mmall3.ultimatecosmetics.modules.balloons.Balloons;
import com.j0ach1mmall3.ultimatecosmetics.modules.gadgets.Gadgets;

import java.lang.reflect.Field;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 4/10/2015
 */
public final class ProtocolLibListener extends PacketAdapter {
    private final Main plugin;

    public ProtocolLibListener(Main plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.NAMED_SOUND_EFFECT);
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Object packet = event.getPacket().getHandle();
        String name = null;
        try {
            Field f = packet.getClass().getDeclaredField("a");
            f.setAccessible(true);
            if(ReflectionAPI.verBiggerThan(1, 9)) {
                Object soundEffect = f.get(packet);
                Field b = soundEffect.getClass().getDeclaredField("b");
                b.setAccessible(true);
                Object key = b.get(soundEffect);
                name = ((String) key.getClass().getMethod("toString").invoke(key)).split(":")[1];
            } else name = (String) f.get(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CosmeticConfig balloons = this.plugin.getConfigByType(CosmeticType.BALLOON);
        CosmeticConfig gadgets = this.plugin.getConfigByType(CosmeticType.GADGET);
        if ("mob.bat.idle".equals(name) && ((balloons != null && ((Balloons) balloons).isDisableBatSounds()) || (gadgets != null && ((Gadgets) gadgets).isDisableBatSounds()))) event.setCancelled(true);
    }
}
