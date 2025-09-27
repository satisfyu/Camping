package net.satisfy.camping;

import net.satisfy.camping.client.world.block.renderer.GrillRenderer;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.platform.Services;

public class CampingClient {
    public static void init() {
        Services.REGISTER.blockEntityRenderer(CampingBlockEntities.GRILL, GrillRenderer::new);
       }
}
