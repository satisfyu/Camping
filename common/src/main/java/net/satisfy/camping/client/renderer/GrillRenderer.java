package net.satisfy.camping.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.block.GrillBlock;
import net.satisfy.camping.block.entity.GrillBlockEntity;

public class GrillRenderer implements BlockEntityRenderer<GrillBlockEntity> {
    private static final float SIZE = 0.3F;
    private final ItemRenderer itemRenderer;

    public GrillRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(GrillBlockEntity grillBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        Direction direction = grillBlockEntity.getBlockState().getValue(GrillBlock.FACING);
        NonNullList<ItemStack> items = grillBlockEntity.getItems();
        int posLong = (int) grillBlockEntity.getBlockPos().asLong();

        for (int i = 0; i < items.size(); ++i) {
            ItemStack itemStack = items.get(i);
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5F, 1F, 0.5F);
                Direction itemDirection = Direction.from2DDataValue((i + direction.get2DDataValue()) % 4);
                float rotation = -itemDirection.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.translate(-0.15625F, -0.15625F, 0.0F);
                poseStack.scale(SIZE, SIZE, SIZE);
                this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, bufferSource, grillBlockEntity.getLevel(), posLong + i);
                poseStack.popPose();
            }
        }
    }
}
