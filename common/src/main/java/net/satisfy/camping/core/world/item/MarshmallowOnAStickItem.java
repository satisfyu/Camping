package net.satisfy.camping.core.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.satisfy.camping.core.registry.CampingItems;
import org.jetbrains.annotations.NotNull;

public class MarshmallowOnAStickItem extends Item {

    private final boolean roasted;

    public MarshmallowOnAStickItem(boolean roasted, Properties properties) {
        super(properties);
        this.roasted = roasted;
    }

    public @NotNull ItemStack finishUsingItem(ItemStack usedStack, Level level, LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(usedStack, level, entity);
        return entity instanceof Player player && player.getAbilities().instabuild ? itemstack : new ItemStack(Items.STICK);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        if (!(level.getBlockState(clickedPos).getBlock() instanceof CampfireBlock)) return super.useOn(context);

        if (level.isClientSide()) level.playLocalSound(clickedPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1, 1, false);

        if (!level.isClientSide() && context.getPlayer() != null) {
            context.getPlayer().setItemInHand(context.getHand(), new ItemStack(this.roasted ? Items.CHARCOAL : CampingItems.ROASTED_MARSHMALLOW_ON_A_STICK));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.isCrouching()) return super.use(level, player, hand);

        player.setItemInHand(hand, new ItemStack(this.roasted ? CampingItems.ROASTED_MARSHMALLOW : CampingItems.MARSHMALLOW));
        player.addItem(new ItemStack(Items.STICK));

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
