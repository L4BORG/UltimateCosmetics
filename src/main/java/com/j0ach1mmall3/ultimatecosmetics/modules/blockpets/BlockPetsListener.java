package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 21/08/2015
 */
public final class BlockPetsListener implements Listener {
    private final Set<Player> renamingPlayers = new HashSet<>();
    private final BlockPetsModule module;

    public BlockPetsListener(BlockPetsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        for(Entity entity : e.getEntity().getNearbyEntities(1, 1, 1)) {
            if(entity.hasMetadata("BlockPet")) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("BlockPet").get(0).asString());
            for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(owner, BlockPet.class)) {
                cosmetic.give(this.module.getParent());
            }
            e.setCancelled(true);
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("BlockPet").get(0).asString());
            for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(owner, BlockPet.class)) {
                cosmetic.give(this.module.getParent());
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("BlockPet").get(0).asString());
            for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(owner, BlockPet.class)) {
                cosmetic.give(this.module.getParent());
            }
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("BlockPet").get(0).asString());
            for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(owner, BlockPet.class)) {
                cosmetic.give(this.module.getParent());
            }
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("BlockPet").get(0).asString());
            for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(owner, BlockPet.class)) {
                cosmetic.give(this.module.getParent());
            }
            e.setCancelled(true);
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("BlockPet").get(0).asString());
            for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(owner, BlockPet.class)) {
                cosmetic.give(this.module.getParent());
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent e) {
        if (e.getEntity().hasMetadata("BlockPet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getReason() != EntityTargetEvent.TargetReason.CUSTOM && e.getEntity().hasMetadata("BlockPet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByPet(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("BlockPet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        if (e.getEntity().hasMetadata("BlockPet")) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(p, BlockPet.class)) {
            e.setCancelled(true);
            p.updateInventory();
            if (e.getRightClicked().getUniqueId().equals(cosmetic.getBlock().getUniqueId()) && p.hasPermission("uc.renamepet") && !this.renamingPlayers.contains(p)) {
                String renamePet = this.module.getParent().getLang().getRenamePet();
                if(!renamePet.isEmpty()) p.sendMessage(Placeholders.parse(renamePet, p));
                this.renamingPlayers.add(p);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(this.renamingPlayers.contains(p)) {
            this.renamingPlayers.remove(p);
            for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(p, BlockPet.class)) {
                e.setCancelled(true);
                cosmetic.getBlock().setCustomName(Placeholders.parse(e.getMessage(), p));
                this.module.getParent().getDataLoader().setPetName(p, e.getMessage());
            }
            String successfulRename = this.module.getParent().getLang().getSuccessfulRename();
            if(!successfulRename.isEmpty()) p.sendMessage(Placeholders.parse(successfulRename, p).replace("%petname%", Placeholders.parse(e.getMessage(), p)));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        this.renamingPlayers.remove(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        this.renamingPlayers.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        for(BlockPet cosmetic : this.module.getParent().getApi().getCosmetics(p, BlockPet.class)) {
            cosmetic.getCreature().teleport(p);
        }
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }
}
