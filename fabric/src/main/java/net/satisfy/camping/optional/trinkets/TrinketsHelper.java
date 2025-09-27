package net.satisfy.camping.optional.trinkets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
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
            List<Tuple<SlotReference, ItemStack>> trinketList;

            trinketList = trinketComponent.getEquipped(CampingItems.SMALL_BACKPACK);
            if (!trinketList.isEmpty()) returned.set(trinketList.get(0).getB());

            trinketList = trinketComponent.getEquipped(CampingItems.LARGE_BACKPACK);
            if (!trinketList.isEmpty()) returned.set(trinketList.get(0).getB());

            trinketList = trinketComponent.getEquipped(CampingItems.WANDERER_BACKPACK);
            if (!trinketList.isEmpty()) returned.set(trinketList.get(0).getB());

            trinketList = trinketComponent.getEquipped(CampingItems.WANDERER_BAG);
            if (!trinketList.isEmpty()) returned.set(trinketList.get(0).getB());

            trinketList = trinketComponent.getEquipped(CampingItems.SHEEPBAG);
            if (!trinketList.isEmpty()) returned.set(trinketList.get(0).getB());

            trinketList = trinketComponent.getEquipped(CampingItems.GOODYBAG);
            if (!trinketList.isEmpty()) returned.set(trinketList.get(0).getB());

            trinketList = trinketComponent.getEquipped(CampingItems.ENDERPACK);
            if (!trinketList.isEmpty()) returned.set(trinketList.get(0).getB());

            trinketList = trinketComponent.getEquipped(CampingItems.ENDERBAG);
            if (!trinketList.isEmpty()) returned.set(trinketList.get(0).getB());
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

    public static class BackpackTrinketRenderer implements TrinketRenderer {

        @Override
        public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity, float v, float v1, float v2, float v3, float v4, float v5) {
            BackpackBlockItem backpack = (BackpackBlockItem) itemStack.getItem();
            Model m = BackpackRegistry.getBodyModel(backpack, ((HumanoidModel<?>) entityModel).body);
            ModelPart part;
            if (m instanceof HumanoidModel<?> hm) {
                part = hm.body;
            } else if (m instanceof HierarchicalModel<?> h) {
                part = h.root();
            } else {
                return;
            }
            BackpackRenderLayer.performTranslations(poseStack, backpack.variant, livingEntity.isCrouching());
            VertexConsumer vc = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(backpack.getTexture()));
            part.render(poseStack, vc, i, OverlayTexture.NO_OVERLAY);
        }
    }

    public static class EnderpackTrinketRenderer implements TrinketRenderer {

        @Override
        public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity entity, float v, float v1, float v2, float v3, float v4, float v5) {
            EnderpackBlockItem enderpack = (EnderpackBlockItem) itemStack.getItem();
            Model m = BackpackRegistry.getBodyModel(enderpack, ((HumanoidModel<?>) entityModel).body);

            boolean isEnderBag = enderpack == CampingItems.ENDERBAG;
            boolean isEnderPack = enderpack == CampingItems.ENDERPACK;

            poseStack.pushPose();
            if (isEnderBag) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);
            if (isEnderPack) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);
            if (entity.isCrouching()) poseStack.translate(0, -0.0625f - (0.0625f / 8f), (0.0625f) / 10.0f);

            ModelPart part;
            if (m instanceof HumanoidModel<?> hm) {
                part = hm.body;
            } else if (m instanceof HierarchicalModel<?> h) {
                part = h.root();
            } else {
                poseStack.popPose();
                return;
            }

            VertexConsumer vc = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(enderpack.getTexture()));
            part.render(poseStack, vc, i, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}
