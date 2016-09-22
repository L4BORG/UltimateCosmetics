package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.modules.bowtrails.BowTrail;
import com.j0ach1mmall3.ultimatecosmetics.modules.cloaks.Cloak;
import com.j0ach1mmall3.ultimatecosmetics.modules.particles.Particle;
import org.bukkit.Effect;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 29/02/2016
 */
public class ParticleCosmeticStorage extends CosmeticStorage {
    private final Effect particle;
    private final int id;
    private final int data;
    private final float speed;
    private final Shape shape;

    public ParticleCosmeticStorage(String identifier, String permission, JLibItem jLibItem, Effect particle, int id, int data, float speed, Shape shape) {
        super(identifier, permission, jLibItem);
        this.particle = particle;
        this.id = id;
        this.data = data;
        this.speed = speed;
        this.shape = shape;
    }

    public final Effect getParticle() {
        return this.particle;
    }

    public final int getId() {
        return this.id;
    }

    public final int getData() {
        return this.data;
    }

    public final float getSpeed() {
        return this.speed;
    }

    public final Shape getShape() {
        return this.shape;
    }

    public enum Shape {
        RANDOM(Particle.class),
        HELIX(Particle.class),
        DOUBLEHELIX(Particle.class),
        DOME(Particle.class),
        CROSS(Particle.class),
        DOUBLECROSS(Particle.class),
        SPHERE(Particle.class),
        CYLINDER(Particle.class),
        CONE(Particle.class),
        TRAIL(Particle.class, BowTrail.class),
        HALO(Cloak.class),
        EFFECT2D(Cloak.class);
        private final Set<Class> types = new HashSet<>();

        Shape(Class... classes) {
            this.types.addAll(Arrays.asList(classes));
        }

        public boolean isCosmeticType(Class<? extends Cosmetic> clazz) {
            return this.types.contains(clazz);
        }
    }
}
