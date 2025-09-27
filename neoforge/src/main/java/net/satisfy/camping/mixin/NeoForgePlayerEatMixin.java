package net.satisfy.camping.mixin;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.camping.core.config.NeoForgeCampingConfig;
import net.satisfy.camping.core.util.GrillingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class NeoForgePlayerEatMixin {

    @Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void camping$eat(Level level, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!(self instanceof Player player)) return;
        if (!NeoForgeCampingConfig.enableGrilling || !GrillingUtil.isGrilled(stack)) return;
        FoodProperties props = stack.getFoodProperties(player);
        if (props == null) return;

        GrillingUtil.FoodValue extra = GrillingUtil.getAdditionalFoodValue(stack);
        int nutrition = Math.round(props.nutrition() * 1.25f) + extra.nutrition();
        float saturation = props.saturation() * 1.25f + extra.saturationModifier();

        FoodData food = player.getFoodData();
        food.eat(nutrition, saturation);

        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        level.gameEvent(player, GameEvent.EAT, player.blockPosition());

        cir.setReturnValue(stack);
    }
}
