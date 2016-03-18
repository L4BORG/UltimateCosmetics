package com.j0ach1mmall3.ultimatecosmetics;

import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.EntityCosmeticStorage;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EnumSet;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/03/2016
 */
public final class Reflection {
    private static final Class<?> NAVIGATIONABSTRACTCLASS = ReflectionAPI.getNmsClass("NavigationAbstract");
    private static final Class<?> PGSCLASS = ReflectionAPI.getNmsClass("PathfinderGoalSelector");
    private static final Class<?> PATHFINDERGOALCLASS = ReflectionAPI.getNmsClass("PathfinderGoal");
    private static final Class<?> HUMANCLASS = ReflectionAPI.getNmsClass("EntityHuman");
    private static final Class<?> ENTITYINSENTIENTCLASS = ReflectionAPI.getNmsClass("EntityInsentient");
    private static final Class<?> ENTITYCREATURECLASS = ReflectionAPI.getNmsClass("EntityCreature");
    private static final Class<?> PGFCLASS = ReflectionAPI.getNmsClass("PathfinderGoalFloat");
    private static final Class<?> PGMACLASS = ReflectionAPI.getNmsClass("PathfinderGoalMeleeAttack");
    private static final Class<?> PGLAPCLASS = ReflectionAPI.getNmsClass("PathfinderGoalLookAtPlayer");

    private Reflection() {
    }

    private static Object getGoalSelector(Object o) {
        Object obj = null;
        try {
            Field goalSelector = ENTITYINSENTIENTCLASS.getDeclaredField("goalSelector");
            goalSelector.setAccessible(true);
            obj = goalSelector.get(o);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static Object getTargetSelector(Object o) {
        Object obj = null;
        try {
            Field targetSelector = ENTITYINSENTIENTCLASS.getDeclaredField("targetSelector");
            targetSelector.setAccessible(true);
            obj = targetSelector.get(o);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void removeGoalSelectors(Entity ent) {
        Object o = ReflectionAPI.getHandle((Object) ent);
        try {
            Field b = PGSCLASS.getDeclaredField("b");
            Field c = PGSCLASS.getDeclaredField("c");
            b.setAccessible(true);
            c.setAccessible(true);
            ((Collection) b.get(getGoalSelector(o))).clear();
            ((Collection) b.get(getTargetSelector(o))).clear();
            ((Collection) c.get(getGoalSelector(o))).clear();
            ((Collection) c.get(getTargetSelector(o))).clear();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void addGoalSelectors(Entity ent) {
        Object o = ReflectionAPI.getHandle((Object) ent);
        try {
            Method a = PGSCLASS.getMethod("a", int.class, PATHFINDERGOALCLASS);
            a.invoke(getGoalSelector(o), 0, PGFCLASS.getConstructor(ENTITYINSENTIENTCLASS).newInstance(o));
            a.invoke(getGoalSelector(o), 2, PGMACLASS.getConstructor(ENTITYCREATURECLASS, double.class, boolean.class).newInstance(o, 1.0D, true));
            a.invoke(getGoalSelector(o), 8, PGLAPCLASS.getConstructor(ENTITYINSENTIENTCLASS, Class.class, float.class).newInstance(o, HUMANCLASS, 8.0F));
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setNavigation(Entity ent, Location l, double speed) {
        Object o = ReflectionAPI.getHandle((Object) ent);
        try {
            Object navigationAbstract = ENTITYINSENTIENTCLASS.getMethod("getNavigation").invoke(o);
            NAVIGATIONABSTRACTCLASS.getMethod("a", double.class, double.class, double.class, double.class).invoke(navigationAbstract, l.getX(), l.getY(), l.getZ(), speed);
        } catch (SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static void addData(Entity ent, EnumSet<EntityCosmeticStorage.EntityData> entityDatas) {
        for(EntityCosmeticStorage.EntityData data : entityDatas) {
            if (ent instanceof Ageable) {
                ((Ageable) ent).setBreed(false);
            }
            if(data.isType(EntityCosmeticStorage.EntityData.Type.COLOR) && ent instanceof Colorable) ((Colorable) ent).setColor(DyeColor.valueOf(data.name()));
            if(data.isType(EntityCosmeticStorage.EntityData.Type.CAT) && ent instanceof Ocelot) ((Ocelot) ent).setCatType(Ocelot.Type.valueOf(data.name() + "_CAT"));
            if(data.isType(EntityCosmeticStorage.EntityData.Type.PROFESSION) && ent instanceof Villager) ((Villager) ent).setProfession(Villager.Profession.valueOf(data.name()));
            if(data.isType(EntityCosmeticStorage.EntityData.Type.HORSE_ARMOR) && ent instanceof Horse) ((Horse) ent).getInventory().setArmor(new ItemStack(Material.valueOf(data + "_BARDING")));
            if(data.isType(EntityCosmeticStorage.EntityData.Type.HORSE_COLOR) && ent instanceof Horse) ((Horse) ent).setColor(Horse.Color.valueOf(data.name()));
            if(data.isType(EntityCosmeticStorage.EntityData.Type.HORSE_VARIANT) && ent instanceof Horse) ((Horse) ent).setVariant(Horse.Variant.valueOf(data.name()));
            if(data.isType(EntityCosmeticStorage.EntityData.Type.HORSE_STYLE) && ent instanceof Horse) ((Horse) ent).setStyle(Horse.Style.valueOf(data.name()));
            if(data.isType(EntityCosmeticStorage.EntityData.Type.RABBIT) && ReflectionAPI.verBiggerThan(1, 8) && ent instanceof org.bukkit.entity.Rabbit) ((org.bukkit.entity.Rabbit) ent).setRabbitType(org.bukkit.entity.Rabbit.Type.valueOf(data.name()));
            switch (data) {
                case BABY:
                    if (ent instanceof Ageable) {
                        ((Ageable) ent).setBaby();
                        ((Ageable) ent).setAgeLock(true);
                    }
                    if (ent instanceof Zombie) ((Zombie) ent).setBaby(true);
                    break;
                case FIRE:
                    ent.setFireTicks(Integer.MAX_VALUE);
                    break;
                case CHESTED:
                    if (ent instanceof Horse) {
                        ((Horse) ent).setCarryingChest(true);
                    }
                    break;
                case POWER:
                    if (ent instanceof Creeper) {
                        ((Creeper) ent).setPowered(true);
                    }
                    break;
                case SADDLE:
                    if (ent instanceof Pig) {
                        ((Pig) ent).setSaddle(true);
                    }
                    break;
                case SHEARED:
                    if (ent instanceof Sheep) {
                        ((Sheep) ent).setSheared(true);
                    }
                    break;
                case TAMED:
                    if (ent instanceof Tameable) {
                        ((Tameable) ent).setTamed(true);
                    }
                    break;
                case VILLAGER:
                    if (ent instanceof Zombie) {
                        ((Zombie) ent).setVillager(true);
                    }
                    break;
                case WITHER:
                    if (ent instanceof Skeleton) {
                        ((Skeleton) ent).setSkeletonType(Skeleton.SkeletonType.WITHER);
                    }
            }
        }
    }
}
