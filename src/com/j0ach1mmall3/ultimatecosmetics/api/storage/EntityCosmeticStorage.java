package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.ultimatecosmetics.Main;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 29/02/2016
 */
public final class EntityCosmeticStorage extends CosmeticStorage {
    private final EntityType type;
    private final EnumSet<EntityData> data;

    public EntityCosmeticStorage(Main plugin, String identifier, String permission, GuiItem guiItem, EntityType type, EnumSet<EntityData> data) {
        super(plugin, identifier, permission, guiItem);
        this.type = type;
        this.data = data;
    }

    public EntityType getType() {
        return this.type;
    }

    public EnumSet<EntityData> getData() {
        return this.data;
    }

    public enum EntityType {
        CREEPER("Creeper", org.bukkit.entity.EntityType.CREEPER, 50),
        SKELETON("Skeleton", org.bukkit.entity.EntityType.SKELETON, 51),
        SPIDER("Spider", org.bukkit.entity.EntityType.SPIDER, 52),
        GIANT("Giant", org.bukkit.entity.EntityType.GIANT, 53),
        ZOMBIE("Zombie", org.bukkit.entity.EntityType.ZOMBIE, 54),
        PIG_ZOMBIE("PigZombie", org.bukkit.entity.EntityType.PIG_ZOMBIE, 57),
        ENDERMAN("Enderman", org.bukkit.entity.EntityType.ENDERMAN, 58),
        CAVE_SPIDER("CaveSpider", org.bukkit.entity.EntityType.CAVE_SPIDER, 59),
        SILVERFISH("Silverfish", org.bukkit.entity.EntityType.SILVERFISH, 60),
        BLAZE("Blaze", org.bukkit.entity.EntityType.BLAZE, 61),
        WITCH("Witch", org.bukkit.entity.EntityType.WITCH, 66),
        ENDERMITE("Endermite", org.bukkit.entity.EntityType.ENDERMITE, 67),
        PIG("Pig", org.bukkit.entity.EntityType.PIG, 90),
        SHEEP("Sheep", org.bukkit.entity.EntityType.SHEEP, 91),
        COW("Cow", org.bukkit.entity.EntityType.COW, 92),
        CHICKEN("Chicken", org.bukkit.entity.EntityType.CHICKEN, 93),
        SQUID("Squid", org.bukkit.entity.EntityType.SQUID, 94),
        WOLF("Wolf", org.bukkit.entity.EntityType.WOLF, 95),
        MUSHROOM_COW("MushroomCow", org.bukkit.entity.EntityType.MUSHROOM_COW, 96),
        SNOWMAN("SnowMan", org.bukkit.entity.EntityType.SNOWMAN, 97),
        OCELOT("Ocelot", org.bukkit.entity.EntityType.OCELOT, 98),
        IRON_GOLEM("IronGolem", org.bukkit.entity.EntityType.IRON_GOLEM, 99),
        HORSE("Horse", org.bukkit.entity.EntityType.HORSE, 100),
        RABBIT("Rabbit", org.bukkit.entity.EntityType.RABBIT, 101),
        VILLAGER("Villager", org.bukkit.entity.EntityType.VILLAGER, 120);
        private final String name;
        private final org.bukkit.entity.EntityType type;
        private final int id;

        EntityType(String name, org.bukkit.entity.EntityType type, int id) {
            this.name = name;
            this.type = type;
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public org.bukkit.entity.EntityType getType() {
            return this.type;
        }

        public int getId() {
            return this.id;
        }
    }

    public enum EntityData {
        BABY("baby", Type.BOOLEAN),
        BLACK("black", Type.COLOR, Type.CAT, Type.HORSE_COLOR, Type.RABBIT),
        BLACK_AND_WHITE("blackAndWhite", Type.RABBIT),
        BLACKSMITH("blacksmith", Type.PROFESSION),
        BLACK_DOTS("blackdots", Type.HORSE_STYLE),
        BLUE("blue", Type.COLOR),
        BROWN("brown", Type.COLOR, Type.HORSE_COLOR, Type.RABBIT),
        BUTCHER("butcher", Type.PROFESSION),
        CHESTED("chested", Type.BOOLEAN),
        CHESTNUT("chestnut", Type.HORSE_COLOR),
        CREAMY("creamy", Type.HORSE_COLOR),
        CYAN("cyan", Type.COLOR),
        DARK_BROWN("darkBrown", Type.HORSE_COLOR),
        DIAMOND("diamond", Type.HORSE_ARMOR),
        DONKEY("donkey", Type.HORSE_VARIANT),
        FARMER("farmer", Type.PROFESSION),
        FIRE("fire", Type.BOOLEAN),
        GRAY("gray", Type.COLOR, Type.HORSE_COLOR),
        GREEN("green", Type.COLOR),
        GOLD("gold", Type.HORSE_ARMOR, Type.RABBIT),
        HORSE("horse", Type.HORSE_VARIANT),
        IRON("iron", Type.HORSE_ARMOR),
        LIBRARIAN("librarian", Type.PROFESSION),
        LIGHT_BLUE("lightBlue", Type.COLOR),
        LIME("lime", Type.COLOR),
        MAGENTA("magenta", Type.COLOR),
        MULE("mule", Type.HORSE_VARIANT),
        NONE("none", Type.HORSE_STYLE),
        ORANGE("orange", Type.COLOR),
        PINK("pink", Type.COLOR),
        POWER("powered", Type.BOOLEAN),
        PRIEST("priest", Type.PROFESSION),
        PURPLE("purple", Type.COLOR),
        RED("red", Type.CAT, Type.COLOR),
        SADDLE("saddle", Type.BOOLEAN),
        SALT_AND_PEPPER("saltAndPepper", Type.RABBIT),
        SHEARED("sheared", Type.BOOLEAN),
        SIAMESE("siamese", Type.CAT),
        SILVER("silver", Type.COLOR),
        SKELETON_HORSE("skeletonHorse", Type.HORSE_VARIANT),
        TAMED("tamed", Type.BOOLEAN),
        THE_KILLER_BUNNY("killer", Type.RABBIT),
        VILLAGER("villager", Type.BOOLEAN),
        WHITEFIELD("whitepatch", Type.HORSE_STYLE),
        WHITE_DOTS("whiteDots", Type.HORSE_STYLE),
        WHITE("white", Type.COLOR, Type.HORSE_COLOR, Type.HORSE_STYLE, Type.RABBIT),
        WILD("wild", Type.CAT),
        WITHER("wither", Type.BOOLEAN),
        YELLOW("yellow", Type.COLOR),
        UNDEAD_HORSE("zombieHorse", Type.HORSE_VARIANT);
        private final EnumSet<Type> types = EnumSet.noneOf(Type.class);

        EntityData(String name, Type... types) {
            this.types.addAll(Arrays.asList(types));
        }

        public boolean isType(Type type) {
            return this.types.contains(type);
        }

        public enum Type {
            BOOLEAN,
            COLOR,
            CAT,
            PROFESSION,
            HORSE_ARMOR,
            HORSE_COLOR,
            HORSE_VARIANT,
            HORSE_STYLE,
            RABBIT
        }
    }
}
