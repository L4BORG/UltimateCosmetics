package com.j0ach1mmall3.ultimatecosmetics.modules.gadgets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.jlib.player.JLibPlayer;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CooldownCosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.Lang;
import com.j0ach1mmall3.ultimatecosmetics.data.DataLoader;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 22/08/2015
 */
public final class GadgetsListener implements Listener {
    private static final Set<Material> PAINTBALL_DENIED = EnumSet.of(Material.AIR, Material.getMaterial(166) == null ? Material.AIR : Material.getMaterial(166), Material.WALL_SIGN, Material.SIGN_POST);

    private final GadgetsModule module;
    private final Set<Player> firePlayers = new HashSet<>();
    private final Set<Player> diamondShowerPlayers = new HashSet<>();
    private final Set<Player> paintTrailPlayers = new HashSet<>();
    private final Map<Player, String> cooldownPlayers = new HashMap<>();
    private final Map<Player, Entity> fishingPlayers = new HashMap<>();

    public GadgetsListener(final GadgetsModule module) {
        this.module = module;
        module.getParent().getServer().getPluginManager().registerEvents(this, module.getParent());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(module.getParent(), new Runnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity ent : world.getEntities()) {
                        if (ent.hasMetadata("MelonThrower") && ent.isOnGround()) GadgetsListener.this.handleMelon(ent);
                        if (ent.hasMetadata("ColorBomb") && ent.isOnGround()) GadgetsListener.this.handleColorBomb(ent);
                        if (ent.hasMetadata("GoldFountain") && ent.isOnGround()) GadgetsListener.this.handleGoldFountain(ent);
                    }
                }
            }
        }, 10L, 10L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(module.getParent(), new Runnable() {
            @Override
            public void run() {
                final Set<Item> items = new HashSet<>();
                for (Player p : GadgetsListener.this.diamondShowerPlayers) {
                    Sounds.broadcastSound(GadgetsListener.this.module.getConfig().getByIdentifier("DiamondShower").getSound(), p.getLocation());
                    Location l = p.getLocation();
                    l.setY(l.getY() + 2.0);
                    Item i = l.getWorld().dropItemNaturally(l, new JLibItem.Builder().withType(Material.DIAMOND).withAmount(1).withName(Random.getString(16, true, true)).build().getItemStack());
                    i.setCustomName("UCEntity");
                    i.setCustomNameVisible(false);
                    i.setMetadata("DiamondShower", new FixedMetadataValue(GadgetsListener.this.module.getParent(), null));
                    GadgetsListener.this.module.getParent().queueEntity(i);
                    i.setPickupDelay(Integer.MAX_VALUE);
                    items.add(i);
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(module.getParent(), new Runnable() {
                    @Override
                    public void run() {
                        for (Item i : items) {
                            i.remove();
                            GadgetsListener.this.module.getParent().removeEntity(i);
                        }
                    }
                }, 20L);
            }
        }, (int) this.module.getConfig().getByIdentifier("DiamondShower").getAdditionalValues().get("Interval"), (int) this.module.getConfig().getByIdentifier("DiamondShower").getAdditionalValues().get("Interval"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if(e.getAction() == Action.PHYSICAL || p.getItemInHand() == null) return;
        Gadgets config = this.module.getConfig();
        if (config.isGadgetItem(p.getItemInHand())) {
            final GadgetStorage gadget = config.getGadgetStorage(p.getItemInHand());
            final Map<String, Object> additionalValues = gadget.getAdditionalValues();
            e.setCancelled(true);
            p.updateInventory();

            if (p.isInsideVehicle()) {
                Methods.informPlayerNoPermission(p, this.module.getParent().getLang().getDismountVehicle());
                p.updateInventory();
                return;
            }

            if(!"GrapplingHook".equalsIgnoreCase(gadget.getIdentifier())) {
                if(this.isInCooldown(p, gadget) || this.checkAmmo(p, gadget)) return;
                this.addToCooldown(p, gadget);
            }

            if (!config.isKeepGadget()) p.setItemInHand(null);
            p.updateInventory();

            Location l = p.getEyeLocation().add(0, 0.1, 0);
            final MetadataValue metadataValue = new FixedMetadataValue(this.module.getParent(), p.getName());
            Sounds.broadcastSound(gadget.getSound(), l);

            switch (gadget.getIdentifier()) {
                case "Enderbow":
                    Entity arrow = p.launchProjectile(Arrow.class);
                    arrow.setCustomName("UCEntity");
                    arrow.setCustomNameVisible(false);
                    arrow.setMetadata(gadget.getIdentifier(), metadataValue);
                    this.module.getParent().queueEntity(arrow);
                    arrow.setVelocity(arrow.getVelocity().multiply(2));
                    break;

                case "EtherealPearl":
                    Projectile pearl = p.launchProjectile(EnderPearl.class);
                    pearl.setCustomName("UCEntity");
                    pearl.setCustomNameVisible(false);
                    pearl.setMetadata(gadget.getIdentifier(), metadataValue);
                    this.module.getParent().queueEntity(pearl);
                    pearl.setShooter(null);
                    pearl.teleport(p.getLocation().add(0, 0.1, 0));
                    Methods.setPassenger(pearl, p);
                    pearl.setVelocity(pearl.getVelocity().multiply((int) additionalValues.get("Speed")));
                    break;

                case "PaintballGun":
                    Entity snowball = p.launchProjectile(Snowball.class);
                    snowball.setCustomName("UCEntity");
                    snowball.setCustomNameVisible(false);
                    snowball.setMetadata(gadget.getIdentifier(), metadataValue);
                    this.module.getParent().queueEntity(snowball);
                    snowball.setVelocity(snowball.getVelocity().multiply((int) additionalValues.get("Speed")));
                    break;

                case "FlyingPig":
                    LivingEntity bat = p.getWorld().spawn(p.getLocation().add(0, 0.1, 0), Bat.class);
                    bat.setCustomName("UCEntity");
                    bat.setCustomNameVisible(false);
                    bat.setMetadata(gadget.getIdentifier(), metadataValue);
                    this.module.getParent().queueEntity(bat);
                    bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
                    bat.setNoDamageTicks(Integer.MAX_VALUE);
                    Pig pig = p.getWorld().spawn(p.getLocation().add(0, 0.1, 0), Pig.class);
                    pig.setCustomName("UCEntity");
                    pig.setCustomNameVisible(false);
                    pig.setMetadata(gadget.getIdentifier(), metadataValue);
                    this.module.getParent().queueEntity(pig);
                    pig.setAdult();
                    pig.setAgeLock(true);
                    pig.setBreed(false);
                    pig.setSaddle(true);
                    pig.setNoDamageTicks(Integer.MAX_VALUE);
                    Methods.setPassenger(bat, pig);
                    Methods.setPassenger(pig, p);
                    break;

                case "BatBlaster":
                    final Set<LivingEntity> bats = new HashSet<>();

                    for (int i = 0; i < (int) additionalValues.get("Amount"); i++) {
                        LivingEntity b = p.getWorld().spawn(l, Bat.class);
                        b.setCustomName("UCEntity");
                        b.setCustomNameVisible(false);
                        b.setMetadata(gadget.getIdentifier(), metadataValue);
                        this.module.getParent().queueEntity(b);
                        b.setVelocity(l.getDirection().multiply((int) additionalValues.get("Speed")));
                        b.setNoDamageTicks((int) additionalValues.get("RemoveDelay") * 20);
                        bats.add(b);
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            for(LivingEntity b : bats) {
                                b.damage(b.getMaxHealth());
                                GadgetsListener.this.module.getParent().removeEntity(b);
                            }
                        }
                    }, (int) additionalValues.get("RemoveDelay") * 20L);
                    break;

                case "CATapult":
                    final List<Entity> cats = new ArrayList<>();

                    for (int i = 0; i < (int) additionalValues.get("Amount"); i++) {
                        Ocelot cat = p.getWorld().spawn(l, Ocelot.class);
                        cat.setCustomName("UCEntity");
                        cat.setCustomNameVisible(false);
                        cat.setMetadata(gadget.getIdentifier(), metadataValue);
                        this.module.getParent().queueEntity(cat);
                        cat.setTamed(true);
                        Vector v = l.getDirection().multiply((int) additionalValues.get("Speed"));
                        v.setX(Random.getDouble());
                        v.setY(Random.getDouble());
                        v.setZ(Random.getDouble());
                        cat.setVelocity(v);
                        cat.setNoDamageTicks(Integer.MAX_VALUE);
                        cats.add(cat);
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            for(Entity cat : cats) {
                                cat.getWorld().playEffect(cat.getLocation(), Effect.EXPLOSION_HUGE, 0);
                                cat.remove();
                                GadgetsListener.this.module.getParent().removeEntity(cat);
                            }
                        }
                    }, (int) additionalValues.get("ExplosionDelay") * 20L);
                    break;

                case "RailGun":
                    Vector vec = l.getDirection();
                    Location lastParticle = new Location(p.getWorld(), l.getX(), l.getY() - 0.3, l.getZ());
                    for (int i = 0; i < (int) additionalValues.get("Range") << 1; i++) {
                        if (lastParticle.getBlock().getType() != Material.AIR) return;
                        Methods.broadcastSafeParticle(lastParticle, Effect.FIREWORKS_SPARK, 0, 0, 0.0F, 1, 100);
                        lastParticle.add(vec.getX(), vec.getY(), vec.getZ());
                    }
                    break;

                case "CryoTube":
                    this.setFakeBlock(p.getLocation().getBlock(), Material.ICE, (byte) 0, (int) additionalValues.get("RemoveDelay"));
                    this.setFakeBlock(l.getBlock(), Material.ICE, (byte) 0, (int) additionalValues.get("RemoveDelay"));
                    Methods.broadcastSafeParticle(p.getLocation(), Effect.STEP_SOUND, 79, 79, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
                    Methods.broadcastSafeParticle(l, Effect.STEP_SOUND, 79, 79, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
                    break;

                case "Rocket":
                    new BukkitRunnable() {
                        private int counter = (int) additionalValues.get("Countdown");

                        @Override
                        public void run() {
                            if (this.counter > 0) {
                                if (ReflectionAPI.verBiggerThan(1, 8)) new JLibPlayer(p).sendTitle(5, 10, 5, ChatColor.RED + String.valueOf(this.counter));
                                else p.sendMessage(ChatColor.RED + String.valueOf(this.counter));
                                new JLibPlayer(p).playNote(Instrument.STICKS, Note.Tone.A);
                                this.counter--;
                            } else {
                                if (ReflectionAPI.verBiggerThan(1, 8)) new JLibPlayer(p).sendTitle(0, 10, 0, ChatColor.RED + "Lift-Off");
                                else p.sendMessage(ChatColor.RED + "Lift-Off");
                                Firework fw = p.getWorld().spawn(p.getLocation().add(0, 0.1, 0), Firework.class);
                                fw.setCustomName("UCEntity");
                                fw.setCustomNameVisible(false);
                                fw.setMetadata(gadget.getIdentifier(), metadataValue);
                                GadgetsListener.this.module.getParent().queueEntity(fw);
                                FireworkMeta fm = fw.getFireworkMeta();
                                FireworkEffect.Builder effect = FireworkEffect.builder();
                                effect.with(FireworkEffect.Type.BALL_LARGE);
                                effect.withColor(Color.fromRGB(Random.getInt(255), Random.getInt(255), Random.getInt(255)));
                                effect.withFade(Color.fromRGB(Random.getInt(255), Random.getInt(255), Random.getInt(255)));
                                effect.withTrail();
                                fm.addEffect(effect.build());
                                fm.setPower(4);
                                fw.setFireworkMeta(fm);
                                Methods.setPassenger(fw, p);
                                fw.setVelocity(new Vector(0, (int) additionalValues.get("Speed"), 0));
                                p.setFallDistance(-100.0F);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(this.module.getParent(), 0L, 20L);
                    break;

                case "PoopBomb":
                    final Set<Entity> items = new HashSet<>();

                    for (int a = 0; a < (int) additionalValues.get("Amount"); a++) {
                        int x = Random.getInt((int) additionalValues.get("Radius") << 1) - (int) additionalValues.get("Radius");
                        int z = Random.getInt((int) additionalValues.get("Radius") << 1) - (int) additionalValues.get("Radius");
                        Item i = l.getWorld().dropItemNaturally(l.add(x , 10, z), new JLibItem.Builder().withType(Material.INK_SACK).withAmount(1).withDurability((short) 3).withName(Random.getString(16, true, true)).build().getItemStack());
                        i.setCustomName("UCEntity");
                        i.setCustomNameVisible(false);
                        i.setMetadata(gadget.getIdentifier(), metadataValue);
                        this.module.getParent().queueEntity(i);
                        i.setPickupDelay(Integer.MAX_VALUE);
                        items.add(i);
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            for(Entity i : items) {
                                Methods.broadcastSafeParticle(i.getLocation(), Effect.SMOKE, 0, 0, 2.0F, 10, 100);
                                i.remove();
                                GadgetsListener.this.module.getParent().removeEntity(i);
                            }
                        }
                    }, (int) additionalValues.get("RemoveDelay") * 20L);
                    break;

                case "GrapplingHook":
                    if (this.fishingPlayers.containsKey(p)) this.fishingPlayers.get(p).remove();
                    Entity hook = p.launchProjectile(Fish.class);
                    hook.setCustomName("UCEntity");
                    hook.setCustomNameVisible(false);
                    hook.setMetadata(gadget.getIdentifier(), metadataValue);
                    this.module.getParent().queueEntity(hook);
                    this.fishingPlayers.put(p, hook);
                    break;

                case "SelfDestruct":
                    for (Entity near : p.getNearbyEntities((int) additionalValues.get("PushbackRange"), (int) additionalValues.get("PushbackRange"), (int) additionalValues.get("PushbackRange"))) {
                        near.setVelocity(near.getLocation().getDirection().multiply(-2));
                        Methods.broadcastSafeParticle(l, Effect.EXPLOSION_HUGE, 0, 0, 0.0F, 1, 100);
                        p.playEffect(EntityEffect.HURT);
                    }
                    break;

                case "SlimeVasion":
                    final Set<Entity> slimes = new HashSet<>();

                    for (int i = 0; i < (int) additionalValues.get("Amount"); i++) {
                        Slime slime = p.getWorld().spawn(l, Slime.class);
                        slime.setCustomName("UCEntity");
                        slime.setCustomNameVisible(false);
                        slime.setMetadata(gadget.getIdentifier(), metadataValue);
                        this.module.getParent().queueEntity(slime);
                        slime.setSize(Random.getInt(1, 4));
                        slime.setNoDamageTicks(Integer.MAX_VALUE);
                        slimes.add(slime);
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            for(Entity slime : slimes) {
                                slime.remove();
                                GadgetsListener.this.module.getParent().removeEntity(slime);
                            }
                        }
                    }, (int) additionalValues.get("RemoveDelay") * 20L);
                    break;

                case "FunGun":
                    for (int i = 0; i < 5; i++) {
                        Projectile s = p.launchProjectile(Snowball.class);
                        s.setCustomName("UCEntity");
                        s.setCustomNameVisible(false);
                        s.setMetadata(gadget.getIdentifier(), metadataValue);
                        this.module.getParent().queueEntity(s);
                        s.setShooter(p);
                    }
                    break;

                case "MelonThrower":
                case "ColorBomb":
                case "GoldFountain":
                    Item i = p.getWorld().dropItemNaturally(l, new JLibItem.Builder().withItemStack(gadget.getjLibItem().getItemStack()).withName(Random.getString(16, true, true)).build().getItemStack());
                    i.setCustomName("UCEntity");
                    i.setCustomNameVisible(false);
                    i.setMetadata(gadget.getIdentifier(), metadataValue);
                    this.module.getParent().queueEntity(i);
                    i.setVelocity(l.getDirection().multiply(1.5f));
                    i.setPickupDelay(Integer.MAX_VALUE);
                    break;

                case "FireTrail":
                    this.firePlayers.add(p);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) additionalValues.get("Duration") * 20, (int) additionalValues.get("SpeedMultiplier") - 1));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            GadgetsListener.this.firePlayers.remove(p);
                        }
                    }, (int) additionalValues.get("Duration") * 20L);
                    break;

                case "DiamondShower":
                    this.diamondShowerPlayers.add(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            GadgetsListener.this.diamondShowerPlayers.remove(p);
                        }
                    }, (int) additionalValues.get("Duration") * 20L);
                    break;

                case "PaintTrail":
                    this.paintTrailPlayers.add(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            GadgetsListener.this.paintTrailPlayers.remove(p);
                        }
                    }, (int) gadget.getAdditionalValues().get("Duration") * 20L);
                    break;
            }
            p.updateInventory();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (this.module.getConfig().isGadgetItem(e.getCurrentItem()) && this.module.getParent().getApi().hasCosmetics((Player) e.getWhoClicked(), Gadget.class)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (this.firePlayers.contains(p)) {
            Block b = p.getLocation().getBlock();
            if (b.getType() == Material.AIR) this.setFakeBlock(b, Material.FIRE, (byte) 0, (int) this.module.getConfig().getByIdentifier("FireTrail").getAdditionalValues().get("RemoveDelay"));
        }
        if (this.paintTrailPlayers.contains(p)) this.setRandomClay(p.getLocation().getBlock().getRelative(BlockFace.DOWN),  (int) this.module.getConfig().getByIdentifier("PaintTrail").getAdditionalValues().get("RestoreDelay"));
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (this.module.getConfig().isGadgetItem(e.getItemDrop().getItemStack()) && this.module.getParent().getApi().hasCosmetics(e.getPlayer(), Gadget.class)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata("MelonSlice")) {
            e.setCancelled(true);
            e.getItem().remove();
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) this.module.getConfig().getByIdentifier("MelonThrower").getAdditionalValues().get("Speed.Duration") * 20, (int) this.module.getConfig().getByIdentifier("MelonThrower").getAdditionalValues().get("Speed.Multiplier") - 1));
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
                for(Gadget cosmetic : this.module.getParent().getApi().getCosmetics(p, Gadget.class)) {
                    cosmetic.remove(this.module.getParent());
                }
                return;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION && p.isInsideVehicle()) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e) {
        Entity ent = e.getRemover();
        if(ent instanceof Player && this.module.getParent().getApi().hasCosmetics((Player) ent, Gadget.class)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Entity ent = e.getRightClicked();
        if((ent instanceof Painting || ent instanceof ItemFrame) && this.module.getParent().getApi().hasCosmetics(e.getPlayer(), Gadget.class)) {
            e.setCancelled(true);
            e.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("Enderbow") || e.getDamager().hasMetadata("SlimeVasion")) e.setCancelled(true);
        if (e.getDamager().hasMetadata("GrapplingHook")) {
            e.setCancelled(true);
            if ((boolean) this.module.getConfig().getByIdentifier("GrapplingHook").getAdditionalValues().get("PullPlayers") && e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                Player shooter = Bukkit.getPlayer(e.getDamager().getMetadata("GrapplingHook").get(0).asString());
                e.getDamager().remove();
                this.fishingPlayers.remove(p);
                this.pullEntityToLocation(p, shooter.getLocation());
            }
        }
    }


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
        if (ent.hasMetadata("PaintballGun")) {
            Location l = ent.getLocation();
            Sounds.broadcastSound(this.module.getConfig().getByIdentifier("PaintballGun").getSound(), l);
            Iterator<Block> iterator = new BlockIterator(l.getWorld(), l.toVector(), ent.getVelocity().normalize(), 0.0D, (int) this.module.getConfig().getByIdentifier("PaintballGun").getAdditionalValues().get("PaintSize"));
            while (iterator.hasNext()) {
                Block block = iterator.next();
                this.setRandomClay(block, (int) this.module.getConfig().getByIdentifier("PaintballGun").getAdditionalValues().get("RestoreDelay"));
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
            Sounds.broadcastSound(this.module.getConfig().getByIdentifier("FunGun").getSound(), l);
            Methods.broadcastSafeParticle(l, Effect.HEART, 0, 0, 0.0F, 0.2F, 0.0F, 0.0F, 1, 100);
            Methods.broadcastSafeParticle(l, Effect.LAVA_POP, 0, 0, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
        }
    }

    @EventHandler
    public void onInventoryPickup(InventoryPickupItemEvent e) {
        if (this.isEntity(e.getItem())) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        if (this.isEntity(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        if (this.isEntity(e.getEntity())) e.setCancelled(true);
    }

    private void setRandomClay(Block block, int delay) {
        if(PAINTBALL_DENIED.contains(block.getType())) return;
        Methods.broadcastSafeParticle(block.getLocation(), Effect.MAGIC_CRIT, 0, 0, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
        this.setFakeBlock(block, Material.STAINED_CLAY, (byte) Random.getInt(15), delay);
    }

    private void pullEntityToLocation(Entity p, Location l) {
        Location el = p.getLocation().add(0, 0.5, 0);
        p.teleport(el);
        p.setVelocity(new Vector(0.25 * (l.getX() - el.getX()), 0.20 * (l.getY() - el.getY()) + 0.05, 0.25 * (l.getZ() - el.getZ())));
        p.setFallDistance(-100.0F);
    }

    private void handleMelon(Entity ent) {
        Location l = ent.getLocation();
        this.module.getParent().removeEntity(ent);
        ent.remove();
        Methods.broadcastSafeParticle(l, Effect.STEP_SOUND, 103, 103, 0.7F, 0.7F, 0.7F, 0.0F, 10, 100);
        final Set<Item> items = new HashSet<>();
        for (int a = 0; a < (int) this.module.getConfig().getByIdentifier("MelonThrower").getAdditionalValues().get("Amount"); a++) {
            Item i = l.getWorld().dropItemNaturally(l, new JLibItem.Builder().withType(Material.MELON).withAmount(1).withName(Random.getString(16, true, true)).build().getItemStack());
            i.setCustomName("UCEntity");
            i.setCustomNameVisible(false);
            i.setMetadata("MelonSlice", new FixedMetadataValue(this.module.getParent(), null));
            this.module.getParent().queueEntity(i);
            items.add(i);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
            @Override
            public void run() {
                for(Item i : items) {
                    i.remove();
                    GadgetsListener.this.module.getParent().removeEntity(i);
                }
            }
        }, (int) this.module.getConfig().getByIdentifier("MelonThrower").getAdditionalValues().get("RemoveDelay") * 20L);
    }

    private void handleColorBomb(Entity ent) {
        final Location l = ent.getLocation();
        this.module.getParent().removeEntity(ent);
        ent.remove();
        new BukkitRunnable() {
            private int count;

            @Override
            public void run() {
                this.count++;
                if (this.count > (int) GadgetsListener.this.module.getConfig().getByIdentifier("ColorBomb").getAdditionalValues().get("RemoveDelay") * 20) this.cancel();
                else {
                    Sounds.broadcastSound(GadgetsListener.this.module.getConfig().getByIdentifier("ColorBomb").getSound(), l);
                    List<String> items = (List<String>) GadgetsListener.this.module.getConfig().getByIdentifier("ColorBomb").getAdditionalValues().get("Items");
                    String item = items.get(Random.getInt(items.size() - 1));
                    final Item i = l.getWorld().dropItemNaturally(l, new JLibItem.Builder().withType(Parsing.parseMaterial(item)).withAmount(1).withDurability((short) Parsing.parseData(item)).withName(Random.getString(16, true, true)).build().getItemStack());
                    i.setCustomName("UCEntity");
                    i.setCustomNameVisible(false);
                    i.setMetadata("ColorBomb", new FixedMetadataValue(GadgetsListener.this.module.getParent(), null));
                    GadgetsListener.this.module.getParent().queueEntity(i);
                    i.setVelocity(i.getVelocity().setY(0.75));
                    i.setPickupDelay(Integer.MAX_VALUE);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(GadgetsListener.this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            i.remove();
                            GadgetsListener.this.module.getParent().removeEntity(i);
                        }
                    }, 5L);
                }
            }
        }.runTaskTimer(this.module.getParent(), 0L, 1L);
    }

    private void handleGoldFountain(Entity ent) {
        final Location l = ent.getLocation();
        this.module.getParent().removeEntity(ent);
        ent.remove();
        new BukkitRunnable() {
            private int count;

            @Override
            public void run() {
                this.count++;
                if (this.count > (int) GadgetsListener.this.module.getConfig().getByIdentifier("GoldFountain").getAdditionalValues().get("RemoveDelay") * 20) this.cancel();
                else {
                    Sounds.broadcastSound(GadgetsListener.this.module.getConfig().getByIdentifier("GoldFountain").getSound(), l);
                    final Item i = l.getWorld().dropItemNaturally(l, new JLibItem.Builder().withType(Material.GOLD_INGOT).withAmount(1).withName(Random.getString(16, true, true)).build().getItemStack());
                    i.setCustomName("UCEntity");
                    i.setCustomNameVisible(false);
                    i.setMetadata("GoldFountain", new FixedMetadataValue(GadgetsListener.this.module.getParent(), null));
                    GadgetsListener.this.module.getParent().queueEntity(i);
                    i.setVelocity(i.getVelocity().setY(0.75));
                    i.setPickupDelay(Integer.MAX_VALUE);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(GadgetsListener.this.module.getParent(), new Runnable() {
                        @Override
                        public void run() {
                            i.remove();
                            GadgetsListener.this.module.getParent().removeEntity(i);
                        }
                    }, 5L);
                }
            }
        }.runTaskTimer(this.module.getParent(), 0L, 1L);
    }

    private boolean isEntity(Entity ent) {
        if (ent.getCustomName() != null && ent.getCustomName().endsWith("UCEntity")) return true;
        for (Entity e : this.module.getParent().getEntitiesQueue()) {
            if (ent.equals(e)) return true;
        }
        return false;
    }


    private void setFakeBlock(final Block block, Material type, byte data, int secDelay) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendBlockChange(block.getLocation(), type, data);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
            @Override
            public void run() {
                block.getState().update();
            }
        }, secDelay * 20L);
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
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.module.getParent(), new Runnable() {
            @Override
            public void run() {
                GadgetsListener.this.cooldownPlayers.remove(p);
            }
        }, storage.getCooldown() * 20L);
    }

    private boolean checkAmmo(Player p, GadgetStorage gadget) {
        DataLoader dataLoader = this.module.getParent().getDataLoader();
        Lang lang = this.module.getParent().getLang();
        if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
            int i = dataLoader.getAmmoCache().get(p).get(gadget.getIdentifier());
            if (i <= 0) {
                Methods.informPlayerNoPermission(p, Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                return true;
            } else {
                dataLoader.takeAmmo(gadget.getIdentifier(), p, 1);
                if(!lang.getLostAmmo().isEmpty()) p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
            }
        }
        return false;
    }
}
