package net.satisfy.camping.core.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.camping.core.registry.CampingBlocks;
import net.satisfy.camping.core.registry.CampingItems;
import org.jetbrains.annotations.NotNull;

public class EnderpackBlockItem extends BlockItem {

    private final ResourceLocation texture;

    public EnderpackBlockItem(Block block, ResourceLocation texture) {
        super(block, new Properties().fireResistant().stacksTo(1));
        this.texture = texture;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos relativePos = clickedPos.relative(context.getClickedFace());

        BlockState blockState = null;

        if (context.getItemInHand().getItem() == CampingItems.ENDERPACK) {
            blockState = CampingBlocks.ENDERPACK.defaultBlockState();
        }
        else if (context.getItemInHand().getItem() == CampingItems.ENDERBAG) {
            blockState = CampingBlocks.ENDERBAG.defaultBlockState();
        }

        if (blockState != null && player != null && level.isEmptyBlock(relativePos)) {
            if (level.setBlockAndUpdate(relativePos, blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, player.getDirection().getOpposite()))) {
                player.playSound(SoundEvents.ENDER_CHEST_CLOSE, 0.5f, 0.5f);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, relativePos);
                context.getItemInHand().shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.FAIL;
    }
}
