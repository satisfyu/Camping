package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.core.world.block.entity.EnderpackBlockEntity;
import net.satisfy.camping.core.world.block.entity.GrillBlockEntity;
import net.satisfy.camping.platform.PlatformHelper;

import java.util.function.BiConsumer;

public class CampingBlockEntities {

    public static final BlockEntityType<GrillBlockEntity> GRILL = PlatformHelper.createBlockEntityType(GrillBlockEntity::new, CampingBlocks.GRILL);
    public static final BlockEntityType<BackpackBlockEntity> BACKPACK = PlatformHelper.createBlockEntityType(BackpackBlockEntity::new, CampingBlocks.BACKPACKS);
    public static final BlockEntityType<EnderpackBlockEntity> ENDERPACK = PlatformHelper.createBlockEntityType(EnderpackBlockEntity::new, CampingBlocks.ENDERPACKS);

    public static void register(BiConsumer<BlockEntityType<?>, ResourceLocation> consumer) {
        consumer.accept(GRILL, Camping.identifier("grill"));
        consumer.accept(BACKPACK, Camping.identifier("backpack"));
        consumer.accept(ENDERPACK, Camping.identifier("enderpack"));
    }
}
