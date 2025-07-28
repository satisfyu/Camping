package net.satisfy.camping.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class PlatformHelper {
    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    @ExpectPlatform
    public static String getPlatformName(){
        throw new AssertionError();
    }

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    @ExpectPlatform
    public static boolean isModLoaded(String modId){
        throw new AssertionError();
    }

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    @ExpectPlatform
    public static boolean isDevelopmentEnvironment() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    @ExpectPlatform
    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ItemStack getEquippedBackpack(Player player) {
        throw new AssertionError();
    }

}
