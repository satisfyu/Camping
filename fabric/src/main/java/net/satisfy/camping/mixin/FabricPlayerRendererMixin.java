package net.satisfy.camping.mixin;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.satisfy.camping.client.renderer.entity.layers.BackpackRenderLayer;
import net.satisfy.camping.client.renderer.entity.layers.EnderpackRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class FabricPlayerRendererMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void camping_$_init(EntityRendererProvider.Context context, boolean useSlimModel, CallbackInfo ci) {
        PlayerRenderer instance = (PlayerRenderer) (Object) this;
        instance.addLayer(new EnderpackRenderLayer(instance));
        instance.addLayer(new BackpackRenderLayer(instance));
    }
}
