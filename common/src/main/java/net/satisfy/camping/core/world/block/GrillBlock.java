package net.satisfy.camping.core.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.core.world.block.entity.GrillBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class GrillBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<GrillBlock> CODEC = simpleCodec(GrillBlock::new);

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final Supplier<VoxelShape> VOXEL_SHAPE_SUPPLIER;
    public static final Map<Direction, VoxelShape> SHAPE;

    public GrillBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                              BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof GrillBlockEntity grill)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Optional: nur wenn der Grill brennt darf man was drauflegen
        if (!state.getValue(LIT)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        var opt = grill.getCookableRecipe(stack);
        if (opt.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // WICHTIG: auf dem Client nur Erfolg signalisieren -> Server führt Logik aus
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        int time = opt.get().getCookingTime();

        // Nur EIN Item platzieren
        ItemStack one = stack.copyWithCount(1);

        if (grill.placeFood(player, one, time)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1); // nur wenn placeFood NICHT selbst verkleinert
            }
            // Server: Interaktion konsumiert
            return ItemInteractionResult.CONSUME;
        }

        // Slot voll o.Ä.: trotzdem handled
        return ItemInteractionResult.CONSUME;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean waterlogged = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(WATERLOGGED, waterlogged)
                .setValue(LIT, !waterlogged)
                .setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(LIT) && entity instanceof Player) {
            entity.hurt(world.damageSources().hotFloor(), 1.0F);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GrillBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CampingBlockEntities.GRILL, level.isClientSide ? GrillBlockEntity::particleTick : GrillBlockEntity::cookTick);
    }

    static {
        VOXEL_SHAPE_SUPPLIER = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.625, 0.1875, 0.8125, 1, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.3125, 0.625, 0.3125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.6875, 0, 0.25, 0.75, 0.625, 0.3125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.6875, 0.3125, 0.625, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.6875, 0, 0.6875, 0.75, 0.625, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.625, 0.25, 0.1875, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.625, 0.8125, 0.1875, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.375, 0.75, 0.0625, 0.625, 0.875, 0.125), BooleanOp.OR);
            return shape;
        };
        SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction dir : Direction.Plane.HORIZONTAL.stream().toList()) {
                map.put(dir, CampingUtil.rotateShape(Direction.NORTH, dir, VOXEL_SHAPE_SUPPLIER.get()));
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.camping.canbeplaced").setStyle(Style.EMPTY.withColor(0x556B2F).withItalic(true)));
    }
}
