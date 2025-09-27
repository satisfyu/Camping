package net.satisfy.camping.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.config.ForgeCampingConfig;
import net.satisfy.camping.core.util.GrillingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ForgeItemMixin {

//    @Redirect(method = "isFoil(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEnchanted()Z"))
//    private boolean redirectIsEnchanted(ItemStack itemStack) {
//        return (itemStack.isEnchanted() || CampingUtil.Grilling.isGrilled(itemStack)) && ForgeCampingConfig.enableGlint;
//    }

    @Inject(method = "isFoil(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "RETURN"), cancellable = true)
    private void camping$isFoilInject(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {
        if (ForgeCampingConfig.enableGlint && GrillingUtil.isGrilled(pStack)) cir.setReturnValue(true);
    }
}
