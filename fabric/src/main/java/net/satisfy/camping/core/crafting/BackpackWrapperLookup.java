package net.satisfy.camping.core.crafting;

import com.google.common.collect.MapMaker;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.util.LazyOptional;
import net.satisfy.camping.core.world.item.BackpackItem;

import java.util.Map;

public class BackpackWrapperLookup {

    public static void init() {}

    public static final ItemApiLookup<LazyOptional<IBackpackWrapper>, Void> ITEM = ItemApiLookup.get(Camping.identifier("item_backpack_wrapper"), (Class<LazyOptional<IBackpackWrapper>>) (Class<?>) LazyOptional.class, Void.class);

    public static LazyOptional<IBackpackWrapper> get(ItemStack provider) {
        LazyOptional<IBackpackWrapper> wrapper = ITEM.find(provider, null);
        if (wrapper != null) {
            return wrapper;
        }
        return LazyOptional.empty();
    }

    public static ItemApiLookup.ItemApiProvider<LazyOptional<IBackpackWrapper>, Void> initCapabilities() {
        return new ItemApiLookup.ItemApiProvider<>() {
            final Map<ItemStack, IBackpackWrapper> wrapperMap = new MapMaker().weakKeys().weakValues().makeMap();

            @Override
            public LazyOptional<IBackpackWrapper> find(ItemStack stack, Void context) {
                if (stack.getCount() == 1) {
                    return LazyOptional.of(() -> wrapperMap.computeIfAbsent(stack, this::initWrapper)).cast();
                }

                return LazyOptional.empty();
            }

            private IBackpackWrapper initWrapper(ItemStack stack) {
                return new BackpackWrapper(stack);
            }
        };
    }

    static {
        ITEM.registerForItems(initCapabilities(), CampingItems.BACKPACKS);
    }
}
