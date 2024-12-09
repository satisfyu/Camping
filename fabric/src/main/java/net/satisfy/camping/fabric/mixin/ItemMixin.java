package net.satisfy.camping.fabric.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.fabric.config.ConfigFabric;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public class ItemMixin {
    @Redirect(method = "isFoil(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEnchanted()Z"))
    private boolean redirectIsEnchanted(ItemStack itemStack) {
        ConfigFabric config = AutoConfig.getConfigHolder(ConfigFabric.class).getConfig();
        return (itemStack.isEnchanted() || CampingUtil.Grilling.isGrilled(itemStack)) && config.enableGlint;
    }
}
