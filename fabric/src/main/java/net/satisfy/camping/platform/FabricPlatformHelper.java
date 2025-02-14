package net.satisfy.camping.platform;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.item.BackpackItem;
import net.satisfy.camping.core.world.item.EnderpackItem;
import net.satisfy.camping.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {

        return FabricBlockEntityTypeBuilder.create(func::apply, blocks).build();
    }

    public ItemStack getEquippedBackpack(Player player) {
        ItemStack chestSlotItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestSlotItem.getItem() instanceof BackpackItem || chestSlotItem.getItem() instanceof EnderpackItem) {
            return chestSlotItem;
        }
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        if (component.isPresent()) {
            TrinketComponent trinketComponent = component.get();
            List<Tuple<SlotReference, ItemStack>> equippedItems;

            equippedItems = trinketComponent.getEquipped(CampingItems.SMALL_BACKPACK);
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(CampingItems.LARGE_BACKPACK);
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(CampingItems.WANDERER_BACKPACK);
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(CampingItems.WANDERER_BAG);
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(CampingItems.SHEEPBAG);
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(CampingItems.GOODYBAG);
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(CampingItems.ENDERPACK);
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(CampingItems.ENDERBAG);
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }
        }
        return ItemStack.EMPTY;
    }
}
