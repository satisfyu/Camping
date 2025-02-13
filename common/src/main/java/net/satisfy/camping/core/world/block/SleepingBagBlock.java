package net.satisfy.camping.core.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;



import java.util.List;

import static net.satisfy.camping.core.util.CampingUtil.rotateShape;

@SuppressWarnings("deprecation")
public class SleepingBagBlock extends BedBlock {
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    public static final BooleanProperty CAN_DROP = BlockStateProperties.CONDITIONAL;
    protected static final VoxelShape SLEEPING_BAG_SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.25, 1.0D);

    public SleepingBagBlock(DyeColor color, Properties properties) {
        super(color, properties.forceSolidOn());
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(OCCUPIED, Boolean.FALSE).setValue(CAN_DROP, Boolean.TRUE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(PART) == BedPart.HEAD ? rotateShape(Direction.NORTH, state.getValue(FACING), SLEEPING_BAG_SHAPE) : SLEEPING_BAG_SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (state.getValue(PART) != BedPart.HEAD) {
                pos = pos.relative(state.getValue(FACING));
                state = level.getBlockState(pos);

                if (!state.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (state.getValue(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(level, pos)) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return InteractionResult.SUCCESS;
            } else {
                player.startSleepInBed(pos).ifLeft((failureReason) -> {
                    if (failureReason.getMessage() != null) {
                        player.displayClientMessage(failureReason.getMessage(), true);
                    }
                }).ifRight((success) -> player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1))); // todo make this value configurable
                return InteractionResult.SUCCESS;
            }
        }
    }


    private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
        List<Villager> villagers = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if (villagers.isEmpty()) {
            return false;
        } else {
            villagers.get(0).stopSleeping();
            return true;
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float factor) {
        super.fallOn(level, state, pos, entity, factor * 0.5F);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(getter, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 deltaMovement = entity.getDeltaMovement();
        if (deltaMovement.y < 0.0D) {
            double bounceFactor = entity instanceof LivingEntity ? 0.75D : 0.8D;
            entity.setDeltaMovement(deltaMovement.x, -deltaMovement.y * 0.3300000262260437D * bounceFactor, deltaMovement.z);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor accessor, BlockPos pos, BlockPos newPos) {
        if (direction == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            return newState.is(this) && newState.getValue(PART) != state.getValue(PART) ? state.setValue(OCCUPIED, newState.getValue(OCCUPIED)) : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, newState, accessor, pos, newPos);
        }
    }

    private static Direction getNeighbourDirection(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
    }

   
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos blockpos = context.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction);
        return context.getLevel().getBlockState(blockpos1).canBeReplaced(context) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED, CAN_DROP);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity livingEntity, ItemStack itemstack) {
        super.setPlacedBy(level, pos, state, livingEntity, itemstack);
        if (!level.isClientSide) {
            BlockPos headPos = pos.relative(state.getValue(FACING));
            level.setBlock(headPos, state.setValue(PART, BedPart.HEAD), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        BlockPos seedPos = pos.relative(state.getValue(FACING), state.getValue(PART) == BedPart.HEAD ? 0 : 1);
        return Mth.getSeed(seedPos.getX(), pos.getY(), seedPos.getZ());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (!state.getValue(CAN_DROP)) {
            return List.of();
        }
        return super.getDrops(state, builder);
    }

   
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
