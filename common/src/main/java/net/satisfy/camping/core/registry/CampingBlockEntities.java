package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.block.entity.*;
import net.satisfy.camping.platform.Services;

import java.util.function.BiConsumer;

public class CampingBlockEntities {

    public static final BlockEntityType<GrillBlockEntity> GRILL = Services.REGISTER.blockEntity(GrillBlockEntity::new, CampingBlocks.GRILL);
    public static final BlockEntityType<TurnedCampfireBlockEntity> TURNED_CAMPFIRE = Services.REGISTER.blockEntity(TurnedCampfireBlockEntity::new, CampingBlocks.TURNED_CAMPFIRE);
    public static final BlockEntityType<StickCampfireBlockEntity> STICK_CAMPFIRE = Services.REGISTER.blockEntity(StickCampfireBlockEntity::new, CampingBlocks.STICK_CAMPFIRE);
    public static final BlockEntityType<EnderpackBlockEntity> ENDERPACK = Services.REGISTER.blockEntity(EnderpackBlockEntity::new, CampingBlocks.ENDERPACK, CampingBlocks.ENDERBAG);
    public static final BlockEntityType<BackpackBlockEntity> BACKPACK = Services.REGISTER.blockEntity(BackpackBlockEntity::new, CampingBlocks.GOODYBAG, CampingBlocks.LARGE_BACKPACK, CampingBlocks.SHEEPBAG, CampingBlocks.SMALL_BACKPACK, CampingBlocks.WANDERER_BACKPACK, CampingBlocks.WANDERER_BAG);

    public static void register(BiConsumer<BlockEntityType<?>, ResourceLocation> consumer) {
        consumer.accept(GRILL, Camping.identifier("grill"));
        consumer.accept(STICK_CAMPFIRE, Camping.identifier("stick_campfire"));
        consumer.accept(TURNED_CAMPFIRE, Camping.identifier("turned_campfire"));
        consumer.accept(ENDERPACK, Camping.identifier("enderpack"));
        consumer.accept(BACKPACK, Camping.identifier("backpack"));
    }
}
