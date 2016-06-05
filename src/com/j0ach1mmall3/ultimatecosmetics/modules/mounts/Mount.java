package com.j0ach1mmall3.ultimatecosmetics.modules.mounts;

import com.j0ach1mmall3.jlib.integration.Placeholders;
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
public final class Mount extends Cosmetic<Mounts, EntityCosmeticStorage> {
    private Creature entity;

    public Mount(Mounts cosmeticConfig, Player player, EntityCosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal() {
        this.entity = this.player.getWorld().spawn(this.player.getLocation(), this.cosmeticStorage.getType().getClazz());
        Reflection.removeGoalSelectors(this.entity);
        this.entity.teleport(this.player);
        this.entity.setCanPickupItems(false);
        Reflection.addData(this.entity, this.cosmeticStorage.getData());
        this.entity.setCustomName(Placeholders.parse(this.cosmeticStorage.getGuiItem().getItem().getItemMeta().getDisplayName(), this.player));
        this.entity.setCustomNameVisible(true);
        this.entity.setPassenger(this.player);
        this.entity.setMaxHealth(20.0D);
        this.entity.setHealth(this.entity.getMaxHealth());
        this.entity.setMetadata("Mount", new FixedMetadataValue(this.cosmeticStorage.getPlugin(), this.player));
        return true;
    }

    @Override
    protected void removeInternal() {
        this.entity.remove();
    }

    public LivingEntity getEntity() {
        return this.entity;
    }
}
