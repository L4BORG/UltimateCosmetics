package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.modules.pets.Pet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
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
    public void onFallingBlockLand(EntityChangeBlockEvent e) {
        Entity ent = e.getEntity();
        CosmeticsAPI api = ((Main) this.module.getParent()).getApi();
        if (ent.hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("BlockPet").get(0).asString());
            for(Cosmetic cosmetic : api.getCosmetics(owner, CosmeticType.BLOCKPET)) {
                cosmetic.give();
            }
            e.setCancelled(true);
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("BlockPet").get(0).asString());
            for(Cosmetic cosmetic : api.getCosmetics(owner, CosmeticType.BLOCKPET)) {
                cosmetic.give();
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        Entity ent = e.getEntity();
        CosmeticsAPI api = ((Main) this.module.getParent()).getApi();
        if (ent.hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("BlockPet").get(0).asString());
            for(Cosmetic cosmetic : api.getCosmetics(owner, CosmeticType.BLOCKPET)) {
                cosmetic.give();
            }
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("BlockPet").get(0).asString());
            for(Cosmetic cosmetic : api.getCosmetics(owner, CosmeticType.BLOCKPET)) {
                cosmetic.give();
            }
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        Entity ent = e.getEntity();
        CosmeticsAPI api = ((Main) this.module.getParent()).getApi();
        if (ent.hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("BlockPet").get(0).asString());
            for(Cosmetic cosmetic : api.getCosmetics(owner, CosmeticType.BLOCKPET)) {
                cosmetic.give();
            }
            e.setCancelled(true);
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("BlockPet")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("BlockPet").get(0).asString());
            for(Cosmetic cosmetic : api.getCosmetics(owner, CosmeticType.BLOCKPET)) {
                cosmetic.give();
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
        for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.BLOCKPET)) {
            e.setCancelled(true);
            if (e.getRightClicked().getUniqueId().equals(((BlockPet) cosmetic).getBlock().getUniqueId()) && p.hasPermission("uc.renamepet") && !this.renamingPlayers.contains(p)) {
                String renamePet = ((Main) this.module.getParent()).getLang().getRenamePet();
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
            for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.BLOCKPET)) {
                e.setCancelled(true);
                ((BlockPet) cosmetic).getBlock().setCustomName(Placeholders.parse(e.getMessage(), p));
                ((Main) this.module.getParent()).getDataLoader().setPetName(p, e.getMessage());
            }
            String successfulRename = ((Main) this.module.getParent()).getLang().getSuccessfulRename();
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
        for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.PET)) {
            ((Pet) cosmetic).getEntity().teleport(p);
        }
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent e) {
        if (e.getEntity().hasMetadata("Mount")) e.setCancelled(true);
    }
}
