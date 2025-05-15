package net.satisfy.camping.core.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.core.world.block.entity.TurnedCampfireBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class TurnedCampfireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    private static final float FIRE_DAMAGE = 1.0f;
    public static final VoxelShape SHAPE = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)7.0F, (double)16.0F);

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TurnedCampfireBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true).setValue(SIGNAL_FIRE, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof TurnedCampfireBlockEntity campfire) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            Optional<CampfireCookingRecipe> optional = campfire.getCookableRecipe(itemStack);
            if (optional.isPresent()) {
                if (!level.isClientSide && campfire.placeFood(player, player.getAbilities().instabuild ? itemStack.copy() : itemStack, optional.get().getCookingTime())) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, SIGNAL_FIRE, WATERLOGGED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;

        LevelAccessor accessor = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(LIT, !waterlogged)
                .setValue(SIGNAL_FIRE, this.isSmokeSource(accessor.getBlockState(pos.below())))
                .setValue(WATERLOGGED, waterlogged)
                .setValue(FACING, context.getHorizontalDirection());
    }

    private boolean isSmokeSource(BlockState $$0) {
        return $$0.is(Blocks.HAY_BLOCK);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return direction == Direction.DOWN ? state.setValue(SIGNAL_FIRE, this.isSmokeSource(neighborState)) : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public static void dowse( Entity $$0, LevelAccessor $$1, BlockPos $$2, BlockState $$3) {
        if ($$1.isClientSide()) {
            for(int $$4 = 0; $$4 < 20; ++$$4) {
                makeParticles((Level)$$1, $$2, (Boolean)$$3.getValue(SIGNAL_FIRE), true);
            }
        }

        BlockEntity $$5 = $$1.getBlockEntity($$2);
        if ($$5 instanceof TurnedCampfireBlockEntity) {
            ((TurnedCampfireBlockEntity)$$5).dowse();
        }

        $$1.gameEvent($$0, GameEvent.BLOCK_CHANGE, $$2);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(LIT) && entity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) {
            entity.hurt(world.damageSources().hotFloor(), FIRE_DAMAGE);
        }
        super.stepOn(world, pos, state, entity);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TurnedCampfireBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CampingBlockEntities.TURNED_CAMPFIRE, level.isClientSide ? TurnedCampfireBlockEntity::particleTick : TurnedCampfireBlockEntity::cookTick);
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }

    public static void makeParticles(Level $$0, BlockPos $$1, boolean $$2, boolean $$3) {
        RandomSource $$4 = $$0.getRandom();
        SimpleParticleType $$5 = $$2 ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        $$0.addAlwaysVisibleParticle($$5, true, (double)$$1.getX() + (double)0.5F + $$4.nextDouble() / (double)3.0F * (double)($$4.nextBoolean() ? 1 : -1), (double)$$1.getY() + $$4.nextDouble() + $$4.nextDouble(), (double)$$1.getZ() + (double)0.5F + $$4.nextDouble() / (double)3.0F * (double)($$4.nextBoolean() ? 1 : -1), (double)0.0F, 0.07, (double)0.0F);
        if ($$3) {
            $$0.addParticle(ParticleTypes.SMOKE, (double)$$1.getX() + (double)0.5F + $$4.nextDouble() / (double)4.0F * (double)($$4.nextBoolean() ? 1 : -1), (double)$$1.getY() + 0.4, (double)$$1.getZ() + (double)0.5F + $$4.nextDouble() / (double)4.0F * (double)($$4.nextBoolean() ? 1 : -1), (double)0.0F, 0.005, (double)0.0F);
        }

    }
}
