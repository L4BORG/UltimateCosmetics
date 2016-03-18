package com.j0ach1mmall3.ultimatecosmetics.modules.hearts;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class HeartsListener implements Listener {
    private final HeartsModule module;
    private final Map<Player, Cosmetic> heartsMap = new HashMap<>();

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
            for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.HEART)) {
                HeartStorage heart = (HeartStorage) cosmetic.getCosmeticStorage();
                if(cause.name().equals(heart.getColor().getPotionEffect().getName())) e.setCancelled(true);
                if (((Hearts) this.module.getConfig()).isScaleDamage()) {
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
            for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.HEART)) {
                HeartStorage heart = (HeartStorage) cosmetic.getCosmeticStorage();
                if(heart.getEffect() == HeartStorage.Effect.BOUNCING) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (((Hearts) this.module.getConfig()).isKeepOnDeath()) {
            Player p = e.getEntity();
            for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.HEART)) {
                this.heartsMap.put(p, cosmetic);
                cosmetic.remove();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (this.heartsMap.containsKey(p)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    HeartsListener.this.heartsMap.get(p).give();
                    HeartsListener.this.heartsMap.remove(p);
                }
            }.runTaskLater(this.module.getParent(), 1L);
        }
    }
}
