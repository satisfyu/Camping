package net.satisfy.camping.client.world.block.renderer;

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
import net.satisfy.camping.core.world.block.TurnedCampfireBlock;
import net.satisfy.camping.core.world.block.entity.TurnedCampfireBlockEntity;

public class TurnedCampfireRenderer implements BlockEntityRenderer<TurnedCampfireBlockEntity> {
    private static final float SIZE = 0.375F;
    private final ItemRenderer itemRenderer;

    public TurnedCampfireRenderer(BlockEntityRendererProvider.Context $$0) {
        super();
        this.itemRenderer = $$0.getItemRenderer();
    }

    public static final float translateX = 0.0625f * 8f;
    public static final float translateZ = -0.0625f * 4f;
    public void render(TurnedCampfireBlockEntity $$0, float $$1, PoseStack stack, MultiBufferSource $$3, int $$4, int $$5) {
        Direction $$6 = (Direction)$$0.getBlockState().getValue(TurnedCampfireBlock.FACING);
        NonNullList<ItemStack> $$7 = $$0.getItems();
        int $$8 = (int)$$0.getBlockPos().asLong();

        stack.translate(translateX, 0, translateZ);
        stack.mulPose(Axis.YN.rotationDegrees(45));

        for(int $$9 = 0; $$9 < $$7.size(); ++$$9) {
            ItemStack $$10 = (ItemStack)$$7.get($$9);
            if ($$10 != ItemStack.EMPTY) {
                stack.pushPose();

                /// we are rotating each item stack???
                // test 1
//                stack.rotateAround(Axis.YN.rotationDegrees(45.0f), 0, 0, 0); /// ???????????????? rotate on Y axis in negative direction 45 degrees, with respect to origin 0,8,0
                stack.translate(0.5F, 0.44921875F, 0.5F);
                Direction $$11 = Direction.from2DDataValue(($$9 + $$6.get2DDataValue()) % 4);
                float $$12 = -$$11.toYRot();
                stack.mulPose(Axis.YP.rotationDegrees($$12));
                stack.mulPose(Axis.XP.rotationDegrees(90.0F));
                stack.translate(-0.3125F, -0.3125F, 0.0F);
                stack.scale(0.375F, 0.375F, 0.375F);

                // test 2
//                stack.mulPose(Axis.YN.rotationDegrees(45));
                this.itemRenderer.renderStatic($$10, ItemDisplayContext.FIXED, $$4, $$5, stack, $$3, $$0.getLevel(), $$8 + $$9);
                stack.popPose();
            }
        }

    }

    // $FF: synthetic method
    // $FF: bridge method
//    public void render(BlockEntity var1, float var2, PoseStack var3, MultiBufferSource var4, int var5, int var6) {
//        this.render((CampfireBlockEntity)var1, var2, var3, var4, var5, var6);
//    }
}

