package com.j0ach1mmall3.ultimatecosmetics.modules.mounts;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.Reflection;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.EntityCosmeticStorage;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Mount extends Cosmetic<EntityCosmeticStorage> {
    private Creature entity;

    public Mount(Player player, EntityCosmeticStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(Main plugin) {
        this.entity = this.player.getWorld().spawn(this.player.getLocation().add(0, 0.1, 0), this.cosmeticStorage.getType().getClazz());
        Reflection.removeGoalSelectors(this.entity);
        Reflection.addData(this.entity, this.cosmeticStorage.getData());
        this.entity.setCanPickupItems(false);
        this.entity.setRemoveWhenFarAway(false);
        this.entity.setNoDamageTicks(Integer.MAX_VALUE);
        this.entity.setMaxHealth(20.0D);
        this.entity.setHealth(this.entity.getMaxHealth());
        this.entity.setMetadata("Mount", new FixedMetadataValue(plugin, this.player.getName()));
        this.entity.setCustomName(Placeholders.parse(this.cosmeticStorage.getjLibItem().getItemStack().getItemMeta().getDisplayName(), this.player));
        this.entity.setCustomNameVisible(true);
        Methods.setPassenger(this.entity, this.player);
        plugin.queueEntity(this.entity);
        return true;
    }

    @Override
    protected void removeInternal(Main plugin) {
        this.entity.remove();
    }

    public LivingEntity getEntity() {
        return this.entity;
    }
}
