package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.Reflection;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class BlockPet extends Cosmetic<CosmeticStorage> {
    private Creature creature;
    private FallingBlock block;

    public BlockPet(Player player, CosmeticStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(final Main plugin) {
        Location l = this.player.getLocation().add(0, 0.1, 0);
        this.creature = l.getWorld().spawn(l, Ocelot.class);
        Reflection.removeGoalSelectors(this.creature);
        Reflection.addGoalSelectors(this.creature);
        this.creature.setCanPickupItems(false);
        this.creature.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 100));
        this.creature.setRemoveWhenFarAway(false);
        this.creature.setNoDamageTicks(Integer.MAX_VALUE);
        this.creature.setMaxHealth(20.0D);
        this.creature.setHealth(this.creature.getMaxHealth());
        this.creature.setMetadata("BlockPet", new FixedMetadataValue(plugin, this.player.getName()));
        this.creature.setTarget(this.player);
        this.creature.setCustomNameVisible(true);
        this.block = this.player.getWorld().spawnFallingBlock(l, this.cosmeticStorage.getjLibItem().getItemStack().getType(), (byte) this.cosmeticStorage.getjLibItem().getItemStack().getDurability());
        this.block.setDropItem(false);
        this.block.setCustomName(Placeholders.parse(this.cosmeticStorage.getjLibItem().getItemStack().getItemMeta().getDisplayName(), this.player));
        this.block.setCustomNameVisible(true);
        Methods.setPassenger(this.creature, this.block);
        plugin.getDataLoader().getPetName(this.player, new CallbackHandler<String>() {
            @Override
            public void callback(final String o) {
                if(o == null || o.isEmpty()) return;
                Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Void>() {
                    @Override
                    public Void call() {
                        BlockPet.this.block.setCustomName(Placeholders.parse(o, BlockPet.this.player));
                        return null;
                    }
                });
            }
        });
        plugin.queueEntity(this.creature);
        plugin.queueEntity(this.block);
        return true;
    }

    @Override
    protected void removeInternal(Main plugin) {
        this.block.remove();
        this.creature.remove();
    }

    public Creature getCreature() {
        return this.creature;
    }

    public FallingBlock getBlock() {
        return this.block;
    }
}
