package net.satisfy.camping.core.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.satisfy.camping.core.registry.CampingBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class TentBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<TentBlock> CODEC = simpleCodec(TentBlock::new);

    public TentBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        if (this instanceof TentMainBlock tentMainBlock) {
            DyeColor color = tentMainBlock.getColor();
            return new ItemStack(CampingBlocks.TENT_MAIN.get(color.getName()));
        }
        return new ItemStack(this);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Monster && level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            level.destroyBlock(pos, true, entity);
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockPos lowerPos = getLowerHalfPos(state, pos);
            BlockState lowerState = level.getBlockState(lowerPos);
            BlockPos mainPos = getMainPos(lowerState, lowerPos);
            BlockState mainState = level.getBlockState(mainPos);

            if (mainState.getBlock() instanceof TentMainBlock) {
                ItemStack dropStack = mainState.getBlock().getCloneItemStack(level, mainPos, mainState);

                removeTentPart(level, mainPos);
                removeTentPart(level, mainPos.above());
                removeTentPart(level, mainPos.relative(mainState.getValue(FACING).getOpposite()));
                removeTentPart(level, mainPos.relative(mainState.getValue(FACING).getOpposite()).above());
                removeTentPart(level, mainPos.relative(mainState.getValue(FACING).getCounterClockWise()));
                removeTentPart(level, mainPos.relative(mainState.getValue(FACING).getCounterClockWise()).above());
                removeTentPart(level, mainPos.relative(mainState.getValue(FACING).getCounterClockWise()).relative(mainState.getValue(FACING).getOpposite()));
                removeTentPart(level, mainPos.relative(mainState.getValue(FACING).getCounterClockWise()).relative(mainState.getValue(FACING).getOpposite()).above());

                if (!player.isCreative()) {
                    popResource(level, mainPos, dropStack);
                }
            }
        }

        super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    private static void removeTentPart(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() instanceof TentBlock) {
            level.removeBlock(blockPos, false);
        }
    }

    private static BlockPos getLowerHalfPos(BlockState state, BlockPos pos) {
        if (state.hasProperty(TentMainBlock.HALF) && state.getValue(TentMainBlock.HALF) == DoubleBlockHalf.UPPER) {
            return pos.below();
        }
        return pos;
    }

    private static BlockPos getMainPos(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);

        if (state.getBlock() instanceof TentMainBlock) {
            return pos;
        }

        if (state.getBlock() instanceof TentMainHeadBlock) {
            return pos.relative(facing);
        }

        if (state.getBlock() instanceof TentRightBlock) {
            return pos.relative(facing.getClockWise());
        }

        if (state.getBlock() instanceof TentRightHeadBlock) {
            return pos.relative(facing).relative(facing.getClockWise());
        }

        return pos;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.camping.canbeplaced").setStyle(Style.EMPTY.withColor(0x556B2F).withItalic(true)));
    }
}