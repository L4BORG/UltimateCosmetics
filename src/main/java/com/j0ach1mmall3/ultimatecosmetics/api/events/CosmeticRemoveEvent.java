package com.j0ach1mmall3.ultimatecosmetics.api.events;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import org.bukkit.event.HandlerList;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 13/10/2015
 */
public final class CosmeticRemoveEvent extends CosmeticEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public CosmeticRemoveEvent(Cosmetic cosmetic) {
        super(cosmetic);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
