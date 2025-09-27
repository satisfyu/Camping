package net.satisfy.camping.core.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class CampingFoods {
    public static final FoodProperties MARSHMALLOW = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1F)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 0), 0.5f)
            .build();

    public static final FoodProperties ROASTED_MARSHMALLOW = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(0.6F)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1), 1.0f)
            .build();
}
