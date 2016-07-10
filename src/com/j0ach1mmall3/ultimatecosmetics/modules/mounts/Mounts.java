package com.j0ach1mmall3.ultimatecosmetics.modules.mounts;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.EntityCosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.List;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 23/08/2015
 */
public final class Mounts extends CosmeticConfig<EntityCosmeticStorage> {
    private final double mountSpeed;

    public Mounts(MountsModule module) {
        super("mounts.yml", module.getParent(), "Mounts");
        this.mountSpeed = this.config.getDouble("MountSpeed");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Mount.class;
    }

    @Override
    public Cosmetic getCosmetic(EntityCosmeticStorage cosmeticStorage, Player player) {
        return new Mount(this, player, cosmeticStorage);
    }

    @Override
    protected CosmeticStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new EntityCosmeticStorage(
                this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getGuiItemNew(this.config, path),
                EntityCosmeticStorage.EntityType.valueOf(this.config.getString(path + "MountType").toUpperCase()),
                this.getEntityData(this.config.getStringList(path + "MountData"))
        );
    }

    private EnumSet<EntityCosmeticStorage.EntityData> getEntityData(List<String> data) {
        EnumSet<EntityCosmeticStorage.EntityData> entityDatas = EnumSet.noneOf(EntityCosmeticStorage.EntityData.class);
        for(String d : data) {
            entityDatas.add(EntityCosmeticStorage.EntityData.valueOf(d));
        }
        return entityDatas;
    }

    public double getMountSpeed() {
        return this.mountSpeed;
    }
}
