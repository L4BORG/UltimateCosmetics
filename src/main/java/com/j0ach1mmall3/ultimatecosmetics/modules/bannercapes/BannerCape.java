package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class BannerCape extends Cosmetic<CosmeticStorage> {
    private ArmorStand armorStand;

    public BannerCape(Player player, CosmeticStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(Main plugin) {
        this.armorStand = this.player.getWorld().spawn(this.player.getEyeLocation().subtract(0, 1, 0), ArmorStand.class);
        this.armorStand.setVisible(false);
        this.armorStand.setHeadPose(new EulerAngle(Math.toRadians(plugin.getConfig(BannerCapes.class).getAngle()), 0, 0));
        this.armorStand.setBodyPose(new EulerAngle(Math.toRadians(180), 0, 0));
        this.armorStand.setLeftLegPose(new EulerAngle(Math.toRadians(180), 0, 0));
        this.armorStand.setRightLegPose(new EulerAngle(Math.toRadians(180), 0, 0));
        this.armorStand.setMarker(false);
        this.armorStand.setGravity(false);
        this.armorStand.setArms(false);
        this.armorStand.setBasePlate(false);
        this.armorStand.setMetadata("BannerCape",  new FixedMetadataValue(plugin, this.player.getName()));
        return true;
    }

    @Override
    protected void removeInternal(Main plugin) {
        this.armorStand.remove();
    }

    public ArmorStand getArmorStand() {
        return this.armorStand;
    }
}