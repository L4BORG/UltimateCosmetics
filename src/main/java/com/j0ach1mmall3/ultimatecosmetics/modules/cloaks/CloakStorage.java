package com.j0ach1mmall3.ultimatecosmetics.modules.cloaks;

import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleCosmeticStorage;
import org.bukkit.Effect;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 20/09/2016
 */
public final class CloakStorage extends ParticleCosmeticStorage {
    private final String[] shape;

    public CloakStorage(String identifier, String permission, JLibItem jLibItem, Effect particle, int id, int data, float speed, Shape shape, String[] shape1) {
        super(identifier, permission, jLibItem, particle, id, data, speed, shape);
        this.shape = shape1;
    }

    public String[] getGridShape() {
        return this.shape;
    }
}
