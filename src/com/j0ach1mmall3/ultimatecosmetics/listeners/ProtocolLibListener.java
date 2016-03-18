package com.j0ach1mmall3.ultimatecosmetics.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import com.j0ach1mmall3.ultimatecosmetics.modules.balloons.Balloons;
import com.j0ach1mmall3.ultimatecosmetics.modules.gadgets.Gadgets;

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
        PacketContainer packet = event.getPacket();
        String sound = packet.getStrings().read(0);
        CosmeticConfig balloons = this.plugin.getConfigByType(CosmeticType.BALLOON);
        CosmeticConfig gadgets = this.plugin.getConfigByType(CosmeticType.GADGET);
        if ("mob.bat.idle".equals(sound) && ((balloons != null && ((Balloons) balloons).isDisableBatSounds()) || (gadgets != null && ((Gadgets) gadgets).isDisableBatSounds()))) event.setCancelled(true);
    }
}
