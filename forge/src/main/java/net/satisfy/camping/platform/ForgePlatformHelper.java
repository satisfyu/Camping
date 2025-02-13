package net.satisfy.camping.platform;

import net.minecraft.core.BlockPos;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {

        return BlockEntityType.Builder.of(func::apply, blocks).build(null);
    }

    public ItemStack getEquippedBackpack(Player player) {

        ItemStack chestSlotItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestSlotItem.getItem() instanceof BackpackItem || chestSlotItem.getItem() instanceof EnderpackItem) {
            return chestSlotItem;
        }

        AtomicReference<ItemStack> extraSlotItem = new AtomicReference<>(ItemStack.EMPTY);
        if (Services.PLATFORM.isModLoaded("curios")) {
            CuriosApi.getCuriosInventory(player).ifPresent(curioInventory -> {
                curioInventory.findFirstCurio(CampingItems.SMALL_BACKPACK).ifPresent(slotResult -> {
                    extraSlotItem.set(slotResult.stack());
                });
                curioInventory.findFirstCurio(CampingItems.LARGE_BACKPACK).ifPresent(slotResult -> {
                    extraSlotItem.set(slotResult.stack());
                });
                curioInventory.findFirstCurio(CampingItems.WANDERER_BAG).ifPresent(slotResult -> {
                    extraSlotItem.set(slotResult.stack());
                });
                curioInventory.findFirstCurio(CampingItems.WANDERER_BACKPACK).ifPresent(slotResult -> {
                    extraSlotItem.set(slotResult.stack());
                });
                curioInventory.findFirstCurio(CampingItems.GOODYBAG).ifPresent(slotResult -> {
                    extraSlotItem.set(slotResult.stack());
                });
                curioInventory.findFirstCurio(CampingItems.SHEEPBAG).ifPresent(slotResult -> {
                    extraSlotItem.set(slotResult.stack());
                });
                curioInventory.findFirstCurio(CampingItems.ENDERBAG).ifPresent(slotResult -> {
                    extraSlotItem.set(slotResult.stack());
                });
                curioInventory.findFirstCurio(CampingItems.ENDERPACK).ifPresent(slotResult -> {
                    extraSlotItem.set(slotResult.stack());
                });
            });
        }

        if (extraSlotItem.get() != null) return extraSlotItem.get();

//        AtomicReference<ItemStack> extraSlotItem = new AtomicReference<ItemStack>(ItemStack.EMPTY);
//
//        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
//            iCuriosItemHandler.getUpdatingInventories().forEach(iCurioStacksHandler -> {
//                for (int i = 0; i < iCurioStacksHandler.getStacks().getSlots(); i++) {
//                    if (iCurioStacksHandler.getStacks().getStackInSlot(i) != ItemStack.EMPTY) extraSlotItem.set(iCurioStacksHandler.getStacks().getStackInSlot(i));
//                }
//            });
//        });

        // return extraSlotItem.get();

//        return extraSlotItem.get() == null ? ItemStack.EMPTY : extraSlotItem.get();

        return ItemStack.EMPTY;
    }
}