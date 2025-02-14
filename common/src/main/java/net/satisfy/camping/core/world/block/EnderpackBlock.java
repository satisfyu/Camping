package net.satisfy.camping.core.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.world.block.entity.EnderpackBlockEntity;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.util.CampingUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class EnderpackBlock extends BaseEntityBlock {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  private final BackpackType backpackType;

  public EnderpackBlock(Properties properties, BackpackType backpackType) {
    super(properties);
    this.backpackType = backpackType;
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
    return new ItemStack(CampingItems.ENDERPACK);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (player.isCrouching()) {
      level.destroyBlock(pos, false);
      level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CampingItems.ENDERPACK)));
    } else {
      player.openMenu(new SimpleMenuProvider((id, inventory, playerEntity) -> ChestMenu.threeRows(id, inventory, player.getEnderChestInventory()), CampingItems.ENDERPACK.getName(new ItemStack(CampingItems.ENDERPACK))));
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

  private static final Supplier<VoxelShape> ENDERPACK = () -> {
    VoxelShape shape = Shapes.empty();
    shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.3125, 0.8125, 0.625, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.25, 0.6875, 0.5, 0.3125), BooleanOp.OR);
    return shape;
  };

  private static final Supplier<VoxelShape> ENDERBAG = () -> {
    VoxelShape shape = Shapes.empty();
    shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.4375, 0.8125, 0.75, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.4375, 0.5, 0.375, 0.5625, 0.625, 0.4375), BooleanOp.OR);
    return shape;
  };

  public static final Map<BackpackType, Map<Direction, VoxelShape>> SHAPES = net.minecraft.Util.make(new HashMap<>(), map -> {
    map.put(BackpackType.ENDERPACK, generateShapes(ENDERPACK));
    map.put(BackpackType.ENDERBAG, generateShapes(ENDERBAG));
  });

  private static Map<Direction, VoxelShape> generateShapes(Supplier<VoxelShape> shapeSupplier) {
    Map<Direction, VoxelShape> shapeMap = new HashMap<>();
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      shapeMap.put(direction, CampingUtil.rotateShape(Direction.NORTH, direction, shapeSupplier.get()));
    }
    return shapeMap;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return SHAPES.get(backpackType).get(state.getValue(FACING));
  }

  public enum BackpackType {
    ENDERPACK, ENDERBAG
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new EnderpackBlockEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, CampingBlockEntities.ENDERPACK,
            (world, pos, state1, blockEntity) -> blockEntity.tick(world, pos, state1, blockEntity));
  }
}
