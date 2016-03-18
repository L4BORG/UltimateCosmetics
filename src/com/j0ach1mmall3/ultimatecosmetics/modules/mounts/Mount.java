package com.j0ach1mmall3.ultimatecosmetics.modules.mounts;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.EntityCosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.Reflection;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Mount extends Cosmetic {
    private LivingEntity entity;

    public Mount(CosmeticConfig cosmeticConfig, Player player, EntityCosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.MOUNT);
    }

    @Override
    protected boolean giveInternal() {
        this.entity = (LivingEntity) this.player.getWorld().spawnEntity(this.player.getLocation(), ((EntityCosmeticStorage) this.cosmeticStorage).getType().getType());
        Reflection.removeGoalSelectors(this.entity);
        this.entity.teleport(this.player);
        this.entity.setCanPickupItems(false);
        Reflection.addData(this.entity, ((EntityCosmeticStorage) this.cosmeticStorage).getData());
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
