package com.j0ach1mmall3.ultimatecosmetics.modules.cloaks;

import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 31/10/2015
 */
public final class Cloaks extends CosmeticConfig<CloakStorage> {
    private final int updateInterval;
    private final int viewDistance;

    public Cloaks(CloaksModule module) {
        super("cloaks.yml", module.getParent(), "Cloaks");
        this.updateInterval = this.config.getInt("UpdateInterval");
        this.viewDistance = this.config.getInt("ViewDistance");
    }

    @Override
    public Class<? extends Cosmetic> getCosmeticClass() {
        return Cloak.class;
    }

    @Override
    public Cosmetic getCosmetic(CloakStorage cosmeticStorage, Player player) {
        return new Cloak(player, cosmeticStorage);
    }

    @Override
    protected CloakStorage getCosmeticStorageByIdentifier(String section, String identifier) {
        String path = section + '.' + identifier + '.';
        return new CloakStorage(
                identifier,
                this.config.getString(path + "Permission"),
                this.storage.getJLibItem(this.config, path),
                Effect.valueOf(this.config.getString(path + "Effect")),
                this.config.getInt(path + "ID"),
                this.config.getInt(path + "Data"),
                Parsing.parseFloat(this.config.getString(path + "Speed")),
                ParticleCosmeticStorage.Shape.valueOf(this.config.getString(path + "Shape")),
                this.config.getStringList(path + "GridShape").toArray(new String[this.config.getStringList(path + "GridShape").size()])
        );
    }

    public int getUpdateInterval() {
        return this.updateInterval;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }
}
