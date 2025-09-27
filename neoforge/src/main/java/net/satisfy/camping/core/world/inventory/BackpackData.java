package net.satisfy.camping.core.world.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;

public final class BackpackData {
    public static NonNullList<ItemStack> read(ItemStack stack) {
        NonNullList<ItemStack> out = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
        stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(out);
        return out;
    }

    public static void write(ItemStack stack, NonNullList<ItemStack> items) {
        stack.remove(DataComponents.BLOCK_ENTITY_DATA);
        if (allEmpty(items)) stack.remove(DataComponents.CONTAINER);
        else stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    public static void migrateFromBlockEntityData(ItemStack stack, net.minecraft.core.RegistryAccess access) {
        var be = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (be == null) return;
        CompoundTag tag = be.copyTag();
        NonNullList<ItemStack> out = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
        if (tag.contains("Items", 9)) ContainerHelper.loadAllItems(tag, out, access);
        write(stack, out);
    }

    private static boolean allEmpty(NonNullList<ItemStack> items) {
        for (ItemStack s : items) if (!s.isEmpty()) return false;
        return true;
    }

    private BackpackData() {}
}
