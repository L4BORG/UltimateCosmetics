package com.j0ach1mmall3.ultimatecosmetics.modules.balloons;

import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Balloon extends Cosmetic {
    private Bat bat;
    private FallingBlock block;

    public Balloon(CosmeticConfig cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.BALLOON);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected boolean giveInternal() {
        Location l = this.player.getLocation();
        l.setY(l.getY() + 3.0);
        this.bat = (Bat) this.player.getWorld().spawnEntity(l, EntityType.BAT);
        this.bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 100));
        this.block = this.player.getWorld().spawnFallingBlock(l, this.cosmeticStorage.getGuiItem().getItem().getType(), (byte) this.cosmeticStorage.getGuiItem().getItem().getDurability());
        this.block.setDropItem(false);
        this.bat.setLeashHolder(this.player);
        this.bat.setPassenger(this.block);
        this.bat.setRemoveWhenFarAway(false);
        this.bat.setNoDamageTicks(Integer.MAX_VALUE);
        this.bat.setMetadata("Balloon", new FixedMetadataValue(this.cosmeticStorage.getPlugin(), this.player.getName()));
        return true;
    }

    @Override
    protected void removeInternal() {
        this.bat.setLeashHolder(null);
        this.player.setLeashHolder(null);
        this.block.remove();
        this.bat.remove();
    }

    public Bat getBat() {
        return this.bat;
    }

    public FallingBlock getBlock() {
        return this.block;
    }
}
