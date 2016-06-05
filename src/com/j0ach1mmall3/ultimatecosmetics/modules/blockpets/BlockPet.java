package com.j0ach1mmall3.ultimatecosmetics.modules.blockpets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.ultimatecosmetics.Reflection;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
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
public final class BlockPet extends Cosmetic<BlockPets, CosmeticStorage> {
    private Ocelot ocelot;
    private FallingBlock block;

    public BlockPet(BlockPets cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected boolean giveInternal() {
        Location l = this.player.getLocation();
        this.ocelot = (Ocelot) this.player.getWorld().spawnEntity(l, EntityType.OCELOT);
        Reflection.removeGoalSelectors(this.ocelot);
        Reflection.addGoalSelectors(this.ocelot);
        this.ocelot.teleport(this.player);
        this.ocelot.setCanPickupItems(false);
        this.ocelot.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 100));
        this.block = this.player.getWorld().spawnFallingBlock(l, this.cosmeticStorage.getGuiItem().getItem().getType(), (byte) this.cosmeticStorage.getGuiItem().getItem().getDurability());
        this.block.setDropItem(false);
        this.block.setCustomName(Placeholders.parse(this.cosmeticStorage.getGuiItem().getItem().getItemMeta().getDisplayName(), this.player));
        this.block.setCustomNameVisible(true);
        this.ocelot.setPassenger(this.block);
        this.ocelot.setRemoveWhenFarAway(false);
        this.ocelot.setNoDamageTicks(Integer.MAX_VALUE);
        this.ocelot.setMaxHealth(20.0D);
        this.ocelot.setHealth(this.ocelot.getMaxHealth());
        this.ocelot.setMetadata("BlockPet", new FixedMetadataValue(this.cosmeticStorage.getPlugin(), this.player.getName()));
        this.ocelot.setTarget(this.player);
        this.ocelot.setCustomNameVisible(true);
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
        this.ocelot.remove();
    }

    public Ocelot getOcelot() {
        return this.ocelot;
    }

    public FallingBlock getBlock() {
        return this.block;
    }
}
