package com.j0ach1mmall3.ultimatecosmetics.api.events;

import com.j0ach1mmall3.jlib.inventory.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;

/**
 * Created by j0ach1mmall3 on 17:35 13/10/2015 using IntelliJ IDEA.
 */
public class PlayerClickInGuiEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private GUI gui;
    private final InventoryClickEvent event;
    private boolean cancelled;

    public PlayerClickInGuiEvent(Player player, GUI gui, InventoryClickEvent event) {
        super(player);
        this.gui = gui;
        this.event = event;
    }


    public final GUI getGui() {
        return this.gui;
    }

    public final void setGui(GUI gui) {
        this.gui = gui;
    }

    public final InventoryClickEvent getEvent() {
        return this.event;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public final HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
