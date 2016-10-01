package com.j0ach1mmall3.ultimatecosmetics.modules.morphs;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CooldownCosmeticStorage;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
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
import org.bukkit.util.Vector;

import java.util.*;

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
                    cosmetic.remove(this.module.getParent());
                }
                return;
            }
        }
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && this.isEntity(e.getEntity())) e.setCancelled(true);
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
        if (this.module.getConfig().isAbilityItem(e.getItemDrop().getItemStack()) && this.module.getParent().getApi().hasCosmetics(e.getPlayer(), Morph.class)) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (this.module.getConfig().isAbilityItem(e.getCurrentItem()) && this.module.getParent().getApi().hasCosmetics((Player) e.getWhoClicked(), Morph.class)) e.setCancelled(true);
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                @Override
                public void run() {
                    MorphsListener.this.module.getParent().removeEntity(chicken);
                    chicken.remove();
                }
            }, duration * 20L);
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
            if (metadata.startsWith("cobweb-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, 1));
                return;
            }
            if (metadata.startsWith("milkbucket-")) {
                for (PotionEffect effect : p.getActivePotionEffects()) {
                    p.removePotionEffect(effect.getType());
                }
                return;
            }
            if (metadata.startsWith("ironingot-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 20, 1));
                return;
            }
            if (metadata.startsWith("mushroom-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration * 20, 1));
                return;
            }
            if (metadata.startsWith("porkchop-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration * 20, 1));
            }
        }
    }

    @EventHandler
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
            MetadataValue metadataValue = new FixedMetadataValue(this.module.getParent(), p.getName());
            switch (morph.getMorphType()) {
                case BAT:
                case ENDER_DRAGON:
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            p.setAllowFlight(false);
                            p.setFlying(false);
                        }
                    }, morph.getAbilityDuration() * 20L);
                    break;
                case BLAZE:
                case GHAST:
                    Vector velocity = l.getDirection();
                    velocity.multiply(2);
                    Fireball fireball = p.launchProjectile(Fireball.class, velocity);
                    fireball.setMetadata("AbilityProjectile", metadataValue);
                    fireball.setIsIncendiary(false);
                    this.module.getParent().queueEntity(fireball);
                    break;
                case SPIDER:
                case CAVE_SPIDER:
                    this.throwItems(p, new JLibItem.Builder().withType(Material.WEB).withAmount(1), morph.getAbilityDuration(), "cobweb");
                    break;
                case CHICKEN:
                    for (int i = 0; i < 10; i++) {
                        velocity = l.getDirection();
                        velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
                        velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
                        velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
                        Egg egg = p.launchProjectile(Egg.class, velocity);
                        egg.setMetadata("AbilityEgg", new FixedMetadataValue(this.module.getParent(), morph.getAbilityDuration()));
                        this.module.getParent().queueEntity(egg);
                    }
                    break;
                case COW:
                    this.throwItems(p, new JLibItem.Builder().withType(Material.MILK_BUCKET).withAmount(1), morph.getAbilityDuration(), "milkbucket");
                    break;
                case CREEPER:
                    p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 0);
                    for (Entity near : p.getNearbyEntities(5.0, 5.0, 5.0)) {
                        near.setVelocity(near.getLocation().getDirection().multiply(-2));
                    }
                    break;
                case ENDERMAN:
                case ENDERMITE:
                case SHULKER:
                    Location loc = p.getTargetBlock(EnumSet.noneOf(Material.class), 50).getLocation();
                    loc = loc.getWorld().getHighestBlockAt(loc).getLocation();
                    p.teleport(loc);
                    loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, null);
                    break;
                case GIANT:
                    final Set<Entity> zombies = new HashSet<>();
                    for (int i = 0; i < 5; i++) {
                        Zombie zombie = p.getWorld().spawn(p.getLocation(), Zombie.class);
                        zombie.setMetadata("MorphAbility", metadataValue);
                        this.module.getParent().queueEntity(zombie);
                        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                        zombie.setBaby(false);
                        zombies.add(zombie);
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            for(Entity zombie : zombies) {
                                MorphsListener.this.module.getParent().removeEntity(zombie);
                                zombie.remove();
                            }
                        }
                    }, morph.getAbilityDuration() * 20L);
                    break;
                case IRON_GOLEM:
                    this.throwItems(p, new JLibItem.Builder().withType(Material.IRON_INGOT).withAmount(1), morph.getAbilityDuration(), "ironingot");
                    break;
                case MAGMA_CUBE:
                case SLIME:
                case RABBIT:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, morph.getAbilityDuration() * 20, 1));
                    break;
                case MUSHROOM_COW:
                    this.throwItems(p, new JLibItem.Builder().withType(Material.RED_MUSHROOM).withAmount(1), morph.getAbilityDuration(), "mushroom");
                    break;
                case PIG:
                    this.throwItems(p, new JLibItem.Builder().withType(Material.GRILLED_PORK).withAmount(1), morph.getAbilityDuration(), "porkchop");
                    break;
                case PIG_ZOMBIE:
                    final Set<Entity> pigZombies = new HashSet<>();
                    for (int i = 0; i < 5; i++) {
                        Zombie zombie = p.getWorld().spawn(p.getLocation(), PigZombie.class);
                        zombie.setMetadata("MorphAbility", metadataValue);
                        this.module.getParent().queueEntity(zombie);
                        zombie.setBaby(true);
                        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                        pigZombies.add(zombie);
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            for(Entity zombie : pigZombies) {
                                MorphsListener.this.module.getParent().removeEntity(zombie);
                                zombie.remove();
                            }
                        }
                    }, morph.getAbilityDuration() * 20L);
                    break;
                case SHEEP:
                    this.throwItems(p, new JLibItem.Builder().withType(Material.WOOL).withAmount(1), morph.getAbilityDuration(), "wool");
                    break;
                case SKELETON:
                case WITHER_SKELETON:
                    for (int i = 0; i < 3; i++) {
                        velocity = l.getDirection();
                        velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
                        velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
                        velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
                        Arrow arrow = p.launchProjectile(Arrow.class, velocity);
                        arrow.setMetadata("AbilityProjectile", metadataValue);
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
                        snowball.setMetadata("AbilityProjectile", metadataValue);
                        this.module.getParent().queueEntity(snowball);
                    }
                    break;
                case VILLAGER:
                    final Set<Entity> villagers = new HashSet<>();
                    for (int i = 0; i < 5; i++) {
                        Ageable villager = p.getWorld().spawn(p.getLocation(), Villager.class);
                        this.module.getParent().queueEntity(villager);
                        villager.setBaby();
                        villager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                        villagers.add(villager);
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            for(Entity villager : villagers) {
                                MorphsListener.this.module.getParent().removeEntity(villager);
                                villager.remove();
                            }
                        }
                    }, morph.getAbilityDuration() * 20L);
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
                    skull.setMetadata("AbilityProjectile", metadataValue);
                    skull.setCharged(true);
                    skull.setIsIncendiary(false);
                    this.module.getParent().queueEntity(skull);
                    break;
                case ZOMBIE:
                case ZOMBIE_VILLAGER:
                    final Set<Entity> babyZombies = new HashSet<>();
                    for (int i = 0; i < 5; i++) {
                        Zombie zombie = p.getWorld().spawn(p.getLocation(), Zombie.class);
                        zombie.setMetadata("MorphAbility", metadataValue);
                        this.module.getParent().queueEntity(zombie);
                        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                        zombie.setBaby(true);
                        zombie.setVillager(morph.getMorphType() != DisguiseType.ZOMBIE);
                        babyZombies.add(zombie);
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            for(Entity zombie : babyZombies) {
                                MorphsListener.this.module.getParent().removeEntity(zombie);
                                zombie.remove();
                            }
                        }
                    }, morph.getAbilityDuration() * 20L);
                    break;
                default:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                    break;
            }
            p.updateInventory();
        }
    }

    private boolean isEntity(Entity ent) {
        if (ent.hasMetadata("AbilityItem") || (ent.getCustomName() != null && "AbilityItem".equals(ent.getCustomName()))) return true;
        for (Entity e : this.module.getParent().getEntitiesQueue()) {
            if (ent.getUniqueId().equals(e.getUniqueId())) return true;
        }
        return false;
    }

    private boolean isInCooldown(Player p, CooldownCosmeticStorage storage) {
        if (this.cooldownPlayers.get(p) != null && this.cooldownPlayers.get(p).split(":")[0].equals(storage.getIdentifier())) {
            Methods.informPlayerNoPermission(p, Placeholders.parse(this.module.getParent().getLang().getAbilityCooldown().replace("%timeleft%", String.valueOf(storage.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p).split(":")[1])) / 1000))));
            return true;
        }
        return false;
    }

    private void addToCooldown(final Player p, CooldownCosmeticStorage storage) {
        this.cooldownPlayers.put(p, storage.getIdentifier() + ':' + System.currentTimeMillis());
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
            @Override
            public void run() {
                MorphsListener.this.cooldownPlayers.remove(p);
            }
        }, storage.getCooldown() * 20L);
    }

    private void throwItems(LivingEntity p, JLibItem.Builder jLibItem, int duration, String metadata) {
        final Set<Item> items = new HashSet<>();
        MetadataValue metadataValue = new FixedMetadataValue(this.module.getParent(), metadata + '-' + duration);
        for (int i = 0; i < 10; i++) {
            Vector velocity = p.getEyeLocation().getDirection();
            velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
            velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
            velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
            if("wool".equals(metadata)) jLibItem.withDurability((short) Random.getInt(15));
            Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(), jLibItem.withName(Random.getString(16, true, true)).build().getItemStack());
            item.setCustomName("AbilityItem");
            item.setCustomNameVisible(false);
            item.setMetadata("AbilityItem", metadataValue);
            this.module.getParent().queueEntity(item);
            item.setVelocity(velocity);
            items.add(item);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
            @Override
            public void run() {
                for(Item item : items) {
                    MorphsListener.this.module.getParent().removeEntity(item);
                    item.remove();
                }
            }
        }, duration * 20L);
    }
}
