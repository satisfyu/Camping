package net.satisfy.camping.core.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.core.util.EnderpackVariant;
import net.satisfy.camping.core.world.block.entity.EnderpackBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EnderpackBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Supplier<VoxelShape> ENDERPACK;
    private static final Supplier<VoxelShape> ENDERBAG;
    public static final Map<EnderpackVariant, Map<Direction, VoxelShape>> SHAPES;

    private final EnderpackVariant variant;
    private final MapCodec<EnderpackBlock> codec;

    public EnderpackBlock(Properties properties, EnderpackVariant variant) {
        super(properties);
        this.variant = variant;
        this.codec = simpleCodec(p -> new EnderpackBlock(p, this.variant));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return this.codec;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fs = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fs.getType() == Fluids.WATER);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootBuilder) {
        return this.variant == EnderpackVariant.ENDERPACK ? List.of(new ItemStack(CampingItems.ENDERPACK)) : List.of(new ItemStack(CampingItems.ENDERBAG));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof EnderpackBlock enderpack) {
            if (enderpack.variant == EnderpackVariant.ENDERPACK) return new ItemStack(CampingItems.ENDERPACK);
            if (enderpack.variant == EnderpackVariant.ENDERBAG) return new ItemStack(CampingItems.ENDERBAG);
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isCrouching()) {
            level.destroyBlock(pos, false);
            ItemStack drop = new ItemStack(this.variant == EnderpackVariant.ENDERPACK ? CampingItems.ENDERPACK : CampingItems.ENDERBAG);
            ItemEntity ie = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
            ie.setDefaultPickUpDelay();
            level.addFreshEntity(ie);
            return InteractionResult.CONSUME;
        } else {
            player.openMenu(new SimpleMenuProvider((id, inv, p) -> ChestMenu.threeRows(id, inv, player.getEnderChestInventory()), CampingItems.ENDERPACK.getName(new ItemStack(CampingItems.ENDERPACK))));
            player.awardStat(Stats.OPEN_ENDERCHEST);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 3; ++i) {
            int pm = random.nextInt(2) * 2 - 1;
            int sm = random.nextInt(2) * 2 - 1;
            double x = pos.getX() + 0.5 + 0.25 * pm;
            double y = pos.getY() + random.nextFloat();
            double z = pos.getZ() + 0.5 + 0.25 * sm;
            double vx = random.nextFloat() * pm;
            double vy = (random.nextFloat() - 0.5) * 0.125;
            double vz = random.nextFloat() * sm;
            level.addParticle(ParticleTypes.PORTAL, x, y, z, vx, vy, vz);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnderpackBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CampingBlockEntities.ENDERPACK, (lvl, p, s, be) -> be.tick(lvl, p, s, be));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof EnderpackBlockEntity enderpack) {
                dropSelf(level, pos, enderpack);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void dropSelf(Level level, BlockPos pos, EnderpackBlockEntity be) {
        ItemStack stack = new ItemStack(this.variant == EnderpackVariant.ENDERPACK ? CampingItems.ENDERPACK : CampingItems.ENDERBAG);
        if (be.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, be.getCustomName());
        }
        ItemEntity ie = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        ie.setDefaultPickUpDelay();
        level.addFreshEntity(ie);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(variant).get(state.getValue(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    private static Map<Direction, VoxelShape> generateShapes(Supplier<VoxelShape> shapeSupplier) {
        Map<Direction, VoxelShape> shapeMap = new HashMap<>();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            shapeMap.put(direction, CampingUtil.rotateShape(Direction.NORTH, direction, shapeSupplier.get()));
        }
        return shapeMap;
    }

    static {
        ENDERPACK = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.3125, 0.8125, 0.625, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.25, 0.6875, 0.5, 0.3125), BooleanOp.OR);
            return shape;
        };
        ENDERBAG = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.4375, 0.8125, 0.75, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.5, 0.375, 0.5625, 0.625, 0.4375), BooleanOp.OR);
            return shape;
        };
        SHAPES = new HashMap<>();
        SHAPES.put(EnderpackVariant.ENDERPACK, generateShapes(ENDERPACK));
        SHAPES.put(EnderpackVariant.ENDERBAG, generateShapes(ENDERBAG));
    }
}
