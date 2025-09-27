package net.satisfy.camping.platform;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.satisfy.camping.platform.services.IRegisterHelper;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class NeoForgeRegisterHelper implements IRegisterHelper {
    private IEventBus bus;

    public NeoForgeRegisterHelper() {}

    public NeoForgeRegisterHelper(IEventBus bus) {
        this.bus = bus;
    }

    private IEventBus getBus() {
        return bus != null ? bus : ModLoadingContext.get().getActiveContainer().getEventBus();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntity(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        return BlockEntityType.Builder.of(func::apply, blocks).build(null);
    }

    @Override
    public <T extends BlockEntity> void blockEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>> rendererConstructor) {
        getBus().addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
            event.registerBlockEntityRenderer(type, rendererConstructor::apply);
        });
    }

    @Override
    public <T extends Entity> EntityType<T> entity(BiFunction<EntityType<T>, Level, T> func, MobCategory mobCategory) {
        return EntityType.Builder.of(func::apply, mobCategory).build(null);
    }

    @Override
    public <T extends Entity> void entityRenderer(EntityType<T> type, Function<EntityRendererProvider.Context, EntityRenderer<T>> rendererConstructor) {
        getBus().addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
            event.registerEntityRenderer(type, rendererConstructor::apply);
        });
    }
}
