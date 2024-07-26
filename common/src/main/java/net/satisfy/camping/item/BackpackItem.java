package net.satisfy.camping.item;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.block.BackpackBlock;
import net.satisfy.camping.block.BackpackContainer;
import net.satisfy.camping.block.entity.BackpackBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class BackpackItem extends BlockItem implements Equipable {
    protected final ArmorMaterial material;
    protected final ArmorItem.Type type;
    private final ResourceLocation backpackTexture;

    public final BackpackBlock.BackpackType backpackType;

    public BackpackItem(Block block, Properties settings, ArmorMaterial material, ArmorItem.Type type, ResourceLocation backpackTexture) {
        super(block, settings.stacksTo(1));

        this.backpackType = ((BackpackBlock) block).getBackpackType();
        this.material = material;
        this.type = type;
        this.backpackTexture = backpackTexture;
    }

    public static Stream<ItemStack> getContents(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) {
            return Stream.empty();
        } else {
            CompoundTag compoundTag2 = compoundTag.getCompound("BlockEntityTag");
            ListTag listTag = compoundTag2.getList("Items", 10);
            Stream<Tag> items = listTag.stream();
            Objects.requireNonNull(CompoundTag.class);
            return items.map(CompoundTag.class::cast).map(ItemStack::of);
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

    public static BackpackContainer getContainer(ItemStack stack) {
        BackpackContainer container = new BackpackContainer(stack);
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

    public ResourceLocation getBackpackTexture() {
        return backpackTexture;
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
