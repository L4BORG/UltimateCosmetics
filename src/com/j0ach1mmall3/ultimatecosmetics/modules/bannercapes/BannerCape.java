package com.j0ach1mmall3.ultimatecosmetics.modules.bannercapes;

import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class BannerCape extends Cosmetic {
    private ArmorStand armorStand;

    public BannerCape(CosmeticConfig cosmeticConfig, Player player, CosmeticStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.BANNERCAPE);
    }

    @Override
    protected boolean giveInternal() {
        this.armorStand = this.player.getWorld().spawn(this.player.getEyeLocation().subtract(0, 1, 0), ArmorStand.class);
        this.armorStand.setVisible(false);
        this.armorStand.setHeadPose(new EulerAngle(Math.toRadians(((BannerCapes) this.cosmeticConfig).getAngle()), 0, 0));
        this.armorStand.setBodyPose(new EulerAngle(Math.toRadians(180), 0, 0));
        this.armorStand.setLeftLegPose(new EulerAngle(Math.toRadians(180), 0, 0));
        this.armorStand.setRightLegPose(new EulerAngle(Math.toRadians(180), 0, 0));
        this.armorStand.setMarker(false);
        this.armorStand.setGravity(false);
        this.armorStand.setArms(false);
        this.armorStand.setBasePlate(false);
        return true;
    }

    @Override
    protected void removeInternal() {
        this.armorStand.remove();
    }

    public ArmorStand getArmorStand() {
        return this.armorStand;
    }
}