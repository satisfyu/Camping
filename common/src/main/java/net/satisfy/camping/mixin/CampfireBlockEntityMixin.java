package net.satisfy.camping.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

    /**
     * {@link CampfireBlockEntity#cookTick} is called on the server every tick when the campfire is lit.
     */
    @Inject(method = "cookTick", at = @At("HEAD"))
    private static void camping$cookTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {

        // get a source of random values
        final RandomSource random = level.getRandom();

        // lower chance of occurring (1 in 200 chance per tick, or every 10 ticks/half a second on average)
        if (random.nextInt(200) != 1) return;

        // if doFireTick is not enabled, return early
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) return;

        // generate a count of positions to potentially ignite
        int ignitingCount = random.nextInt(3);

        // if the count of potential positions to ignite is not 0 or negative
        if (ignitingCount > 0) {

            BlockPos ignitingPos = pos;

            // for our sealed range of our potential positions
            for(int i = 0; i < ignitingCount; ++i) {

                // select a new position
                ignitingPos = ignitingPos.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);

                // if the position is not loaded, return early
                if (!level.isLoaded(ignitingPos)) return;

                // get the stat of the new position
                BlockState ignitingState = level.getBlockState(ignitingPos);

                // if the state is empty, fill it with fire
                if (ignitingState.isAir()) {
                    if (camping$hasFlammableNeighbours(level, ignitingPos)) {
                        level.setBlockAndUpdate(ignitingPos, BaseFireBlock.getState(level, ignitingPos));
                        return;
                    }
                }

                // if we cannot assign fire, return early
                else if (ignitingState.blocksMotion()) return;
            }
        }

        // potential positions to ignite were 0 or negative
        else {

            // for the range of 0, 1, 2, 3
            for(int chances = 0; chances < 3; ++chances) {

                // get a new position
                BlockPos randomPos = pos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);

                // if the position is not loaded, return early
                if (!level.isLoaded(randomPos)) return;

                // if the position has a flammable with an empty space above it
                if (level.isEmptyBlock(randomPos.above()) && camping$isFlammable(level, randomPos)) {

                    // fill the space with fire
                    level.setBlockAndUpdate(randomPos.above(), BaseFireBlock.getState(level, randomPos));
                }
            }
        }

    }

    @Unique
    private static boolean camping$hasFlammableNeighbours(LevelReader $$0, BlockPos $$1) {
        for(Direction $$2 : Direction.values()) {
            if (camping$isFlammable($$0, $$1.relative($$2))) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private static boolean camping$isFlammable(LevelReader $$0, BlockPos $$1) {
        return $$1.getY() >= $$0.getMinBuildHeight() && $$1.getY() < $$0.getMaxBuildHeight() && !$$0.hasChunkAt($$1) ? false : $$0.getBlockState($$1).ignitedByLava();
    }
}
