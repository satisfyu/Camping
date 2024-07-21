package net.satisfy.camping.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class EnderpackItem extends BlockItem implements Equipable {
    protected final ArmorMaterial material;
    protected final ArmorItem.Type type;
    private final ResourceLocation backpackTexture;

    public EnderpackItem(Block block, Properties settings, ArmorMaterial material, ArmorItem.Type type, ResourceLocation backpackTexture) {
        super(block, settings.stacksTo(1));
        this.material = material;
        this.type = type;
        this.backpackTexture = backpackTexture;
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
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(27, ItemStack.EMPTY); // 27 = typical Ender Chest size
        int index = 0;

        for (ItemStack stack : (Iterable<ItemStack>) getContents(itemStack)::iterator) {
            nonNullList.set(index++, stack);
        }
        return Optional.of(new BundleTooltip(nonNullList, 64));
    }

    public static SimpleContainer getContainer(ItemStack stack) {
        SimpleContainer container = new SimpleContainer(27); // 27 = typical Ender Chest size
        NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

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

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (!level.isClientSide) {
                if (block instanceof EnderChestBlock) {
                    player.openMenu(new SimpleMenuProvider((id, playerInventory, playerEntity) -> {
                        return new GenericContainerMenu(MenuType.GENERIC_9x3, id, playerInventory, player.getEnderChestInventory(), 3);
                    }, Component.translatable("container.enderchest")));
                }
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
