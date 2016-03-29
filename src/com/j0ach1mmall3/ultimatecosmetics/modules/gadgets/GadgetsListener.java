package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.jlib.visual.Title;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CooldownCosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.Lang;
import com.j0ach1mmall3.ultimatecosmetics.data.DataLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 22/08/2015
 */
public final class GadgetsListener implements Listener {
    @SuppressWarnings("deprecation")
    private static final EnumSet<Material> PAINTBALL_DENIED = EnumSet.of(Material.AIR, Material.getMaterial(166), Material.WALL_SIGN, Material.SIGN_POST);

    private final GadgetsModule module;
    private final Set<Player> firePlayers = new HashSet<>();
    private final Set<Player> diamondShowerPlayers = new HashSet<>();
    private final Set<Player> paintTrailPlayers = new HashSet<>();
    private final Map<Player, String> cooldownPlayers = new HashMap<>();
    private final Map<Player, Entity> fishingPlayers = new HashMap<>();

    public GadgetsListener(GadgetsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity ent : world.getEntities()) {
                        if (ent.hasMetadata("UCEntity") && "MelonThrower".equals(ent.getMetadata("UCEntity").get(0).asString()) && ent.isOnGround())
                            GadgetsListener.this.handleMelon(ent);
                        if (ent.hasMetadata("UCEntity") && "ColorBomb".equals(ent.getMetadata("UCEntity").get(0).asString()) && ent.isOnGround())
                            GadgetsListener.this.handleColorBomb(ent);
                        if (ent.hasMetadata("UCEntity") && "GoldFountain".equals(ent.getMetadata("UCEntity").get(0).asString()) && ent.isOnGround())
                            GadgetsListener.this.handleGoldFountain(ent);
                    }
                }
            }
        }.runTaskTimer(module.getParent(), 0L, 10L);
        new BukkitRunnable() {
            @Override
            public void run() {
                final Set<Item> items = new HashSet<>();
                CustomItem customItem = new CustomItem(Material.DIAMOND, 1, 0);

                for (Player p : GadgetsListener.this.diamondShowerPlayers) {
                    Sounds.broadcastSound(((Gadgets) GadgetsListener.this.module.getConfig()).getSound("DiamondShower", "Sound"), p.getLocation());
                    Location l = p.getLocation();
                    l.setY(l.getY() + 2.0);
                    customItem.setName(Random.getString(16, true, true));
                    final Item i = l.getWorld().dropItemNaturally(l, customItem);
                    i.setPickupDelay(Integer.MAX_VALUE);
                    ((Main) GadgetsListener.this.module.getParent()).queueEntity(i);
                    items.add(i);
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Item i : items) {
                            i.remove();
                            ((Main) GadgetsListener.this.module.getParent()).removeEntity(i);
                        }
                    }
                }.runTaskLater(GadgetsListener.this.module.getParent(), 20L);
            }
        }.runTaskTimer(module.getParent(), 0L, ((Gadgets) this.module.getConfig()).getIntValue("DiamondShower", "Interval"));
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getSlot() == ((Gadgets) this.module.getConfig()).getGadgetSlot() && ((Gadgets) this.module.getConfig()).isGadgetItem(e.getCurrentItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (this.firePlayers.contains(p)) {
            Block b = p.getLocation().getBlock();
            if (b.getType() == Material.AIR) this.setFakeBlock(b, new MaterialData(Material.FIRE), ((Gadgets) this.module.getConfig()).getIntValue("FireTrail", "RemoveDelay"));
        }
        if (this.paintTrailPlayers.contains(p)) this.setRandomClay(p.getLocation().getBlock().getRelative(BlockFace.DOWN), ((Gadgets) this.module.getConfig()).getIntValue("PaintTrail", "RestoreDelay"));
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().getInventory().getHeldItemSlot() == ((Gadgets) this.module.getConfig()).getGadgetSlot() && ((Gadgets) this.module.getConfig()).isGadgetItem(e.getItemDrop().getItemStack())) e.setCancelled(true);
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onInteract(final PlayerInteractEvent e) {
        if(e.getAction() == Action.PHYSICAL) return;
        final Player p = e.getPlayer();
        final Gadgets config = ((Gadgets) this.module.getConfig());
        if (config.isGadgetItem(p.getItemInHand())) {
            GadgetStorage gadget = config.getGadgetStorage(p.getItemInHand());
            e.setCancelled(true);
            p.updateInventory();

            if (p.isInsideVehicle()) {
                ((Main) this.module.getParent()).informPlayerNoPermission(p, ((Main) this.module.getParent()).getLang().getDismountVehicle());
                p.updateInventory();
                return;
            }

            if(!gadget.getIdentifier().equalsIgnoreCase("GrapplingHook")) {
                if(this.isInCooldown(p, gadget) || this.checkAmmo(p, gadget)) return;
                this.addToCooldown(p, gadget);
            }

            if (!config.isKeepGadget()) p.setItemInHand(null);
            p.updateInventory();

            Location l = p.getEyeLocation();
            MetadataValue metadataValue = new FixedMetadataValue(this.module.getParent(), p.getName());
            Sounds.broadcastSound(gadget.getSound(), l);

            switch (gadget.getIdentifier()) {
                case "Enderbow":
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    arrow.setVelocity(arrow.getVelocity().multiply(2));
                    arrow.setMetadata("Enderbow", metadataValue);
                    break;

                case "EtherealPearl":
                    EnderPearl pearl = p.launchProjectile(EnderPearl.class);
                    pearl.setShooter(null);
                    pearl.setPassenger(p);
                    pearl.setMetadata("EtherealPearl", metadataValue);
                    pearl.setVelocity(pearl.getVelocity().multiply(config.getIntValue("EtherealPearl", "Speed")));
                    break;

                case "PaintballGun":
                    Snowball snow = p.launchProjectile(Snowball.class);
                    snow.setMetadata("Paintball", metadataValue);
                    snow.setVelocity(snow.getVelocity().multiply(config.getIntValue("PaintballGun", "Speed")));
                    break;

                case "FlyingPig":
                    Bat bat = (Bat) p.getWorld().spawnEntity(l, EntityType.BAT);
                    bat.setMetadata("FlyingPig", metadataValue);
                    bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
                    bat.setNoDamageTicks(Integer.MAX_VALUE);
                    Pig pig = (Pig) p.getWorld().spawnEntity(l, EntityType.PIG);
                    pig.setCustomName(gadget.getGuiItem().getItem().getItemMeta().getDisplayName());
                    pig.setCustomNameVisible(true);
                    pig.setAdult();
                    pig.setAgeLock(true);
                    pig.setBreed(false);
                    pig.setSaddle(true);
                    pig.setNoDamageTicks(Integer.MAX_VALUE);
                    bat.setPassenger(pig);
                    pig.setPassenger(p);
                    ((Main) this.module.getParent()).queueEntity(bat);
                    ((Main) this.module.getParent()).queueEntity(pig);
                    break;

                case "BatBlaster":
                    final Set<Bat> bats = new HashSet<>();

                    for (int a = 0; a < config.getIntValue("BatBlaster", "Amount"); a++) {
                        final Bat b = (Bat) p.getWorld().spawnEntity(l, EntityType.BAT);
                        b.setVelocity(l.getDirection().multiply(config.getIntValue("BatBlaster", "Speed")));
                        b.setNoDamageTicks(20 * config.getIntValue("BatBlaster", "RemoveDelay"));
                        ((Main) this.module.getParent()).queueEntity(b);
                        bats.add(b);
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Bat b : bats) {
                                b.damage(1000.0);
                                ((Main) GadgetsListener.this.module.getParent()).removeEntity(b);
                            }
                        }
                    }.runTaskLater(this.module.getParent(), 20 * config.getIntValue("BatBlaster", "RemoveDelay"));
                    break;

                case "CATapult":
                    final List<Ocelot> cats = new ArrayList<>();

                    for (int a = 0; a < config.getIntValue("CATapult", "Amount"); a++) {
                        final Ocelot cat = (Ocelot) p.getWorld().spawnEntity(l, EntityType.OCELOT);
                        cat.setTamed(true);
                        Vector v = l.getDirection().multiply(config.getIntValue("CATapult", "Speed"));
                        v.setX(Random.getDouble());
                        v.setY(Random.getDouble());
                        v.setZ(Random.getDouble());
                        cat.setVelocity(v);
                        cat.setNoDamageTicks(Integer.MAX_VALUE);
                        ((Main) this.module.getParent()).queueEntity(cat);
                        cats.add(cat);
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Ocelot cat : cats) {
                                cat.getWorld().playEffect(cat.getLocation(), Effect.EXPLOSION_HUGE, 0);
                                cat.remove();
                                ((Main) GadgetsListener.this.module.getParent()).removeEntity(cat);
                            }
                        }
                    }.runTaskLater(this.module.getParent(), 20 * config.getIntValue("CATapult", "ExplosionDelay"));
                    break;

                case "RailGun":
                    Vector vec = p.getLocation().getDirection();
                    Location lastParticle = new Location(p.getWorld(), l.getX(), l.getY() - 0.3, l.getZ());
                    for (int j = 0; j < config.getIntValue("RailGun", "Range") << 1; j++) {
                        if (lastParticle.getBlock().getType() != Material.AIR) return;
                        Methods.broadcastSafeParticle(lastParticle, Effect.FIREWORKS_SPARK, 0, 0, 0.0F, 1, 100);
                        lastParticle.add(vec.getX(), vec.getY(), vec.getZ());
                    }
                    break;

                case "CryoTube":
                    this.setFakeBlock(p.getLocation().getBlock(), new MaterialData(Material.ICE, (byte) 0), config.getIntValue("CryoTube", "RemoveDelay"));
                    this.setFakeBlock(l.getBlock(), new MaterialData(Material.ICE, (byte) 0), config.getIntValue("CryoTube", "RemoveDelay"));
                    Methods.broadcastSafeParticle(p.getLocation(), Effect.STEP_SOUND, 79, 79, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
                    Methods.broadcastSafeParticle(l, Effect.STEP_SOUND, 79, 79, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
                    break;

                case "Rocket":
                    new BukkitRunnable() {
                        private int counter = config.getIntValue("Rocket", "Countdown");

                        @Override
                        public void run() {
                            if (this.counter > 0) {
                                if (ReflectionAPI.verBiggerThan(1, 8)) new Title(p, ChatColor.RED + String.valueOf(this.counter), 5, 10, 5).send();
                                else p.sendMessage(ChatColor.RED + String.valueOf(this.counter));
                                this.counter--;
                            } else {
                                if (ReflectionAPI.verBiggerThan(1, 8)) new Title(p, ChatColor.RED + "Lift-Off", 0, 10, 0).send();
                                else p.sendMessage(ChatColor.RED + "Lift-Off");
                                Firework fw = p.getWorld().spawn(p.getLocation(), Firework.class);
                                FireworkMeta fm = fw.getFireworkMeta();
                                FireworkEffect.Builder effect = FireworkEffect.builder();
                                effect.with(FireworkEffect.Type.BALL_LARGE);
                                effect.withColor(Color.fromRGB(Random.getInt(255), Random.getInt(255), Random.getInt(255)));
                                effect.withFade(Color.fromRGB(Random.getInt(255), Random.getInt(255), Random.getInt(255)));
                                effect.withTrail();
                                fm.addEffect(effect.build());
                                fm.setPower(4);
                                fw.setFireworkMeta(fm);
                                fw.setPassenger(p);
                                fw.setVelocity(new Vector(0, config.getIntValue("Rocket", "Speed"), 0));
                                p.setFallDistance(-100.0F);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(this.module.getParent(), 0L, 20L);
                    break;

                case "PoopBomb":
                    final Set<Item> items = new HashSet<>();
                    CustomItem customItem = new CustomItem(Material.INK_SACK, 1, (short) 3);

                    for (int a = 0; a < config.getIntValue("PoopBomb", "Amount"); a++) {
                        int x = Random.getInt(config.getIntValue("PoopBomb", "Radius") << 1) - config.getIntValue("PoopBomb", "Radius");
                        int z = Random.getInt(config.getIntValue("PoopBomb", "Radius") << 1) - config.getIntValue("PoopBomb", "Radius");
                        customItem.setName(Random.getString(16, true, true));
                        final Item item = l.getWorld().dropItemNaturally(l.add(x , 15, z), customItem);
                        item.setPickupDelay(Integer.MAX_VALUE);
                        ((Main) this.module.getParent()).queueEntity(item);
                        items.add(item);
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Item item : items) {
                                Methods.broadcastSafeParticle(item.getLocation(), Effect.SMOKE, 0, 0, 2.0F, 10, 100);
                                item.remove();
                                ((Main) GadgetsListener.this.module.getParent()).removeEntity(item);
                            }
                        }
                    }.runTaskLater(this.module.getParent(), config.getIntValue("PoopBomb", "RemoveDelay") * 20);
                    break;

                case "GrapplingHook":
                    if (this.fishingPlayers.containsKey(p)) this.fishingPlayers.get(p).remove();
                    Fish hook = p.launchProjectile(Fish.class);
                    hook.setMetadata("GrapplingHook", metadataValue);
                    this.fishingPlayers.put(p, hook);
                    break;

                case "SelfDestruct":
                    for (Entity near : p.getNearbyEntities(config.getIntValue("SelfDestruct", "PushbackRange"), config.getIntValue("SelfDestruct", "PushbackRange"), config.getIntValue("SelfDestruct", "PushbackRange"))) {
                        near.setVelocity(near.getLocation().getDirection().multiply(-2));
                        Methods.broadcastSafeParticle(p.getLocation(), Effect.EXPLOSION_HUGE, 0, 0, 0.0F, 1, 100);
                        p.playEffect(EntityEffect.HURT);
                    }
                    break;

                case "SlimeVasion":
                    final Set<Slime> slimes = new HashSet<>();

                    for (int a = 0; a < config.getIntValue("SlimeVasion", "Amount"); a++) {
                        final Slime slime = (Slime) p.getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
                        slime.setSize(Random.getInt(1, 4));
                        slime.setNoDamageTicks(Integer.MAX_VALUE);
                        slime.setMetadata("SlimeVasion", metadataValue);
                        ((Main) this.module.getParent()).queueEntity(slime);
                        slimes.add(slime);
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Slime slime : slimes) {
                                slime.remove();
                                ((Main) GadgetsListener.this.module.getParent()).removeEntity(slime);
                            }
                        }
                    }.runTaskLater(this.module.getParent(), config.getIntValue("SlimeVasion", "RemoveDelay") * 20);
                    break;

                case "FunGun":
                    for (int a = 0; a < 5; a++) {
                        Snowball s = p.launchProjectile(Snowball.class);
                        s.setMetadata("FunGun", metadataValue);
                        s.setShooter(p);
                    }
                    break;

                case "MelonThrower":
                case "ColorBomb":
                case "GoldFountain":
                    ItemStack stack = gadget.getGuiItem().getItem().clone();
                    CustomItem ci = new CustomItem(stack);
                    ci.setName(Random.getString(16, true, true));
                    Item item = p.getWorld().dropItemNaturally(l, ci);
                    item.setVelocity(l.getDirection().multiply(1.5f));
                    item.setMetadata("UCEntity", new FixedMetadataValue(this.module.getParent(), gadget.getIdentifier()));
                    item.setPickupDelay(Integer.MAX_VALUE);
                    ((Main) this.module.getParent()).queueEntity(item);
                    break;

                case "FireTrail":
                    this.firePlayers.add(p);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, config.getIntValue("FireTrail", "Duration") * 20, config.getIntValue("FireTrail", "SpeedMultiplier") - 1));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            GadgetsListener.this.firePlayers.remove(p);
                        }
                    }.runTaskLater(this.module.getParent(), config.getIntValue("FireTrail", "Duration") * 20);
                    break;

                case "DiamondShower":
                    this.diamondShowerPlayers.add(p);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            GadgetsListener.this.diamondShowerPlayers.remove(p);
                        }
                    }.runTaskLater(this.module.getParent(), config.getIntValue("DiamondShower", "Duration") * 20);
                    break;

                case "PaintTrail":
                    this.paintTrailPlayers.add(p);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            GadgetsListener.this.paintTrailPlayers.remove(p);
                        }
                    }.runTaskLater(this.module.getParent(), config.getIntValue(gadget.getIdentifier(), "Duration") * 20);
                    break;
            }
            p.updateInventory();
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata("UCEntity") && "MelonSlice".equals(e.getItem().getMetadata("UCEntity").get(0).asString())) {
            e.setCancelled(true);
            e.getItem().remove();
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ((Gadgets) this.module.getConfig()).getIntValue("MelonThrower", "Speed.Duration") * 20, ((Gadgets) this.module.getConfig()).getIntValue("MelonThrower", "Speed.Multiplier") - 1));
        }
        if (this.isEntity(e.getItem())) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onPlayerVehicleExit(VehicleExitEvent e) {
        Entity ent = e.getVehicle().getVehicle();
        if (ent != null) {
            if (ent.hasMetadata("FlyingPig")) {
                ent.remove();
                ent.getPassenger().remove();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            double damage = e.getDamage();
            if (damage >= p.getHealth()) {
                for(Cosmetic cosmetic : ((Main) this.module.getParent()).getApi().getCosmetics(p, CosmeticType.GADGET)) {
                    cosmetic.remove();
                }
                return;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION && p.isInsideVehicle()) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e) {
        Entity ent = e.getRemover();
        if(ent instanceof Player) {
            if(((Main) this.module.getParent()).getApi().hasCosmetics(((Player) ent), CosmeticType.GADGET)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Entity ent = e.getRightClicked();
        if((ent instanceof Painting || ent instanceof ItemFrame) && ((Main) this.module.getParent()).getApi().hasCosmetics(e.getPlayer(), CosmeticType.GADGET)) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("Enderbow") || e.getDamager().hasMetadata("SlimeVasion"))
            e.setCancelled(true);
        if (e.getDamager().hasMetadata("GrapplingHook")) {
            e.setCancelled(true);
            if (((Gadgets) this.module.getConfig()).getBooleanValue("GrapplingHook", "PullPlayers") && e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                Player shooter = Bukkit.getPlayer(e.getDamager().getMetadata("GrapplingHook").get(0).asString());
                e.getDamager().remove();
                this.fishingPlayers.remove(p);
                this.pullEntityToLocation(p, shooter.getLocation());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Enderbow")) {
            Player p = Bukkit.getPlayer(ent.getMetadata("Enderbow").get(0).asString());
            Location l = ent.getLocation();
            l.setPitch(p.getLocation().getPitch());
            l.setYaw(p.getLocation().getYaw());
            p.teleport(l);
            p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 0);
            ent.remove();
            return;
        }
        if (ent.hasMetadata("EtherealPearl") && ent.getPassenger() != null && ent.getPassenger() instanceof Player) {
            Entity p = ent.getPassenger();
            p.eject();
            Location l = p.getLocation();
            l.setY(l.getWorld().getHighestBlockYAt(l));
            ent.remove();
            p.teleport(l);
            return;
        }
        if (ent.hasMetadata("Paintball")) {
            Location l = ent.getLocation();
            Sounds.broadcastSound(((Gadgets) this.module.getConfig()).getSound("PaintTrail", "Sound"), l);
            Iterator<Block> iterator = new BlockIterator(l.getWorld(), l.toVector(), ent.getVelocity().normalize(), 0.0D, ((Gadgets) this.module.getConfig()).getIntValue("PaintballGun", "PaintSize"));
            while (iterator.hasNext()) {
                Block block = iterator.next();
                this.setRandomClay(block, ((Gadgets) this.module.getConfig()).getIntValue("PaintballGun", "RestoreDelay"));
            }
            return;
        }
        if (ent.hasMetadata("GrapplingHook")) {
            if (!ent.getNearbyEntities(1.0, 1.0, 1.0).isEmpty() && ent.getNearbyEntities(1.0, 1.0, 1.0).get(0).getType() == EntityType.PLAYER) return;
            Player p = Bukkit.getPlayer(ent.getMetadata("GrapplingHook").get(0).asString());
            this.fishingPlayers.remove(p);
            this.pullEntityToLocation(p, ent.getLocation());
            ent.remove();
            return;
        }
        if (ent.hasMetadata("FunGun")) {
            Location l = ent.getLocation();
            ent.remove();
            Sounds.broadcastSound(((Gadgets) this.module.getConfig()).getSound("CATapult", "Sound"), l);
            Methods.broadcastSafeParticle(l, Effect.HEART, 0, 0, 0.0F, 0.2F, 0.0F, 0.0F, 1, 100);
            Methods.broadcastSafeParticle(l, Effect.LAVA_POP, 0, 0, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(5.0, 5.0, 5.0)) {
            if(this.isEntity(ent)) ent.remove();
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(5.0, 5.0, 5.0)) {
            if(this.isEntity(ent)) ent.remove();
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        for(Entity ent : p.getNearbyEntities(5.0, 5.0, 5.0)) {
            if(this.isEntity(ent)) ent.remove();
        }
    }

    @EventHandler
    public void onInventoryPickup(InventoryPickupItemEvent e) {
        if (this.isEntity(e.getItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        if (this.isEntity(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        if (this.isEntity(e.getEntity())) e.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    private void setRandomClay(Block block, int delay) {
        if(PAINTBALL_DENIED.contains(block.getType())) return;
        Methods.broadcastSafeParticle(block.getLocation(), Effect.MAGIC_CRIT, 0, 0, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
        this.setFakeBlock(block, new MaterialData(Material.STAINED_CLAY, (byte) Random.getInt(15)), delay);
    }

    private void pullEntityToLocation(Entity p, Location l) {
        Location el = p.getLocation().add(0, 0.5, 0);
        p.teleport(el);
        double g = -0.08;
        double t = l.distance(el);
        double vx = 1.07 * t * (l.getX() - el.getX()) / t;
        double vy = 1.03 * t * (l.getY() - el.getY()) / t - 0.5 * g * t;
        double vz = 1.07 * t * (l.getZ() - el.getZ()) / t;
        p.setVelocity(new Vector(vx, vy, vz));
        p.setFallDistance(-100.0F);
    }

    private void handleMelon(Entity ent) {
        MetadataValue metadataValue = new FixedMetadataValue(this.module.getParent(), "MelonSlice");
        Location l = ent.getLocation();
        ((Main) this.module.getParent()).removeEntity(ent);
        ent.remove();
        Sounds.broadcastSound(((Gadgets) this.module.getConfig()).getSound("SelfDestruct", "Sound"), l);
        Methods.broadcastSafeParticle(l, Effect.STEP_SOUND, 103, 103, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
        for (int a = 0; a < ((Gadgets) this.module.getConfig()).getIntValue("MelonThrower", "Amount"); a++) {
            final Item i = l.getWorld().dropItemNaturally(l, new CustomItem(Material.MELON, 1, 0, Random.getString(16, true, true)));
            ((Main) this.module.getParent()).queueEntity(i);
            i.setMetadata("UCEntity", metadataValue);
            new BukkitRunnable() {
                @Override
                public void run() {
                    i.remove();
                    ((Main) GadgetsListener.this.module.getParent()).removeEntity(i);
                }
            }.runTaskLater(this.module.getParent(), ((Gadgets) this.module.getConfig()).getIntValue("MelonThrower", "RemoveDelay") * 20);
        }
    }

    private void handleColorBomb(Entity ent) {
        final Location l = ent.getLocation();
        ((Main) this.module.getParent()).removeEntity(ent);
        ent.remove();
        new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {
                this.count++;
                if (this.count > ((Gadgets) GadgetsListener.this.module.getConfig()).getIntValue("ColorBomb", "RemoveDelay") * 20) this.cancel();
                else {
                    Sounds.broadcastSound(((Gadgets) GadgetsListener.this.module.getConfig()).getSound("ColorBomb", "Sound"), l);
                    String item = ((Gadgets) GadgetsListener.this.module.getConfig()).getStringList("ColorBomb", "Items").get(Random.getInt(((Gadgets) GadgetsListener.this.module.getConfig()).getStringList("ColorBomb", "Items").size() - 1));
                    final Item i = l.getWorld().dropItemNaturally(l, new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Random.getInt() + "UCEntity"));
                    i.setMetadata("UCEntity", new FixedMetadataValue(GadgetsListener.this.module.getParent(), "ColorBomb"));
                    ((Main) GadgetsListener.this.module.getParent()).queueEntity(i);
                    i.setVelocity(i.getVelocity().setY(0.75));
                    i.setPickupDelay(Integer.MAX_VALUE);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            i.remove();
                            ((Main) GadgetsListener.this.module.getParent()).removeEntity(i);
                        }
                    }.runTaskLater(GadgetsListener.this.module.getParent(), 5L);
                }
            }
        }.runTaskTimer(this.module.getParent(), 0L, 1L);
    }

    private void handleGoldFountain(Entity ent) {
        final Location l = ent.getLocation();
        ((Main) this.module.getParent()).removeEntity(ent);
        ent.remove();
        new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {
                this.count++;
                if (this.count > ((Gadgets) GadgetsListener.this.module.getConfig()).getIntValue("GoldFountain", "RemoveDelay") * 20) this.cancel();
                else {
                    Sounds.broadcastSound(((Gadgets) GadgetsListener.this.module.getConfig()).getSound("GoldFountain", "Sound"), l);
                    final Item i = l.getWorld().dropItemNaturally(l, new CustomItem(Material.GOLD_INGOT, 1, 0, Random.getInt() + "UCEntity"));
                    i.setMetadata("UCEntity", new FixedMetadataValue(GadgetsListener.this.module.getParent(), "GoldFountain"));
                    ((Main) GadgetsListener.this.module.getParent()).queueEntity(i);
                    i.setVelocity(i.getVelocity().setY(0.75));
                    i.setPickupDelay(Integer.MAX_VALUE);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            i.remove();
                            ((Main) GadgetsListener.this.module.getParent()).removeEntity(i);
                        }
                    }.runTaskLater(GadgetsListener.this.module.getParent(), 5L);
                }
            }
        }.runTaskTimer(this.module.getParent(), 0L, 1L);
    }

    private boolean isEntity(Entity ent) {
        if (ent.hasMetadata("UCEntity") || ent instanceof LivingEntity && ((LivingEntity) ent).getCustomName() != null && ((LivingEntity) ent).getCustomName().endsWith("UCEntity"))
            return true;
        for (Entity e : ((Main) this.module.getParent()).getEntitiesQueue()) {
            if (ent.getUniqueId().equals(e.getUniqueId())) return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private void setFakeBlock(final Block block, MaterialData materialData, int secDelay) {
        final Material mat = block.getType();
        final byte data = block.getData();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendBlockChange(block.getLocation(), materialData.getItemType(), materialData.getData());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendBlockChange(block.getLocation(), mat, data);
                }
            }
        }.runTaskLater(this.module.getParent(), secDelay * 20);
    }

    private boolean isInCooldown(Player p, CooldownCosmeticStorage storage) {
        if (this.cooldownPlayers.get(p) != null && this.cooldownPlayers.get(p).split(":")[0].equals(storage.getIdentifier())) {
            ((Main) this.module.getParent()).informPlayerNoPermission(p, Placeholders.parse(((Main) this.module.getParent()).getLang().getGadgetsCooldown().replace("%timeleft%", String.valueOf(storage.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p).split(":")[1])) / 1000))));
            return true;
        }
        return false;
    }

    private void addToCooldown(final Player p, CooldownCosmeticStorage storage) {
        this.cooldownPlayers.put(p, storage.getIdentifier() + ':' + System.currentTimeMillis());
        new BukkitRunnable() {
            @Override
            public void run() {
                GadgetsListener.this.cooldownPlayers.remove(p);
            }
        }.runTaskLater(this.module.getParent(), 20 * storage.getCooldown());
    }

    private boolean checkAmmo(Player p, GadgetStorage gadget) {
        DataLoader dataLoader = ((Main) this.module.getParent()).getDataLoader();
        Lang lang = ((Main) this.module.getParent()).getLang();
        if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
            int i = dataLoader.getAmmoCache().get(p).get(gadget.getIdentifier());
            if (i <= 0) {
                ((Main) this.module.getParent()).informPlayerNoPermission(p, Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                return true;
            } else {
                dataLoader.takeAmmo(gadget.getIdentifier(), p, 1);
                if(!lang.getLostAmmo().isEmpty()) p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
            }
        }
        return false;
    }
}
