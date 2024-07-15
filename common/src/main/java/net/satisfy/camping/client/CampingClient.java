package net.satisfy.camping.client;


import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.satisfy.camping.client.render.GrillRenderer;
import net.satisfy.camping.registry.EntityTypeRegistry;


@Environment(EnvType.CLIENT)
public class CampingClient {

    public static void onInitializeClient() {
        BlockEntityRendererRegistry.register(EntityTypeRegistry.GRILL_BLOCK_ENTITY.get(), GrillRenderer::new);
    }

    public static void preInitClient() {
        registerEntityModelLayer();
        registerEntityRenderers();
    }

    public static void registerEntityRenderers() {

    }

    public static void registerEntityModelLayer() {
     }
}
