package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import org.bukkit.Effect;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 29/02/2016
 */
public final class ParticleCosmeticStorage extends CosmeticStorage {
    private final Effect particle;
    private final int id;
    private final int data;
    private final float speed;
    private final Shape shape;

    public ParticleCosmeticStorage(Main plugin, String identifier, String permission, GuiItem guiItem, Effect particle, int id, int data, float speed, Shape shape) {
        super(plugin, identifier, permission, guiItem);
        this.particle = particle;
        this.id = id;
        this.data = data;
        this.speed = speed;
        this.shape = shape;
    }

    public Effect getParticle() {
        return this.particle;
    }

    public int getId() {
        return this.id;
    }

    public int getData() {
        return this.data;
    }

    public float getSpeed() {
        return this.speed;
    }

    public Shape getShape() {
        return this.shape;
    }

    public enum Shape {
        RANDOM(CosmeticType.PARTICLE),
        HELIX(CosmeticType.PARTICLE),
        DOUBLEHELIX(CosmeticType.PARTICLE),
        DOME(CosmeticType.PARTICLE),
        CROSS(CosmeticType.PARTICLE),
        DOUBLECROSS(CosmeticType.PARTICLE),
        SPHERE(CosmeticType.PARTICLE),
        CYLINDER(CosmeticType.PARTICLE),
        CONE(CosmeticType.PARTICLE),
        TRAIL(CosmeticType.PARTICLE, CosmeticType.BOWTRAIL),
        HALO(CosmeticType.CLOAK),
        FLAT(CosmeticType.CLOAK),
        WINGS(CosmeticType.CLOAK);
        private final EnumSet<CosmeticType> types = EnumSet.noneOf(CosmeticType.class);

        Shape(CosmeticType... types) {
            this.types.addAll(Arrays.asList(types));
        }

        public boolean isCosmeticType(CosmeticType type) {
            return this.types.contains(type);
        }
    }
}
