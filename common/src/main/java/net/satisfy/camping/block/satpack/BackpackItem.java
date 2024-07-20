package net.satisfy.camping.block.satpack;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class BackpackItem extends BlockItem {
    public BackpackItem(Block block, Properties settings) {
        super(block, settings.stacksTo(1));
    }

    private static Stream<ItemStack> getContents(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) {
            return Stream.empty();
        } else {
            CompoundTag compoundTag2 = compoundTag.getCompound("BlockEntityTag");
            ListTag listTag = compoundTag2.getList("Items", 10);
            Stream<Tag> var10000 = listTag.stream();
            Objects.requireNonNull(CompoundTag.class);
            return var10000.map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
        int index = 0;

        for (ItemStack stack : (Iterable<ItemStack>) getContents(itemStack)::iterator) {
            nonNullList.set(index++, stack);
        }
        return Optional.of(new BundleTooltip(nonNullList, 64));
    }

    public static SimpleContainer getContainer(ItemStack stack) {
        SimpleContainer container = new SimpleContainer(BackpackBlockEntity.CONTAINER_SIZE);
        NonNullList<ItemStack> items = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);

        int index = 0;
        for (ItemStack itemStack : (Iterable<ItemStack>) getContents(stack)::iterator) {
            items.set(index++, itemStack);
        }

        for (int i = 0; i < items.size(); i++) {
            container.setItem(i, items.get(i));
        }

        return container;
    }
}
