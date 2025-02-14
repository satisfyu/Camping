package net.satisfy.camping.compat.trinkets;

import dev.emi.trinkets.api.TrinketsApi;
import net.satisfy.camping.core.registry.CampingItems;

public class TrinketsCompatibility {

    public static void load() {

        TrinketsApi.registerTrinket(CampingItems.SMALL_BACKPACK, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.LARGE_BACKPACK, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.WANDERER_BACKPACK, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.WANDERER_BAG, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.SHEEPBAG, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.GOODYBAG, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.ENDERBAG, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.ENDERPACK, new BackpackTrinket());
    }
}
