package net.satisfy.camping.item;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.block.BackpackBlock;
import net.satisfy.camping.block.BackpackType;
import net.satisfy.camping.block.entity.BackpackBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BackpackItem extends BlockItem implements Equipable {

    private final ResourceLocation backpackTexture;
    public final BackpackType backpackType;

    public BackpackItem(Block block, ResourceLocation backpackTexture) {
        super(block, new Properties().fireResistant().stacksTo(1));

        this.backpackType = ((BackpackBlock) block).getBackpackType();
        this.backpackTexture = backpackTexture;
    }

    public ResourceLocation getBackpackTexture() {
        return backpackTexture;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return ArmorMaterials.LEATHER.getEquipSound();
    }

    public static NonNullList<ItemStack> getContents(ItemStack itemStack) {

        NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);

        CompoundTag blockEntityTag = BlockItem.getBlockEntityData(itemStack);

        if (blockEntityTag == null) {
            return itemStacks;
        } else {

//        NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);

            blockEntityTag = BlockItem.getBlockEntityData(itemStack);

            ContainerHelper.loadAllItems(blockEntityTag, itemStacks);

            return itemStacks;

//        CompoundTag compoundTag = itemStack.getTag();
//
//        if (compoundTag == null) {
//            return Stream.empty();
//        } else {
//            CompoundTag compoundTag2 = compoundTag.getCompound("BlockEntityTag");
//            ListTag listTag = compoundTag2.getList("Items", 9);
//            Stream<Tag> items = listTag.stream();
//            Objects.requireNonNull(CompoundTag.class);
//            return items.map(CompoundTag.class::cast).map(ItemStack::of);
//        }
        }
    }

    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
//        NonNullList<ItemStack> nonNullList = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
//        int index = 0;

//        Iterator<ItemStack> it = getContents(itemStack).iterator();
//        List<ItemStack> stackList = new ArrayList<>();
//
//        for (ItemStack stack : getContents(itemStack).toList()) {
//
//        }

        return Optional.of(new BundleTooltip(getContents(itemStack), 64));


//        while (it.hasNext()) {
//            ItemStack next = it.next();
//        }

//        for (ItemStack stack : (Iterable<ItemStack>) BackpackItem.getContents(itemStack)::iterator) {
//            nonNullList.set(index++, stack);
//        }
//        return Optional.of(new BundleTooltip(nonNullList, 64));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        return InteractionResultHolder.pass(player.getItemInHand(interactionHand));

        // used for right-click interaction
//        ItemStack itemStack = player.getMainHandItem();
//
//        if (level.isClientSide() || interactionHand == InteractionHand.OFF_HAND) {
//            return InteractionResultHolder.fail(itemStack);
//        }
//
//        CompoundTag blockEntityTag = BlockItem.getBlockEntityData(itemStack);
//
//        if (blockEntityTag != null) {
//
//            if (blockEntityTag.contains("Items", 9)) {
//
//                NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
//
//                ContainerHelper.loadAllItems(blockEntityTag, itemStacks);
//
//                player.openMenu(new SimpleMenuProvider(new MenuConstructor() {
//                    @Override
//                    public @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
//                        return new BackpackScreenHandler(i, player.getInventory(), new BackpackContainer(itemStacks, player));
//                    }
//                }, Component.translatable("container.camping.backpack")));
//            }
//        }
//        else {
//
//            CompoundTag compoundTag = new CompoundTag();
//
//            ContainerHelper.saveAllItems(compoundTag, NonNullList.withSize(24, ItemStack.EMPTY));
//
//            itemStack.addTagElement("BlockEntityTag", compoundTag);
//
//            NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
//
//            blockEntityTag = BlockItem.getBlockEntityData(itemStack);
//
//            ContainerHelper.loadAllItems(blockEntityTag, itemStacks);
//
//            player.openMenu(new SimpleMenuProvider(new MenuConstructor() {
//                @Override
//                public @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
//                    return new BackpackScreenHandler(i, player.getInventory(), new BackpackContainer(itemStacks, player));
//                }
//            }, Component.translatable("container.camping.backpack")));
//
//        }
//
//        return InteractionResultHolder.sidedSuccess(player.getMainHandItem(), level.isClientSide());
    }
}
