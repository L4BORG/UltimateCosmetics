package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.ultimatecosmetics.Reflection;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class BlockPet extends Cosmetic<BlockPets, CosmeticStorage> {
    private Creature creature;
    private FallingBlock block;

    public BlockPet(BlockPets cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected boolean giveInternal() {
        Location l = this.player.getLocation();
        this.creature = (Creature) this.player.getWorld().spawnEntity(l, EntityType.OCELOT);
        Reflection.removeGoalSelectors(this.creature);
        Reflection.addGoalSelectors(this.creature);
        this.creature.teleport(this.player);
        this.creature.setCanPickupItems(false);
        this.creature.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 100));
        this.block = this.player.getWorld().spawnFallingBlock(l, this.cosmeticStorage.getGuiItem().getItem().getType(), (byte) this.cosmeticStorage.getGuiItem().getItem().getDurability());
        this.block.setDropItem(false);
        this.block.setCustomName(Placeholders.parse(this.cosmeticStorage.getGuiItem().getItem().getItemMeta().getDisplayName(), this.player));
        this.block.setCustomNameVisible(true);
        this.creature.setPassenger(this.block);
        this.creature.setRemoveWhenFarAway(false);
        this.creature.setNoDamageTicks(Integer.MAX_VALUE);
        this.creature.setMaxHealth(20.0D);
        this.creature.setHealth(this.creature.getMaxHealth());
        this.creature.setMetadata("BlockPet", new FixedMetadataValue(this.cosmeticStorage.getPlugin(), this.player.getName()));
        this.creature.setTarget(this.player);
        this.creature.setCustomNameVisible(true);
        this.cosmeticStorage.getPlugin().getDataLoader().getPetName(this.player, new CallbackHandler<String>() {
            @Override
            public void callback(final String o) {
                if(o == null || o.isEmpty()) return;
                Bukkit.getScheduler().callSyncMethod(BlockPet.this.cosmeticStorage.getPlugin(), new Callable<Void>() {
                    @Override
                    public Void call() {
                        BlockPet.this.block.setCustomName(Placeholders.parse(o, BlockPet.this.player));
                        return null;
                    }
                });
            }
        });
        return true;
    }

    @Override
    protected void removeInternal() {
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
