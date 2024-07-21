package net.satisfy.camping.item.backpack;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.camping.Camping;
import net.satisfy.camping.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderBackpackItem extends Item implements Equipable {

    protected final ArmorMaterial material;
    protected final ArmorItem.Type type;

    public EnderBackpackItem(ArmorMaterial material, ArmorItem.Type type, Properties properties) {
        super(properties.defaultDurability(material.getDurabilityForType(type)));

        this.material = material;
        this.type = type;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return this.material.getEquipSound();
    }

//    @Override
//    public InteractionResultHolder<ItemStack> swapWithEquipmentSlot(Item item, Level level, Player player, InteractionHand interactionHand) {
//        return Equipable.super.swapWithEquipmentSlot(item, level, player, interactionHand);
//    }

    @Override
    public @Nullable ResourceLocation arch$registryName() {
        return new ResourceLocation(Camping.MODID, "ender_backpack");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        player.openMenu(new SimpleMenuProvider((containerID, inventory, contextualPlayer) -> {
            return ChestMenu.threeRows(containerID, inventory, contextualPlayer.getEnderChestInventory());
        }, Component.translatable("container.camping.ender_backpack")));

        return super.use(level, player, interactionHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        BlockState state = ObjectRegistry.ENDERPACK.get().defaultBlockState();
        BlockPos relativePos = pos.relative(pContext.getClickedFace());

        // only place if crouching, otherwise we always open the menu

        if (player != null) {

            if (player.isShiftKeyDown() && level.isEmptyBlock(relativePos)) {
                if (level.setBlockAndUpdate(relativePos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, player.getDirection().getOpposite()))) {
                    player.playSound(SoundEvents.ENDER_CHEST_CLOSE, 0.5f, 0.5f);
                    level.gameEvent(player, GameEvent.BLOCK_PLACE, relativePos);
                    pContext.getItemInHand().shrink(1);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }

            player.openMenu(new SimpleMenuProvider((containerID, inventory, contextualPlayer) -> {
                return ChestMenu.threeRows(containerID, inventory, contextualPlayer.getEnderChestInventory());
            }, Component.translatable("container.camping.ender_backpack")));
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
