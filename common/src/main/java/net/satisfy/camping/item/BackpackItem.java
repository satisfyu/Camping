package net.satisfy.camping.item;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.block.entity.BackpackBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class BackpackItem extends BlockItem implements Equipable {
    protected final ArmorMaterial material;
    protected final ArmorItem.Type type;

    public BackpackItem(Block block, Properties settings, ArmorMaterial material, ArmorItem.Type type) {
        super(block, settings.stacksTo(1));
        this.material = material;
        this.type = type;
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

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return this.material.getEquipSound();
    }
}
