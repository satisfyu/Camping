package net.satisfy.camping.core.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.satisfy.camping.core.util.MultitoolUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultitoolItem extends Item {

    public MultitoolItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (!blockState.hasProperty(BlockStateProperties.FACING) && !blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            MultitoolUtil.rotateBlock(level, blockPos, blockState, player.isShiftKeyDown());
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.camping.multitool").setStyle(Style.EMPTY.withColor(0x556B2F))
        );
    }
}
