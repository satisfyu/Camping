package net.satisfy.camping.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class MultitoolItem extends Item {
    private Mode mode = Mode.SHEARS;

    protected static final Map<Block, Block> STRIPPABLES;

    static {
        STRIPPABLES = (new ImmutableMap.Builder<Block, Block>()).put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
                .put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
                .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
                .put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD)
                .put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
                .put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
                .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
                .put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
                .put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
                .put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD)
                .put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG).put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK)
                .build();
    }

    public MultitoolItem(Properties properties) {
        super(properties);
    }

    public enum Mode {
        SHEARS, SAW, FLINT_AND_STEEL
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        return switch (mode) {
            case SHEARS -> useAsShears(context);
            case SAW -> useAsSaw(context);
            case FLINT_AND_STEEL -> useAsFlintAndSteel(context);
        };
    }

    private InteractionResult useAsShears(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        ItemStack itemstack = context.getItemInHand();
        BlockState blockstate = level.getBlockState(pos);
        Block block = blockstate.getBlock();

        if (block instanceof GrowingPlantHeadBlock growingplantheadblock) {
            if (!growingplantheadblock.isMaxAge(blockstate)) {
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, itemstack);
                }

                level.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
                BlockState blockstate1 = growingplantheadblock.getMaxAgeState(blockstate);
                level.setBlockAndUpdate(pos, blockstate1);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, Context.of(player, blockstate1));
                if (player != null) {
                    itemstack.hurtAndBreak(1, player, (arg2) -> arg2.broadcastBreakEvent(context.getHand()));
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else if (blockstate.is(BlockTags.LEAVES)) {
            if (!level.isClientSide) {
                level.destroyBlock(pos, true, player);
                assert player != null;
                itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult useAsSaw(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();
        BlockState blockstate = level.getBlockState(pos);

        Optional<BlockState> strippedState = getStrippedState(blockstate);
        Optional<BlockState> scrapedState = getScrapedState(blockstate);
        Optional<BlockState> waxedOffState = getWaxedOffState(blockstate);

        Optional<BlockState> newState = strippedState.isPresent() ? strippedState
                : scrapedState.isPresent() ? scrapedState
                : waxedOffState;

        if (newState.isPresent()) {
            level.playSound(player, pos, getSoundEvent(newState.get()), SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, newState.get(), 11);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, itemstack);
            }
            assert player != null;
            itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    private Optional<BlockState> getStrippedState(BlockState state) {
        Block block = STRIPPABLES.get(state.getBlock());
        return block != null ? Optional.of(block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS))) : Optional.empty();
    }

    private Optional<BlockState> getScrapedState(BlockState state) {
        if (state.is(Blocks.COPPER_BLOCK)) {
            return Optional.of(Blocks.EXPOSED_COPPER.defaultBlockState());
        } else if (state.is(Blocks.EXPOSED_COPPER)) {
            return Optional.of(Blocks.WEATHERED_COPPER.defaultBlockState());
        } else if (state.is(Blocks.WEATHERED_COPPER)) {
            return Optional.of(Blocks.OXIDIZED_COPPER.defaultBlockState());
        }
        return Optional.empty();
    }

    private Optional<BlockState> getWaxedOffState(BlockState state) {
        if (state.is(Blocks.WAXED_COPPER_BLOCK)) {
            return Optional.of(Blocks.COPPER_BLOCK.defaultBlockState());
        } else if (state.is(Blocks.WAXED_EXPOSED_COPPER)) {
            return Optional.of(Blocks.EXPOSED_COPPER.defaultBlockState());
        } else if (state.is(Blocks.WAXED_WEATHERED_COPPER)) {
            return Optional.of(Blocks.WEATHERED_COPPER.defaultBlockState());
        } else if (state.is(Blocks.WAXED_OXIDIZED_COPPER)) {
            return Optional.of(Blocks.OXIDIZED_COPPER.defaultBlockState());
        }
        return Optional.empty();
    }

    private SoundEvent getSoundEvent(BlockState state) {
        if (state.is(Blocks.EXPOSED_COPPER) || state.is(Blocks.WEATHERED_COPPER) || state.is(Blocks.OXIDIZED_COPPER)) {
            return SoundEvents.AXE_SCRAPE;
        } else if (state.is(Blocks.COPPER_BLOCK) || state.is(Blocks.EXPOSED_COPPER) || state.is(Blocks.WEATHERED_COPPER) || state.is(Blocks.OXIDIZED_COPPER)) {
            return SoundEvents.AXE_WAX_OFF;
        }
        return SoundEvents.AXE_STRIP;
    }


    private InteractionResult useAsFlintAndSteel(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (!CampfireBlock.canLight(blockState) && !CandleBlock.canLight(blockState) && !CandleCakeBlock.canLight(blockState)) {
            BlockPos blockPos2 = blockPos.relative(context.getClickedFace());
            if (BaseFireBlock.canBePlacedAt(level, blockPos2, context.getHorizontalDirection())) {
                level.playSound(player, blockPos2, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState blockState2 = BaseFireBlock.getState(level, blockPos2);
                level.setBlock(blockPos2, blockState2, 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
                ItemStack itemStack = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPos2, itemStack);
                    itemStack.hurtAndBreak(1, player, (arg2) -> arg2.broadcastBreakEvent(context.getHand()));
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(blockPos, blockState.setValue(BlockStateProperties.LIT, true), 11);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player, (arg2) -> arg2.broadcastBreakEvent(context.getHand()));
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            mode = Mode.values()[(mode.ordinal() + 1) % Mode.values().length];
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("item.camping.multitool.mode." + mode.name().toLowerCase()), true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return super.use(level, player, hand);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (mode == Mode.SAW && state.is(BlockTags.LOGS)) {
            return 3.0F;
        } else if (mode == Mode.SHEARS && (state.is(BlockTags.LEAVES) || state.is(Blocks.COBWEB) || state.is(BlockTags.WOOL) || state.is(Blocks.VINE) || state.is(Blocks.GLOW_LICHEN))) {
            return 15.0F;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!world.isClientSide && !state.is(BlockTags.FIRE) && mode == Mode.SHEARS) {
            stack.hurtAndBreak(1, entityLiving, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return state.is(BlockTags.LEAVES) || state.is(Blocks.COBWEB) || state.is(Blocks.GRASS) || state.is(Blocks.FERN) || state.is(Blocks.DEAD_BUSH) || state.is(Blocks.HANGING_ROOTS) || state.is(Blocks.VINE) || state.is(Blocks.TRIPWIRE) || state.is(BlockTags.WOOL) || super.mineBlock(stack, world, state, pos, entityLiving);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return mode == Mode.SHEARS && (state.is(Blocks.COBWEB) || state.is(Blocks.REDSTONE_WIRE) || state.is(Blocks.TRIPWIRE));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (mode == Mode.SHEARS && entity instanceof net.minecraft.world.entity.animal.Sheep sheep) {
            if (!entity.level().isClientSide) {
                if (sheep.readyForShearing()) {
                    sheep.setSheared(true);
                    int dropCount = 1 + player.getRandom().nextInt(3);
                    for (int i = 0; i < dropCount; ++i) {
                        ItemStack woolStack = new ItemStack(getWoolItem(sheep.getColor()));
                        ItemEntity itemEntity = sheep.spawnAtLocation(woolStack, 1.0F);
                        assert itemEntity != null;
                        itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(
                                (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.1F,
                                player.getRandom().nextFloat() * 0.05F,
                                (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.1F
                        ));
                    }
                    stack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(hand));
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    private Item getWoolItem(DyeColor color) {
        return switch (color) {
            case ORANGE -> Items.ORANGE_WOOL;
            case MAGENTA -> Items.MAGENTA_WOOL;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_WOOL;
            case YELLOW -> Items.YELLOW_WOOL;
            case LIME -> Items.LIME_WOOL;
            case PINK -> Items.PINK_WOOL;
            case GRAY -> Items.GRAY_WOOL;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_WOOL;
            case CYAN -> Items.CYAN_WOOL;
            case PURPLE -> Items.PURPLE_WOOL;
            case BLUE -> Items.BLUE_WOOL;
            case BROWN -> Items.BROWN_WOOL;
            case GREEN -> Items.GREEN_WOOL;
            case RED -> Items.RED_WOOL;
            case BLACK -> Items.BLACK_WOOL;
            default -> Items.WHITE_WOOL;
        };
    }
}
