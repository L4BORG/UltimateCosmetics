package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CooldownCosmeticStorage;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 2/10/2015
 */
public final class MorphsListener implements Listener {
    private final MorphsModule module;
    private final Map<Player, String> cooldownPlayers = new HashMap<>();

    public MorphsListener(MorphsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            double damage = e.getDamage();
            if (damage >= p.getHealth()) {
                for(Morph cosmetic : this.module.getParent().getApi().getCosmetics(p, Morph.class)) {
                    cosmetic.remove();
                }
                return;
            }
        }
        if (e.getEntity() instanceof LivingEntity && this.isEntity(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("AbilityProjectile")) {
            e.setCancelled(true);
            e.getDamager().remove();
            this.module.getParent().removeEntity(e.getDamager());
            return;
        }
        if (e.getDamager().hasMetadata("MorphAbility") || (e.getEntity() instanceof LivingEntity && this.isEntity(e.getEntity()))) e.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity().hasMetadata("AbilityProjectile")) {
            e.setCancelled(true);
            e.getEntity().remove();
            this.module.getParent().removeEntity(e.getEntity());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (this.module.getConfig().isAbilityItem(e.getItemDrop().getItemStack())) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (this.module.getConfig().isAbilityItem(e.getCurrentItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata("AbilityEgg")) {
            this.module.getParent().removeEntity(e.getEntity());
            e.getEntity().remove();
            int duration = e.getEntity().getMetadata("AbilityEgg").get(0).asInt();
            final Ageable chicken = (Ageable) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.CHICKEN);
            chicken.setBaby();
            chicken.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration * 20, 1));
            this.module.getParent().queueEntity(chicken);
            new BukkitRunnable() {
                @Override
                public void run() {
                    MorphsListener.this.module.getParent().removeEntity(chicken);
                    chicken.remove();
                }
            }.runTaskLater(this.module.getParent(), 20 * duration);
            return;
        }
        if (e.getEntity().hasMetadata("AbilityProjectile")) {
            this.module.getParent().removeEntity(e.getEntity());
            e.getEntity().remove();
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata("AbilityItem")) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            e.getItem().remove();
            String metadata = e.getItem().getMetadata("AbilityItem").get(0).asString();
            if (metadata.contains("cobweb-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, 1));
                return;
            }
            if (metadata.contains("milkbucket-")) {
                for (PotionEffect effect : p.getActivePotionEffects()) p.removePotionEffect(effect.getType());
                return;
            }
            if (metadata.contains("ironingot-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 20, 1));
                return;
            }
            if (metadata.contains("mushroom-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration * 20, 1));
                return;
            }
            if (metadata.contains("porkchop-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration * 20, 1));
            }
        }
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onInteract(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if(e.getAction() == Action.PHYSICAL || p.getItemInHand() == null) return;
        Morphs config = this.module.getConfig();
        if (this.module.getParent().getApi().hasCosmetics(p, Morph.class) && config.isAbilityItem(p.getItemInHand())) {
            MorphStorage morph = config.getMorphByAbilityItem(p.getItemInHand());
            if (e.getAction() != Action.PHYSICAL) e.setCancelled(true);
            if (this.isInCooldown(p, morph)) return;
            this.addToCooldown(p, morph);
            Location l = p.getEyeLocation();
            MetadataValue durationValue = new FixedMetadataValue(this.module.getParent(), morph.getAbilityDuration());
            MetadataValue playerValue = new FixedMetadataValue(this.module.getParent(), p.getName());
            PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1);
            switch (morph.getMorphType()) {
                case BAT:
                case ENDER_DRAGON:
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.setAllowFlight(false);
                            p.setFlying(false);
                        }
                    }.runTaskLater(this.module.getParent(), 20 * morph.getAbilityDuration());
                    break;
                case BLAZE:
                case GHAST:
                    Vector velocity = l.getDirection();
                    velocity.multiply(2);
                    Fireball fireball = p.launchProjectile(Fireball.class, velocity);
                    fireball.setMetadata("AbilityProjectile", playerValue);
                    fireball.setIsIncendiary(false);
                    this.module.getParent().queueEntity(fireball);
                    break;
                case SPIDER:
                case CAVE_SPIDER:
                    this.throwItems(p, new CustomItem(Material.WEB, 1, 0), morph.getAbilityDuration(), "cobweb");
                    break;
                case CHICKEN:
                    for (int i = 0; i < 10; i++) {
                        velocity = l.getDirection();
                        velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
                        velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
                        velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
                        Egg egg = p.launchProjectile(Egg.class, velocity);
                        egg.setMetadata("AbilityEgg", durationValue);
                        this.module.getParent().queueEntity(egg);
                    }
                    break;
                case COW:
                    this.throwItems(p, new CustomItem(Material.MILK_BUCKET, 1, 0), morph.getAbilityDuration(), "milkbucket");
                    break;
                case CREEPER:
                    p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 0);
                    for (Entity near : p.getNearbyEntities(5.0, 5.0, 5.0)) {
                        near.setVelocity(near.getLocation().getDirection().multiply(-2));
                    }
                    break;
                case GUARDIAN:
                case ELDER_GUARDIAN:
                case HORSE:
                case MULE:
                case DONKEY:
                case UNDEAD_HORSE:
                case SKELETON_HORSE:
                case OCELOT:
                case SILVERFISH:
                case SQUID:
                case WOLF:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                    break;
                case ENDERMAN:
                case ENDERMITE:
                    Location loc = p.getTargetBlock(EnumSet.noneOf(Material.class), 50).getLocation();
                    loc = loc.getWorld().getHighestBlockAt(loc).getLocation();
                    p.teleport(loc);
                    loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, null);
                    break;
                case GIANT:
                    final Set<Zombie> zombies = new HashSet<>();
                    for (int i = 0; i < 5; i++) {
                        final Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
                        this.module.getParent().queueEntity(zombie);
                        zombie.addPotionEffect(speed);
                        zombie.setBaby(false);
                        zombie.setMetadata("MorphAbility", playerValue);
                        zombies.add(zombie);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Zombie zombie : zombies) {
                                MorphsListener.this.module.getParent().removeEntity(zombie);
                                zombie.remove();
                            }
                        }
                    }.runTaskLater(this.module.getParent(), 20 * morph.getAbilityDuration());
                    break;
                case IRON_GOLEM:
                    this.throwItems(p, new CustomItem(Material.IRON_INGOT, 1, 0), morph.getAbilityDuration(), "ironingot");
                    break;
                case MAGMA_CUBE:
                case SLIME:
                case RABBIT:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, morph.getAbilityDuration() * 20, 1));
                    break;
                case MUSHROOM_COW:
                    this.throwItems(p, new CustomItem(Material.RED_MUSHROOM, 1, 0), morph.getAbilityDuration(), "mushroom");
                    break;
                case PIG:
                    this.throwItems(p, new CustomItem(Material.GRILLED_PORK, 1, 0), morph.getAbilityDuration(), "porkchop");
                    break;
                case PIG_ZOMBIE:
                    final Set<Zombie> pigZombies = new HashSet<>();
                    for (int i = 0; i < 5; i++) {
                        final Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.PIG_ZOMBIE);
                        this.module.getParent().queueEntity(zombie);
                        zombie.setBaby(true);
                        zombie.addPotionEffect(speed);
                        zombie.setMetadata("MorphAbility", playerValue);
                        pigZombies.add(zombie);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Zombie zombie : pigZombies) {
                                MorphsListener.this.module.getParent().removeEntity(zombie);
                                zombie.remove();
                            }
                        }
                    }.runTaskLater(this.module.getParent(), 20 * morph.getAbilityDuration());
                    break;
                case SHEEP:
                    this.throwColoredWool(p, morph.getAbilityDuration());
                    break;
                case SKELETON:
                case WITHER_SKELETON:
                    for (int i = 0; i < 3; i++) {
                        velocity = l.getDirection();
                        velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
                        velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
                        velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
                        Arrow arrow = p.launchProjectile(Arrow.class, velocity);
                        arrow.setMetadata("AbilityProjectile", playerValue);
                        this.module.getParent().queueEntity(arrow);
                    }
                    break;
                case SNOWMAN:
                    for (int i = 0; i < 3; i++) {
                        velocity = l.getDirection();
                        velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
                        velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
                        velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
                        Snowball snowball = p.launchProjectile(Snowball.class, velocity);
                        snowball.setMetadata("AbilityProjectile", playerValue);
                        this.module.getParent().queueEntity(snowball);
                    }
                    break;
                case VILLAGER:
                    final Set<Villager> villagers = new HashSet<>();
                    for (int i = 0; i < 5; i++) {
                        final Villager villager = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                        this.module.getParent().queueEntity(villager);
                        villager.setBaby();
                        villager.addPotionEffect(speed);
                        villagers.add(villager);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Villager villager : villagers) {
                                MorphsListener.this.module.getParent().removeEntity(villager);
                                villager.remove();
                            }
                        }
                    }.runTaskLater(this.module.getParent(), 20 * morph.getAbilityDuration());
                    break;
                case WITCH:
                    ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
                    ItemStack item = new ItemStack(Material.SPLASH_POTION, 1);
                    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 60, 1), true);
                    item.setItemMeta(potionMeta);
                    potion.setItem(item);
                    break;
                case WITHER:
                    velocity = l.getDirection();
                    velocity.multiply(2);
                    WitherSkull skull = p.launchProjectile(WitherSkull.class, velocity);
                    skull.setMetadata("AbilityProjectile", playerValue);
                    skull.setCharged(true);
                    skull.setIsIncendiary(false);
                    this.module.getParent().queueEntity(skull);
                    break;
                case ZOMBIE:
                case ZOMBIE_VILLAGER:
                    final Set<Zombie> babyZombies = new HashSet<>();
                    for (int i = 0; i < 5; i++) {
                        final Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
                        this.module.getParent().queueEntity(zombie);
                        zombie.addPotionEffect(speed);
                        zombie.setBaby(true);
                        zombie.setVillager(morph.getMorphType() != DisguiseType.ZOMBIE);
                        zombie.setMetadata("MorphAbility", playerValue);
                        babyZombies.add(zombie);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Zombie zombie : babyZombies) {
                                MorphsListener.this.module.getParent().removeEntity(zombie);
                                zombie.remove();
                            }
                        }
                    }.runTaskLater(this.module.getParent(), 20 * morph.getAbilityDuration());
                    break;
            }
            p.updateInventory();
        }
    }

    private boolean isEntity(Entity ent) {
        if (ent.hasMetadata("AbilityItem") || ent instanceof LivingEntity && ent.getCustomName() != null && ent.getCustomName().endsWith("AbilityItem"))
            return true;
        for (Entity e : this.module.getParent().getEntitiesQueue()) {
            if (ent.getUniqueId().equals(e.getUniqueId())) return true;
        }
        return false;
    }

    private boolean isInCooldown(Player p, CooldownCosmeticStorage storage) {
        if (this.cooldownPlayers.get(p) != null && this.cooldownPlayers.get(p).split(":")[0].equals(storage.getIdentifier())) {
            Methods.informPlayerNoPermission(p, Placeholders.parse(this.module.getParent().getLang().getGadgetsCooldown().replace("%timeleft%", String.valueOf(storage.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p).split(":")[1])) / 1000))));
            return true;
        }
        return false;
    }

    private void addToCooldown(final Player p, CooldownCosmeticStorage storage) {
        this.cooldownPlayers.put(p, storage.getIdentifier() + ':' + System.currentTimeMillis());
        new BukkitRunnable() {
            @Override
            public void run() {
                MorphsListener.this.cooldownPlayers.remove(p);
            }
        }.runTaskLater(this.module.getParent(), 20 * storage.getCooldown());
    }

    private void throwItems(LivingEntity p, CustomItem is, int duration, String metadata) {
        final Set<Item> items = new HashSet<>();
        MetadataValue metadataValue = new FixedMetadataValue(this.module.getParent(), metadata + '-' + duration);
        for (int i = 0; i < 10; i++) {
            is.setName(Random.getInt() + "AbilityItem");
            Vector velocity = p.getEyeLocation().getDirection();
            velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
            velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
            velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
            Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(), is);
            item.setVelocity(velocity);
            item.setMetadata("AbilityItem", metadataValue);
            this.module.getParent().queueEntity(item);
            items.add(item);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Item item : items) {
                    MorphsListener.this.module.getParent().removeEntity(item);
                    item.remove();
                }
            }
        }.runTaskLater(this.module.getParent(), 20 * duration);
    }

    private void throwColoredWool(LivingEntity p, int duration) {
        final Set<Item> items = new HashSet<>();
        MetadataValue metadataValue = new FixedMetadataValue(this.module.getParent(), "wool");
        for (int i = 0; i < 10; i++) {
            CustomItem is = new CustomItem(Material.WOOL);
            is.setDurability((short) Random.getInt(15));
            is.setName(Random.getInt() + "AbilityItem");
            Vector velocity = p.getEyeLocation().getDirection();
            velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
            velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
            velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
            Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(), is);
            item.setVelocity(velocity);
            item.setMetadata("AbilityItem", metadataValue);
            this.module.getParent().queueEntity(item);
            items.add(item);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Item item : items) {
                    MorphsListener.this.module.getParent().removeEntity(item);
                    item.remove();
                }

            }
        }.runTaskLater(this.module.getParent(), 20 * duration);
    }
}
