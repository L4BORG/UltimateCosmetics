package com.j0ach1mmall3.ultimatecosmetics.modules.hearts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class HeartsListener implements Listener {
    private final HeartsModule module;
    private final Map<Player, Heart> heartsMap = new HashMap<>();

    public HeartsListener(HeartsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (cause == null) return;
            for(Heart cosmetic : this.module.getParent().getApi().getCosmetics(p, Heart.class)) {
                HeartStorage heart = cosmetic.getCosmeticStorage();
                if(cause.name().equals(heart.getColor().getPotionEffect().getName())) e.setCancelled(true);
                if (this.module.getConfig().isScaleDamage()) {
                    int rows = heart.getRows();
                    e.setDamage(e.getDamage() * rows);
                }
            }
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
        if(e.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) return;
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            for(Heart cosmetic : this.module.getParent().getApi().getCosmetics(p, Heart.class)) {
                HeartStorage heart = cosmetic.getCosmeticStorage();
                if(heart.getEffect() == HeartStorage.Effect.BOUNCING) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (this.module.getConfig().isKeepOnDeath()) {
            Player p = e.getEntity();
            for(Heart cosmetic : this.module.getParent().getApi().getCosmetics(p, Heart.class)) {
                this.heartsMap.put(p, cosmetic);
                cosmetic.remove(this.module.getParent());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (this.heartsMap.containsKey(p)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                @Override
                public void run() {
                    HeartsListener.this.heartsMap.get(p).give(HeartsListener.this.module.getParent());
                    HeartsListener.this.heartsMap.remove(p);
                }
            }, 1L);
        }
    }
}
