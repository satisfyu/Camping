package net.satisfy.camping.core.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class CampingFoods {

    public static final FoodProperties MARSHMALLOW = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.1F)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 0), 0.5f) // second parameter is chance to apply within 0.0 to 1.0
            .build();

    public static final FoodProperties ROASTED_MARSHMALLOW = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.6F)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 0), 0.5f) // mobEffect, duration, multiplier // second parameter is chance to apply within 0.0 to 1.0
            .build();
}
