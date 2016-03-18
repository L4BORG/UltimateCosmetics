package com.j0ach1mmall3.ultimatecosmetics.modules.auras;

import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Aura extends Cosmetic {
    private static final Class<?> PACKET = ReflectionAPI.getNmsClass("PacketPlayOutScoreboardTeam");
    private final Object givePacket;
    private final Object removePacket;

    public Aura(CosmeticConfig cosmeticConfig, Player player, AuraStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.AURA);
        Object givePacket = null;
        Object removePacket = null;
        String teamName = Random.getString(16, true, true);
        try {
            givePacket = PACKET.newInstance();
            ReflectionAPI.setField(givePacket, "a", teamName);
            ReflectionAPI.setField(givePacket, "b", teamName);
            ReflectionAPI.setField(givePacket, "c", cosmeticStorage.getColor().toString());
            ReflectionAPI.setField(givePacket, "d", "");
            ReflectionAPI.setField(givePacket, "e", "always");
            ReflectionAPI.setField(givePacket, "f", "always");
            ReflectionAPI.setField(givePacket, "g", -1);
            ReflectionAPI.setField(givePacket, "h", Collections.singletonList(this.player.getName()));
            ReflectionAPI.setField(givePacket, "i", 0);
            ReflectionAPI.setField(givePacket, "j", 0);

            removePacket = PACKET.newInstance();
            ReflectionAPI.setField(removePacket, "a", teamName);
            ReflectionAPI.setField(removePacket, "i", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.givePacket = givePacket;
        this.removePacket = removePacket;
    }

    @Override
    protected boolean giveInternal() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            this.setGlowing(p);
        }
        return true;
    }

    @Override
    protected void removeInternal() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            ReflectionAPI.sendPacket(p, this.removePacket);
        }
        this.player.setGlowing(false);
    }

    public void setGlowing(Player target) {
        ReflectionAPI.sendPacket(target, this.givePacket);
        this.player.setGlowing(true);
    }
}
