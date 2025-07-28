package net.satisfy.camping.neoforge.core.crafting;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.satisfy.camping.Constants;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.item.BackpackItem;

/**
 * @author wdog5
 * For new capability system
 * since neoforge reworked the capability system
 * @see <a href="https://neoforged.net/news/20.3capability-rework">...</a>
 */
public class CapabilityBackpackWrapper {
    private CapabilityBackpackWrapper() {}

    public static final ItemCapability<IBackpackWrapper, BackpackItem> BACKPACK_WRAPPER_CAPABILITY =
            ItemCapability.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "backpack_wrapper"),
                    IBackpackWrapper.class, BackpackItem.class);

    public static void onRegister(RegisterCapabilitiesEvent event) {
        event.registerItem(BACKPACK_WRAPPER_CAPABILITY, (object, object2) -> {
            return (numberOfInventorySlots, numberOfUpgradeSlots) -> {
                // TODO - why should I do this????
            };
        }, CampingItems.BACKPACKS);
    }
}
