package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.core.world.block.entity.EnderpackBlockEntity;
import net.satisfy.camping.core.world.block.entity.GrillBlockEntity;
import net.satisfy.camping.platform.Services;

import java.util.function.BiConsumer;

public class CampingBlockEntities {

    public static final BlockEntityType<GrillBlockEntity> GRILL = Services.PLATFORM.createBlockEntityType(GrillBlockEntity::new, CampingBlocks.GRILL);
    public static final BlockEntityType<BackpackBlockEntity> BACKPACK = Services.PLATFORM.createBlockEntityType(BackpackBlockEntity::new, CampingBlocks.BACKPACKS);
    public static final BlockEntityType<EnderpackBlockEntity> ENDERPACK = Services.PLATFORM.createBlockEntityType(EnderpackBlockEntity::new, CampingBlocks.ENDERPACKS);

    public static void register(BiConsumer<BlockEntityType<?>, ResourceLocation> consumer) {
        consumer.accept(GRILL, Camping.identifier("grill"));
        consumer.accept(BACKPACK, Camping.identifier("backpack"));
        consumer.accept(ENDERPACK, Camping.identifier("enderpack"));
    }
}
