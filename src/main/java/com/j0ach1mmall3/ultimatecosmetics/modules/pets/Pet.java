package com.j0ach1mmall3.ultimatecosmetics.modules.pets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Reflection;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.EntityCosmeticStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 12/03/2016
 */
public final class Pet extends Cosmetic<EntityCosmeticStorage> {
    private Creature entity;

    public Pet(Player player, EntityCosmeticStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(final Main plugin) {
        this.entity = this.player.getWorld().spawn(this.player.getLocation(), this.cosmeticStorage.getType().getClazz());
        Reflection.removeGoalSelectors(this.entity);
        Reflection.addGoalSelectors(this.entity);
        this.entity.teleport(this.player);
        this.entity.setCanPickupItems(false);
        Reflection.addData(this.entity, this.cosmeticStorage.getData());
        this.entity.setCustomName(Placeholders.parse(this.cosmeticStorage.getjLibItem().getItemStack().getItemMeta().getDisplayName(), this.player));
        this.entity.setCustomNameVisible(true);
        this.entity.setMaxHealth(20.0D);
        this.entity.setHealth(this.entity.getMaxHealth());
        this.entity.setMetadata("Pet", new FixedMetadataValue(plugin, this.player));
        this.entity.setTarget(this.player);
        plugin.getDataLoader().getPetName(this.player, new CallbackHandler<String>() {
            @Override
            public void callback(final String o) {
                if(o == null || o.isEmpty()) return;
                Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Void>() {
                    @Override
                    public Void call() {
                        Pet.this.entity.setCustomName(Placeholders.parse(o, Pet.this.player));
                        return null;
                    }
                });
            }
        });
        return true;
    }

    @Override
    protected void removeInternal(Main plugin) {
        this.entity.remove();
    }

    public Creature getEntity() {
        return this.entity;
    }
}
