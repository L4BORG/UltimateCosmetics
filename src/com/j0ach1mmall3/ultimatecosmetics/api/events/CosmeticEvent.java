package com.j0ach1mmall3.ultimatecosmetics.api.events;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 28/02/2016
 */
public abstract class CosmeticEvent extends Event implements Cancellable {
    private Cosmetic cosmetic;
    private boolean cancelled;

    public CosmeticEvent(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
    }

    public final Cosmetic getCosmetic() {
        return this.cosmetic;
    }

    public final void setCosmetic(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public abstract HandlerList getHandlers();
}
