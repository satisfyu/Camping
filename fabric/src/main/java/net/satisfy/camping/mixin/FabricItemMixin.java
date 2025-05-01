package net.satisfy.camping.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.config.FabricCampingConfig;
import net.satisfy.camping.core.util.CampingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class FabricItemMixin {
//    @Redirect(method = "isFoil(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEnchanted()Z"))
//    private boolean redirectIsEnchanted(ItemStack itemStack) {
//        FabricCampingConfig config = AutoConfig.getConfigHolder(FabricCampingConfig.class).getConfig();
//        return (itemStack.isEnchanted() || CampingUtil.Grilling.isGrilled(itemStack)) && config.enableGlint;
//    }

    @Inject(method = "isFoil(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "RETURN"), cancellable = true)
    private void camping$isFoilInject(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {
        FabricCampingConfig config = AutoConfig.getConfigHolder(FabricCampingConfig.class).getConfig();
        if (config.enableGlint && CampingUtil.Grilling.isGrilled(pStack)) cir.setReturnValue(true);
    }
}
