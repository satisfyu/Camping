package net.satisfy.camping.client;


import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.client.screen.BackpackScreen;
import net.satisfy.camping.client.renderer.GrillRenderer;
import net.satisfy.camping.registry.EntityTypeRegistry;
import net.satisfy.camping.registry.ScreenhandlerTypeRegistry;

import java.util.stream.Stream;

import static net.satisfy.camping.registry.ObjectRegistry.*;


@Environment(EnvType.CLIENT)
public class CampingClient {

    public static void onInitializeClient() {
        for (RegistrySupplier<Block> block : Stream.concat(TENT_MAIN.values().stream(), Stream.concat(TENT_MAIN_HEAD.values().stream(), Stream.concat(TENT_RIGHT.values().stream(), TENT_HEAD_RIGHT.values().stream()))).toList()) {
            RenderTypeRegistry.register(RenderType.cutout(), block.get());}

        MenuRegistry.registerScreenFactory(ScreenhandlerTypeRegistry.BACKPACK_SCREENHANDLER.get(), BackpackScreen::new);

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
