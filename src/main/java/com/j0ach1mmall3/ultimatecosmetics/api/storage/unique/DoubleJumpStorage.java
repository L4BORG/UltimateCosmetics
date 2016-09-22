package com.j0ach1mmall3.ultimatecosmetics.api.storage.unique;

import org.bukkit.Sound;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class DoubleJumpStorage extends UniqueCosmeticStorage {
    private final int multiplier;
    private final Sound sound;

    public DoubleJumpStorage(String identifier, String permission, int multiplier, Sound sound) {
        super(identifier, permission);
        this.multiplier = multiplier;
        this.sound = sound;
    }

    public int getMultiplier() {
        return this.multiplier;
    }

    public Sound getSound() {
        return this.sound;
    }
}
