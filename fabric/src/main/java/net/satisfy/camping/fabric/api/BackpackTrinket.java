package net.satisfy.camping.fabric.api;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Optional;

import static net.satisfy.camping.registry.ObjectRegistry.*;

@SuppressWarnings("unused")
public class BackpackTrinket extends TrinketItem {
    public BackpackTrinket(Properties settings) {
        super(settings);
    }

    public static boolean isEquippedBy(Player player) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        if (component.isPresent()) {
            TrinketComponent trinketComponent = component.get();
            return isEquipped(trinketComponent, SMALL_BACKPACK_ITEM.get()) ||
                    isEquipped(trinketComponent, LARGE_BACKPACK_ITEM.get()) ||
                    isEquipped(trinketComponent, WANDERER_BACKPACK_ITEM.get()) ||
                    isEquipped(trinketComponent, WANDERER_BAG_ITEM.get()) ||
                    isEquipped(trinketComponent, SHEEPBAG_ITEM.get()) ||
                    isEquipped(trinketComponent, GOODYBAG_ITEM.get()) ||
                    isEquipped(trinketComponent, ENDERPACK_ITEM.get()) ||
                    isEquipped(trinketComponent, ENDERBAG_ITEM.get());

        }
        return false;
    }

    private static boolean isEquipped(TrinketComponent component, Item item) {
        return component.isEquipped(item);
    }
}
