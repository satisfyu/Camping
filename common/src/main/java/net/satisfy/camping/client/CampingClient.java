package net.satisfy.camping.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.client.model.*;
import net.satisfy.camping.client.screen.BackpackScreen;
import net.satisfy.camping.client.renderer.GrillRenderer;
import net.satisfy.camping.registry.EntityTypeRegistry;
import net.satisfy.camping.registry.KeyHandlerRegistry;
import net.satisfy.camping.registry.ScreenhandlerTypeRegistry;

import java.util.stream.Stream;

import static net.satisfy.camping.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class CampingClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(), GRILL.get());
        for (RegistrySupplier<Block> block : Stream.concat(TENT_MAIN.values().stream(), Stream.concat(TENT_MAIN_HEAD.values().stream(), Stream.concat(TENT_RIGHT.values().stream(), TENT_HEAD_RIGHT.values().stream()))).toList()) {
            RenderTypeRegistry.register(RenderType.cutout(), block.get());

        }

        KeyHandlerRegistry.init();
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
        EntityModelLayerRegistry.register(SmallBackpackModel.LAYER_LOCATION, SmallBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.register(LargeBackpackModel.LAYER_LOCATION, LargeBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.register(WandererBackpackModel.LAYER_LOCATION, WandererBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.register(WandererBagModel.LAYER_LOCATION, WandererBagModel::createBodyLayer);
        EntityModelLayerRegistry.register(GoodybagModel.LAYER_LOCATION, GoodybagModel::createBodyLayer);
        EntityModelLayerRegistry.register(SheepbagModel.LAYER_LOCATION, SheepbagModel::createBodyLayer);
        EntityModelLayerRegistry.register(EnderpackModel.LAYER_LOCATION, EnderpackModel::createBodyLayer);
        EntityModelLayerRegistry.register(EnderbagModel.LAYER_LOCATION, EnderbagModel::createBodyLayer);

    }
}
