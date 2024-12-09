package net.satisfy.camping.core.mixin;

import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.satisfy.camping.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WanderingTrader.class)
public class WanderingTraderMixin {

    @Inject(method = "updateTrades", at = @At("RETURN"))
    private void addCustomTrade(CallbackInfo ci) {
        WanderingTrader wanderingTrader = (WanderingTrader) (Object) this;
        MerchantOffers offers = wanderingTrader.getOffers();
        Random random = new Random();

        if (random.nextInt(5) == 0) {
            int emeraldCost = 27 + random.nextInt(27);
            MerchantOffer customOffer = new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(ObjectRegistry.WANDERER_BACKPACK_ITEM.get(), 1), 6, 2, 0.05F);
            offers.add(customOffer);
        }
    }
}
