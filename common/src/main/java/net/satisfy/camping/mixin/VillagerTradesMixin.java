package net.satisfy.camping.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.satisfy.camping.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerTrades.class)
public class VillagerTradesMixin {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void addCustomTrades(CallbackInfo ci) {
        Int2ObjectMap<ItemListing[]> shepherdTrades = VillagerTrades.TRADES.get(VillagerProfession.SHEPHERD);
        if (shepherdTrades != null) {
            ItemListing[] level1 = shepherdTrades.get(1);
            ItemListing[] newLevel1 = new ItemListing[level1.length + 1];
            System.arraycopy(level1, 0, newLevel1, 0, level1.length);
            newLevel1[level1.length] = new VillagerTrades.ItemsForEmeralds(ObjectRegistry.SHEEPBAG_ITEM.get(), 37, 1, 1, 15);
            shepherdTrades.put(1, newLevel1);
        }
    }
}