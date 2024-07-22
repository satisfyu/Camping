package net.satisfy.camping.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.camping.Camping;
import net.satisfy.camping.Util.CampingIdentifier;
import net.satisfy.camping.block.entity.GrillBlockEntity;
import net.satisfy.camping.block.entity.BackpackBlockEntity;

import java.util.function.Supplier;

public final class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Camping.MODID, Registries.ENTITY_TYPE);
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Camping.MODID, Registries.BLOCK_ENTITY_TYPE).getRegistrar();

    public static final RegistrySupplier<BlockEntityType<GrillBlockEntity>> GRILL_BLOCK_ENTITY = registerBlockEntity("grill", () -> BlockEntityType.Builder.of(GrillBlockEntity::new, ObjectRegistry.GRILL.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<BackpackBlockEntity>> BACKPACK_BLOCK_ENTITY = registerBlockEntity("backpack", () -> BlockEntityType.Builder.of(BackpackBlockEntity::new, ObjectRegistry.SMALL_BACKPACK.get()).build(null));


    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(new CampingIdentifier(path), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntity(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(path, type);
    }

    public static void registerAttributes() {
    }

    static {
        ENTITY_TYPES.register();
        registerAttributes();
    }
}