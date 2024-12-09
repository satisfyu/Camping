package net.satisfy.camping.fabric.compat.trinkets;

import dev.emi.trinkets.api.TrinketsApi;
import net.satisfy.camping.core.registry.ObjectRegistry;

public class TrinketsCompatibility {

    public static void load() {

        TrinketsApi.registerTrinket(ObjectRegistry.SMALL_BACKPACK_ITEM.get(), new BackpackTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.LARGE_BACKPACK_ITEM.get(), new BackpackTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.WANDERER_BACKPACK_ITEM.get(), new BackpackTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.WANDERER_BAG_ITEM.get(), new BackpackTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.SHEEPBAG_ITEM.get(), new BackpackTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.GOODYBAG_ITEM.get(), new BackpackTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.ENDERBAG_ITEM.get(), new BackpackTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.ENDERPACK_ITEM.get(), new BackpackTrinket());
    }
}
