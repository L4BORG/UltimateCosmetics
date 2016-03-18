package com.j0ach1mmall3.ultimatecosmetics.modules.balloons;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 20/08/2015
 */
public final class Balloons extends CosmeticConfig {
    private final boolean disableBatSounds;
    private final int teleportInterval;
    private final int teleportDistance;

    public Balloons(BalloonsModule module) {
        super("balloons.yml", module.getParent(), "Balloons");
        this.disableBatSounds = this.config.getBoolean("DisableBatSounds");
        this.teleportInterval = this.config.getInt("TeleportInterval");
        this.teleportDistance = this.config.getInt("TeleportDistance");
    }

    @Override
    public Cosmetic getCosmetic(CosmeticStorage cosmeticStorage, Player player) {
        return new Balloon(this, player, cosmeticStorage);
    }

    public boolean isDisableBatSounds() {
        return this.disableBatSounds;
    }

    public int getTeleportInterval() {
        return this.teleportInterval;
    }

    public int getTeleportDistance() {
        return this.teleportDistance;
    }
}
