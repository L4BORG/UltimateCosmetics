package com.j0ach1mmall3.ultimatecosmetics.modules.balloons;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.Methods;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Balloon extends Cosmetic<CosmeticStorage> {
    private Bat bat;
    private FallingBlock block;

    public Balloon(Player player, CosmeticStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(Main plugin) {
        Location l = this.player.getLocation();
        l.setY(l.getY() + 3.0);
        this.bat = this.player.getWorld().spawn(l, Bat.class);
        this.bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 100));
        this.block = this.player.getWorld().spawnFallingBlock(l, this.cosmeticStorage.getjLibItem().getItemStack().getType(), (byte) this.cosmeticStorage.getjLibItem().getItemStack().getDurability());
        this.block.setDropItem(false);
        this.bat.setLeashHolder(this.player);
        Methods.setPassenger(this.bat, this.block);
        this.bat.setRemoveWhenFarAway(false);
        this.bat.setNoDamageTicks(Integer.MAX_VALUE);
        this.bat.setMetadata("Balloon", new FixedMetadataValue(plugin, this.player.getName()));
        plugin.queueEntity(this.bat);
        plugin.queueEntity(this.block);
        return true;
    }

    @Override
    protected void removeInternal(Main plugin) {
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
