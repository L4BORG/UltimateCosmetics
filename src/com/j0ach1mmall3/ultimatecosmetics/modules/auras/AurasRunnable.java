package com.j0ach1mmall3.ultimatecosmetics.modules.auras;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 6/03/2016
 */
public final class AurasRunnable extends BukkitRunnable {
    private final AurasModule module;

    public AurasRunnable(AurasModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        for (Aura cosmetic : this.module.getParent().getApi().getAllCosmetics(Aura.class)) {
            cosmetic.giveInternal();
        }
    }
}
