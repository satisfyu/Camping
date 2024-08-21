package net.satisfy.camping.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MultitoolItem extends Item {

    public MultitoolItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();

        if (player != null) {
            Property<?> facingProperty = getFacingProperty(state);
            if (facingProperty instanceof DirectionProperty directionProperty) {
                BlockState rotatedState = rotateBlock(state, directionProperty);
                level.setBlock(pos, rotatedState, 3);
                spawnBlockParticles(level, pos, rotatedState);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private Property<?> getFacingProperty(BlockState state) {
        if (state.hasProperty(BlockStateProperties.FACING)) {
            return BlockStateProperties.FACING;
        } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return BlockStateProperties.HORIZONTAL_FACING;
        }
        return null;
    }

    private BlockState rotateBlock(BlockState state, DirectionProperty property) {
        return state.setValue(property, state.getValue(property).getClockWise());
    }

    private void spawnBlockParticles(Level level, BlockPos pos, BlockState state) {
        for (Direction direction : Direction.values()) {
            double x = pos.getX() + 0.5 + 0.5 * direction.getStepX();
            double y = pos.getY() + 0.5 + 0.5 * direction.getStepY();
            double z = pos.getZ() + 0.5 + 0.5 * direction.getStepZ();

            level.addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK, state),
                    x, y, z,
                    direction.getStepX() * 0.1,
                    direction.getStepY() * 0.1,
                    direction.getStepZ() * 0.1
            );
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.camping.multitool").withStyle(ChatFormatting.WHITE));
    }
}
