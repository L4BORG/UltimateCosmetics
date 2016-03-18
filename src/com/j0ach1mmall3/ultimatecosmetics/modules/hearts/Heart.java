package com.j0ach1mmall3.ultimatecosmetics.modules.hearts;

import com.j0ach1mmall3.ultimatecosmetics.api.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticType;
import com.j0ach1mmall3.ultimatecosmetics.config.CosmeticConfig;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 9/03/2016
 */
public final class Heart extends Cosmetic {
    private static final Map<Integer, Integer> AMPLIFIERS = new HashMap<>();
    
    static {
        AMPLIFIERS.put(1, 4);
        AMPLIFIERS.put(2, 9);
        AMPLIFIERS.put(3, 14);
        AMPLIFIERS.put(4, 19);
    }
    
    private PotionEffect colorEffect;
    private PotionEffect effectsEffect;

    public Heart(CosmeticConfig cosmeticConfig, Player player, HeartStorage cosmeticStorage) {
        super(cosmeticConfig, player, cosmeticStorage, CosmeticType.HEART);
    }

    @Override
    protected boolean giveInternal() {
        HeartStorage heartStorage = (HeartStorage) this.cosmeticStorage;
        int rows = heartStorage.getRows();
        HeartStorage.Color color = heartStorage.getColor();
        HeartStorage.Effect effect = heartStorage.getEffect();
        this.player.setMaxHealth(rows * 20);
        this.player.setHealth(this.player.getMaxHealth());
        if (color == HeartStorage.Color.YELLOW) {
            this.player.setMaxHealth(0.5);
            this.colorEffect = new PotionEffect(color.getPotionEffect(), Integer.MAX_VALUE, AMPLIFIERS.get(rows));
        }
        if (color != HeartStorage.Color.RED) this.colorEffect = new PotionEffect(color.getPotionEffect(), Integer.MAX_VALUE, 0);
        this.effectsEffect = effect == HeartStorage.Effect.BOUNCING ? new PotionEffect(effect.getPotionEffect(), Integer.MAX_VALUE, 0) : null;
        if(this.colorEffect != null) this.player.addPotionEffect(this.colorEffect);
        if(this.effectsEffect != null) this.player.addPotionEffect(this.effectsEffect);
        this.player.damage(color == HeartStorage.Color.YELLOW ? 1.5 : 0);
        return true;
    }

    @Override
    protected void removeInternal() {
        HeartStorage heartStorage = (HeartStorage) this.cosmeticStorage;
        int rows = heartStorage.getRows();
        HeartStorage.Color color = heartStorage.getColor();
        HeartStorage.Effect effect = heartStorage.getEffect();
        if (rows > 1 || color == HeartStorage.Color.YELLOW) {
            this.player.setMaxHealth(20.0);
            this.player.setHealth(20.0);
        }
        if (color != HeartStorage.Color.RED && this.colorEffect != null) this.player.removePotionEffect(this.colorEffect.getType());
        if (effect != HeartStorage.Effect.NONE && this.effectsEffect != null) this.player.removePotionEffect(this.effectsEffect.getType());
    }

    public void regive() {
        this.removeInternal();
        this.giveInternal();
    }

    public PotionEffect getColorEffect() {
        return this.colorEffect;
    }

    public PotionEffect getEffectsEffect() {
        return this.effectsEffect;
    }
}
