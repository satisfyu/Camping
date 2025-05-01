package net.satisfy.camping.platform;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.world.item.BackpackBlockItem;
import net.satisfy.camping.core.world.item.EnderpackBlockItem;
import net.satisfy.camping.optional.trinkets.TrinketsHelper;
import net.satisfy.camping.platform.services.IPlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

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
    public String getGameDirectory() {

        return FabricLoader.getInstance().getGameDir().toString();
    }

    @Override
    public boolean isClientSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> o, FeatureFlagSet vanillaSet) {
        return new MenuType<T>(o::apply, vanillaSet);
    }

    public ItemStack getEquippedBackpack(Player player) {
        ItemStack chestSlotItem = player.getItemBySlot(EquipmentSlot.CHEST);

        if (chestSlotItem.getItem() instanceof BackpackBlockItem || chestSlotItem.getItem() instanceof EnderpackBlockItem) {
            return chestSlotItem;
        }
        else if (isModLoaded("trinkets")) return TrinketsHelper.getBackpackFromTrinkets(player);

        return ItemStack.EMPTY;
    }
}
