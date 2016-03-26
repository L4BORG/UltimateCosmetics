package com.j0ach1mmall3.ultimatecosmetics.modules.pets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.HorseInventory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class PetsListener implements Listener {
    private final Set<Player> renamingPlayers = new HashSet<>();
    private final PetsModule module;

    public PetsListener(PetsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
        if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if(((Main) this.module.getParent()).getApi().hasCosmetics(p, CosmeticType.PET)) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory() instanceof HorseInventory) {
            Player p = (Player) e.getPlayer();
            if(((Main) this.module.getParent()).getApi().hasCosmetics(p, CosmeticType.PET)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByPet(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("Pet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        if (e.getDuration() != Integer.MAX_VALUE && e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        if (e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent e) {
        if (e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        if (e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getReason() != EntityTargetEvent.TargetReason.CUSTOM && e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent e) {
        if (e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.PET)) {
            e.setCancelled(true);
            if (e.getRightClicked().getUniqueId().equals(((Pet) cosmetic).getEntity().getUniqueId()) && p.hasPermission("uc.renamepet") && !this.renamingPlayers.contains(p)) {
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
            for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.PET)) {
                e.setCancelled(true);
                ((Pet) cosmetic).getEntity().setCustomName(Placeholders.parse(e.getMessage(), p));
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
        if (e.getEntity().hasMetadata("Pet")) e.setCancelled(true);
    }
}
