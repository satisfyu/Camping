package net.satisfy.camping.fabric.mixin;

import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.util.CampingUtil;
import net.satisfy.camping.fabric.config.ConfigFabric;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {

    @Shadow
    public void eat(int foodLevel, float saturationModifier) {}

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"), cancellable = true)
    private void onEat(Item item, ItemStack itemStack, CallbackInfo ci) {
        if (!CampingUtil.Grilling.isGrilled(itemStack) || !ConfigFabric.enableGrilling) {
            return;
        }

        FoodProperties foodProperties = itemStack.getItem().getFoodProperties();
        CampingUtil.Grilling.FoodValue additionalFoodValues = CampingUtil.Grilling.getAdditionalFoodValue(itemStack);

        if (foodProperties != null) {
            int nutrition = (int) (foodProperties.getNutrition() * 1.25) + additionalFoodValues.nutrition();
            float saturation = foodProperties.getSaturationModifier() * 1.25F + additionalFoodValues.saturationModifier();
            eat(nutrition, saturation);
        }

        ci.cancel();
    }
}
