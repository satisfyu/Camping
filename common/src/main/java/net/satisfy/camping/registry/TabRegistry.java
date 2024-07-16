package net.satisfy.camping.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.satisfy.camping.Camping;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CAMPING_TABS = DeferredRegister.create(Camping.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> CAMPING_TAB = CAMPING_TABS.register("camping", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(Blocks.ACACIA_LEAVES))
            .title(Component.translatable("itemGroup.camping.camping_tab"))
            .displayItems((parameters, out) -> {
                String[] colorOrder = {
                        "white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown"
                };
                for (String color : colorOrder) {
                    ObjectRegistry.SLEEPING_BAGS.get(color).ifPresent(out::accept);
                }
                out.accept(ObjectRegistry.MULTITOOL.get());
                out.accept(ObjectRegistry.GRILL.get());
            })
            .build());

    static {
        CAMPING_TABS.register();
    }
}
