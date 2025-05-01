package net.satisfy.camping.platform;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.item.BackpackBlockItem;
import net.satisfy.camping.core.world.item.EnderpackBlockItem;
import net.satisfy.camping.optional.ForgeCuriosHelper;
import net.satisfy.camping.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
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
    public String getGameDirectory() {

        return FMLPaths.GAMEDIR.get().toString();
    }

    @Override
    public boolean isClientSide() {
        return FMLLoader.getDist().isClient();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> o, FeatureFlagSet vanillaSet) {
        return new MenuType<T>(o::apply, vanillaSet);
    }

    @Override
    public ItemStack getEquippedBackpack(Player player) {

        ItemStack chestSlotItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestSlotItem.getItem() instanceof BackpackBlockItem || chestSlotItem.getItem() instanceof EnderpackBlockItem) {
            return chestSlotItem;
        }
        else if (Services.PLATFORM.isModLoaded("curios")) return ForgeCuriosHelper.getBackpackFromCurios(player);

        return ItemStack.EMPTY;
    }
}