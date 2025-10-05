package net.satisfy.camping.optional.trinkets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.client.renderer.entity.layers.BackpackRenderLayer;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.util.BackpackRegistry;
import net.satisfy.camping.core.world.item.BackpackBlockItem;
import net.satisfy.camping.core.world.item.EnderpackBlockItem;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TrinketsHelper {

    public static void registerItemsAsTrinkets() {
        TrinketsApi.registerTrinket(CampingItems.SMALL_BACKPACK, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.LARGE_BACKPACK, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.WANDERER_BACKPACK, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.WANDERER_BAG, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.SHEEPBAG, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.GOODYBAG, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.ENDERBAG, new BackpackTrinket());
        TrinketsApi.registerTrinket(CampingItems.ENDERPACK, new BackpackTrinket());
    }

    public static ItemStack getBackpackFromTrinkets(Player player) {
        AtomicReference<ItemStack> returned = new AtomicReference<>(ItemStack.EMPTY);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            List<Tuple<SlotReference, ItemStack>> l;
            l = trinketComponent.getEquipped(CampingItems.SMALL_BACKPACK); if (!l.isEmpty()) returned.set(l.get(0).getB());
            l = trinketComponent.getEquipped(CampingItems.LARGE_BACKPACK); if (!l.isEmpty()) returned.set(l.get(0).getB());
            l = trinketComponent.getEquipped(CampingItems.WANDERER_BACKPACK); if (!l.isEmpty()) returned.set(l.get(0).getB());
            l = trinketComponent.getEquipped(CampingItems.WANDERER_BAG); if (!l.isEmpty()) returned.set(l.get(0).getB());
            l = trinketComponent.getEquipped(CampingItems.SHEEPBAG); if (!l.isEmpty()) returned.set(l.get(0).getB());
            l = trinketComponent.getEquipped(CampingItems.GOODYBAG); if (!l.isEmpty()) returned.set(l.get(0).getB());
            l = trinketComponent.getEquipped(CampingItems.ENDERPACK); if (!l.isEmpty()) returned.set(l.get(0).getB());
            l = trinketComponent.getEquipped(CampingItems.ENDERBAG); if (!l.isEmpty()) returned.set(l.get(0).getB());
        });
        return returned.get();
    }

    public static void registerRenderersForTrinkets() {
        TrinketRendererRegistry.registerRenderer(CampingItems.SMALL_BACKPACK, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.LARGE_BACKPACK, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.WANDERER_BACKPACK, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.WANDERER_BAG, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.SHEEPBAG, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.GOODYBAG, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.ENDERPACK, new EnderpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.ENDERBAG, new EnderpackTrinketRenderer());
    }

    public static Optional<Tuple<SlotReference, ItemStack>> getEquippedBackpack(Player player) {
        AtomicReference<Tuple<SlotReference, ItemStack>> out = new AtomicReference<>(null);
        TrinketsApi.getTrinketComponent(player).ifPresent(tc -> {
            List<Tuple<SlotReference, ItemStack>> l;
            l = tc.getEquipped(CampingItems.SMALL_BACKPACK); if (!l.isEmpty()) { out.set(l.get(0)); return; }
            l = tc.getEquipped(CampingItems.LARGE_BACKPACK); if (!l.isEmpty()) { out.set(l.get(0)); return; }
            l = tc.getEquipped(CampingItems.WANDERER_BACKPACK); if (!l.isEmpty()) { out.set(l.get(0)); return; }
            l = tc.getEquipped(CampingItems.WANDERER_BAG); if (!l.isEmpty()) { out.set(l.get(0)); return; }
            l = tc.getEquipped(CampingItems.SHEEPBAG); if (!l.isEmpty()) { out.set(l.get(0)); return; }
            l = tc.getEquipped(CampingItems.GOODYBAG); if (!l.isEmpty()) { out.set(l.get(0)); return; }
            l = tc.getEquipped(CampingItems.ENDERPACK); if (!l.isEmpty()) { out.set(l.get(0)); return; }
            l = tc.getEquipped(CampingItems.ENDERBAG); if (!l.isEmpty()) { out.set(l.get(0)); }
        });
        return Optional.ofNullable(out.get());
    }

    public static class BackpackTrinketRenderer implements TrinketRenderer {

        @Override
        public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(entityModel instanceof HumanoidModel<?> hm)) return;
            if (!(itemStack.getItem() instanceof BackpackBlockItem backpack)) return;
            Model model = BackpackRegistry.getBodyModel(backpack, hm.body);
            poseStack.pushPose();
            BackpackRenderLayer.performTranslations(poseStack, backpack.variant, entity.isCrouching());
            VertexConsumer vc = multiBufferSource.getBuffer(model.renderType(backpack.getTexture()));
            model.renderToBuffer(poseStack, vc, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            poseStack.popPose();
        }
    }

    public static class EnderpackTrinketRenderer implements TrinketRenderer {

        @Override
        public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(entityModel instanceof HumanoidModel<?> hm)) return;
            if (!(itemStack.getItem() instanceof EnderpackBlockItem enderpack)) return;
            Model model = BackpackRegistry.getBodyModel(enderpack, hm.body);
            poseStack.pushPose();
            if (itemStack.is(CampingItems.ENDERBAG) || itemStack.is(CampingItems.ENDERPACK)) {
                poseStack.translate(-0.3125f, 0, 0.125f);
            }
            if (entity.isCrouching()) {
                poseStack.translate(0, -0.0703125f, 0.00625f);
            }
            VertexConsumer vc = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(enderpack.getTexture()));
            model.renderToBuffer(poseStack, vc, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            poseStack.popPose();
        }
    }
}