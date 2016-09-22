package com.j0ach1mmall3.ultimatecosmetics.modules.hearts;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Heart extends Cosmetic<HeartStorage> {
    private PotionEffect colorEffect;
    private PotionEffect effectsEffect;

    public Heart(Player player, HeartStorage cosmeticStorage) {
        super(player, cosmeticStorage);
    }

    @Override
    protected boolean giveInternal(Main plugin) {
        HeartStorage heartStorage = this.cosmeticStorage;
        int rows = heartStorage.getRows();
        HeartStorage.Color color = heartStorage.getColor();
        HeartStorage.Effect effect = heartStorage.getEffect();
        this.player.setMaxHealth(rows * 20);
        if (color == HeartStorage.Color.YELLOW) {
            this.player.setMaxHealth(1);
            this.colorEffect = new PotionEffect(color.getPotionEffect(), Integer.MAX_VALUE, rows * 5);
        }
        if (color != HeartStorage.Color.RED && color != HeartStorage.Color.YELLOW) this.colorEffect = new PotionEffect(color.getPotionEffect(), Integer.MAX_VALUE, 0);
        this.effectsEffect = effect == HeartStorage.Effect.BOUNCING ? new PotionEffect(effect.getPotionEffect(), Integer.MAX_VALUE, 0) : null;
        if(this.colorEffect != null) this.player.addPotionEffect(this.colorEffect);
        if(this.effectsEffect != null) this.player.addPotionEffect(this.effectsEffect);
        this.player.damage(color == HeartStorage.Color.YELLOW ? 5 : 0);
        return true;
    }

    @Override
    protected void removeInternal(Main plugin) {
        HeartStorage heartStorage = this.cosmeticStorage;
        int rows = heartStorage.getRows();
        HeartStorage.Color color = heartStorage.getColor();
        HeartStorage.Effect effect = heartStorage.getEffect();
        if (rows > 1 || color == HeartStorage.Color.YELLOW) this.player.setMaxHealth(20.0);
        if (color != HeartStorage.Color.RED && this.colorEffect != null) this.player.removePotionEffect(this.colorEffect.getType());
        if (effect != HeartStorage.Effect.NONE && this.effectsEffect != null) this.player.removePotionEffect(this.effectsEffect.getType());
    }

    public PotionEffect getColorEffect() {
        return this.colorEffect;
    }

    public PotionEffect getEffectsEffect() {
        return this.effectsEffect;
    }
}
