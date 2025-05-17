package net.satisfy.camping.platform;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.client.renderer.entity.MosquitoRenderer;
import net.satisfy.camping.core.registry.CampingEntities;
import net.satisfy.camping.platform.services.IRegisterHelper;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FabricRegisterHelper implements IRegisterHelper {

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntity(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(func::apply, blocks).build();
    }

    @Override
    public <T extends BlockEntity> void blockEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>> rendererConstructor) {
        BlockEntityRenderers.register(type, rendererConstructor::apply);
    }

    @Override
    public <T extends Entity> EntityType<T> entity(BiFunction<EntityType<T>, Level, T> func, MobCategory mobCategory) {
        return FabricEntityTypeBuilder.create(mobCategory, func::apply).build();
    }

    @Override
    public <T extends Entity> void entityRenderer(EntityType<T> type, Function<EntityRendererProvider.Context, EntityRenderer<T>> rendererConstructor) {
        EntityRendererRegistry.register(CampingEntities.MOSQUITO, MosquitoRenderer::new);
    }
}
