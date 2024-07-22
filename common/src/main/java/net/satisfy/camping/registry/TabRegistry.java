package net.satisfy.camping.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.satisfy.camping.Camping;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CAMPING_TABS = DeferredRegister.create(Camping.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> CAMPING_TAB = CAMPING_TABS.register("camping", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.LARGE_BACKPACK.get()))
            .title(Component.translatable("itemGroup.camping.camping_tab"))
            .displayItems((parameters, out) -> {
                String[] colorOrder = {
                        "white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown"
                };
                out.accept(Items.BUNDLE);
                out.accept(ObjectRegistry.SMALL_BACKPACK_ITEM.get());
                out.accept(ObjectRegistry.LARGE_BACKPACK_ITEM.get());
                out.accept(ObjectRegistry.WANDERER_BACKPACK_ITEM.get());
                out.accept(ObjectRegistry.WANDERER_BAG_ITEM.get());
                out.accept(ObjectRegistry.ENDERPACK_ITEM.get());
                out.accept(ObjectRegistry.ENDERBAG_ITEM.get());
                out.accept(ObjectRegistry.MULTITOOL.get());
                out.accept(ObjectRegistry.GRILL.get());
                for (String color : colorOrder) {
                    ObjectRegistry.TENT_MAIN.get(color).ifPresent(out::accept);
                }
                for (String color : colorOrder) {
                    ObjectRegistry.SLEEPING_BAGS.get(color).ifPresent(out::accept);
                }

            })
            .build());

    static {
        CAMPING_TABS.register();
    }
}
