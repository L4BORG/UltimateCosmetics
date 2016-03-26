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
    private final Object updatePacket;
    private final Object removePacket;

    private AuraStorage.Color color;

    public Aura(CosmeticConfig cosmeticConfig, Player player, AuraStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.AURA);
        this.color = cosmeticStorage.getColor();
        Object givePacket = null;
        Object updatePacket = null;
        Object removePacket = null;
        String teamName = Random.getString(16, true, true);
        try {
            givePacket = PACKET.newInstance();
            ReflectionAPI.setField(givePacket, "a", teamName);
            ReflectionAPI.setField(givePacket, "b", teamName);
            ReflectionAPI.setField(givePacket, "c", this.color.asString());
            ReflectionAPI.setField(givePacket, "d", "");
            ReflectionAPI.setField(givePacket, "e", "always");
            ReflectionAPI.setField(givePacket, "f", "always");
            ReflectionAPI.setField(givePacket, "g", -1);
            ReflectionAPI.setField(givePacket, "h", Collections.singletonList(this.player.getName()));
            ReflectionAPI.setField(givePacket, "i", 0);
            ReflectionAPI.setField(givePacket, "j", 0);

            updatePacket = PACKET.newInstance();
            ReflectionAPI.setField(updatePacket, "a", teamName);
            ReflectionAPI.setField(updatePacket, "i", 2);

            removePacket = PACKET.newInstance();
            ReflectionAPI.setField(removePacket, "a", teamName);
            ReflectionAPI.setField(removePacket, "i", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.givePacket = givePacket;
        this.updatePacket = updatePacket;
        this.removePacket = removePacket;
    }

    @Override
    public boolean giveInternal() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            ReflectionAPI.sendPacket(p, this.givePacket);
        }
        this.player.setGlowing(true);
        if(((AuraStorage) this.cosmeticStorage).getColor() == AuraStorage.Color.RAINBOW) {
            if(this.color == AuraStorage.Color.RAINBOW) this.color = AuraStorage.Color.BLACK;
            try {
                ReflectionAPI.setField(this.updatePacket, "c", this.color.asString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.color = AuraStorage.Color.values()[this.color.ordinal() + 1];
            for(Player p : Bukkit.getOnlinePlayers()) {
                ReflectionAPI.sendPacket(p, this.updatePacket);
            }
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
}
