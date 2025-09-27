package net.satisfy.camping.core.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.util.BackpackVariant;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.core.world.BackpackContainer;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public class BackpackBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final Map<BackpackVariant, Map<Direction, VoxelShape>> SHAPES;

    private final BackpackVariant variant;
    private final MapCodec<BackpackBlock> codec;

    public BackpackBlock(Properties properties, BackpackVariant variant) {
        super(properties);
        this.variant = variant;
        this.codec = simpleCodec(p -> new BackpackBlock(p, this.variant));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity living, ItemStack stack) {
        super.setPlacedBy(level, pos, state, living, stack);
        if (level.getBlockEntity(pos) instanceof BackpackBlockEntity bbe) {
            NonNullList<ItemStack> items = BackpackContainer.readFromItem(stack);
            for (int i = 0; i < items.size(); i++) bbe.setItem(i, items.get(i));
            bbe.setChanged();

        }
    }

    public BackpackVariant getVariant() {
        return variant;
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
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BackpackBlockEntity backpack) {
                dropBlockWithContents(level, pos, backpack);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fs = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fs.getType() == Fluids.WATER);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(this.variant).get(state.getValue(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack out = new ItemStack(this.asItem());
        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be instanceof BackpackBlockEntity bbe) {
            NonNullList<ItemStack> items = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
            for (int i = 0; i < items.size(); i++) items.set(i, bbe.getItem(i));
            BackpackContainer.writeToItem(out, items);
        }
        return List.of(out);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BackpackBlockEntity(pos, state);
    }

    private Item getBackpackItem() {
        return switch (this.getVariant()) {
            case WANDERER_BACKPACK -> CampingItems.WANDERER_BACKPACK;
            case LARGE_BACKPACK -> CampingItems.LARGE_BACKPACK;
            case SMALL_BACKPACK -> CampingItems.SMALL_BACKPACK;
            case WANDERER_BAG -> CampingItems.WANDERER_BAG;
            case SHEEPBAG -> CampingItems.SHEEPBAG;
            case GOODYBAG -> CampingItems.GOODYBAG;
        };
    }

    private void dropBlockWithContents(Level level, BlockPos pos, BackpackBlockEntity be) {
        ItemStack stack = new ItemStack(getBackpackItem());
        be.saveToItem(stack, level.registryAccess());
        if (be.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, be.getCustomName());
        }
        ItemEntity ie = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        ie.setDefaultPickUpDelay();
        level.addFreshEntity(ie);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isSpectator()) return InteractionResult.CONSUME;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof BackpackBlockEntity backpack) {
            if (player.isShiftKeyDown()) {
                level.destroyBlock(pos, false);
                return InteractionResult.CONSUME;
            }
            player.openMenu(backpack);
            player.awardStat(Stats.OPEN_CHEST);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    private static Map<Direction, VoxelShape> generateShapes(Supplier<VoxelShape> supplier) {
        Map<Direction, VoxelShape> map = new HashMap<>();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            map.put(dir, CampingUtil.rotateShape(Direction.NORTH, dir, supplier.get()));
        }
        return map;
    }

    static {
        SHAPES = new HashMap<>();
        SHAPES.put(BackpackVariant.SMALL_BACKPACK, generateShapes(BackpackBlockShapes.SMALL_BACKPACK));
        SHAPES.put(BackpackVariant.LARGE_BACKPACK, generateShapes(BackpackBlockShapes.LARGE_BACKPACK));
        SHAPES.put(BackpackVariant.WANDERER_BACKPACK, generateShapes(BackpackBlockShapes.WANDERER_BACKPACK));
        SHAPES.put(BackpackVariant.WANDERER_BAG, generateShapes(BackpackBlockShapes.WANDERER_BAG));
        SHAPES.put(BackpackVariant.GOODYBAG, generateShapes(BackpackBlockShapes.GOODYBAG));
        SHAPES.put(BackpackVariant.SHEEPBAG, generateShapes(BackpackBlockShapes.SHEEPBAG));
    }
}
