package net.satisfy.camping.core.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
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

    public EnderpackBlock(Properties properties, EnderpackVariant variant) {
        super(properties);
        this.variant = variant;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootBuilder) {
        return this.variant == EnderpackVariant.ENDERPACK ? List.of(new ItemStack(CampingItems.ENDERPACK)) : List.of(new ItemStack(CampingItems.ENDERBAG));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof EnderpackBlock enderpack) {
            if (enderpack.variant == EnderpackVariant.ENDERPACK) return new ItemStack(CampingItems.ENDERPACK);
            if (enderpack.variant == EnderpackVariant.ENDERBAG) return new ItemStack(CampingItems.ENDERBAG);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isCrouching()) {
            level.destroyBlock(pos, false);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CampingItems.ENDERPACK)));
        }
        else {
            player.openMenu(new SimpleMenuProvider((id, inventory, playerX) -> ChestMenu.threeRows(id, inventory, player.getEnderChestInventory()), CampingItems.ENDERPACK.getName(new ItemStack(CampingItems.ENDERPACK))));
            player.awardStat(Stats.OPEN_ENDERCHEST);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for(int i = 0; i < 3; ++i) {
            int posMultiplier = random.nextInt(2) * 2 - 1;
            int speedMultiplier = random.nextInt(2) * 2 - 1;
            double xPos = (double)pos.getX() + 0.5 + 0.25 * (double)posMultiplier;
            double yPos = (float)pos.getY() + random.nextFloat();
            double zPos = (double)pos.getZ() + 0.5 + 0.25 * (double)speedMultiplier;
            double xSpeed = random.nextFloat() * (float)posMultiplier;
            double ySpeed = ((double)random.nextFloat() - 0.5) * 0.125;
            double zSpeed = random.nextFloat() * (float)speedMultiplier;
            level.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnderpackBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CampingBlockEntities.ENDERPACK, (levelX, pos, stateX, enderpack) -> enderpack.tick(levelX, pos, stateX, enderpack));
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
        SHAPES = net.minecraft.Util.make(new HashMap<>(), map -> {
            map.put(EnderpackVariant.ENDERPACK, generateShapes(ENDERPACK));
            map.put(EnderpackVariant.ENDERBAG, generateShapes(ENDERBAG));
        });
    }
}
