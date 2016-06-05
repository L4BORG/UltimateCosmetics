package com.j0ach1mmall3.ultimatecosmetics.modules.pets;

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
public final class Pets extends CosmeticConfig<EntityCosmeticStorage> {
    private final int teleportInterval;
    private final int teleportDistance;

    public Pets(PetsModule module) {
        super("pets.yml", module.getParent(), "Pets");
        this.teleportInterval = this.config.getInt("TeleportInterval");
        this.teleportDistance = this.config.getInt("TeleportDistance");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Pet.class;
    }

    @Override
    public Cosmetic getCosmetic(EntityCosmeticStorage cosmeticStorage, Player player) {
        return new Pet(this, player, cosmeticStorage);
    }

    @Override
    protected CosmeticStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new EntityCosmeticStorage(
                this.storage.getPlugin(),
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getGuiItemNew(this.config, path),
                EntityCosmeticStorage.EntityType.valueOf(this.config.getString(path + "PetType").toUpperCase()),
                this.getEntityData(this.config.getStringList(path + "PetData"))
        );
    }

    private EnumSet<EntityCosmeticStorage.EntityData> getEntityData(List<String> data) {
        EnumSet<EntityCosmeticStorage.EntityData> entityDatas = EnumSet.noneOf(EntityCosmeticStorage.EntityData.class);
        for(String d : data) {
            entityDatas.add(EntityCosmeticStorage.EntityData.valueOf(d));
        }
        return entityDatas;
    }

    public int getTeleportInterval() {
        return this.teleportInterval;
    }

    public int getTeleportDistance() {
        return this.teleportDistance;
    }
}
