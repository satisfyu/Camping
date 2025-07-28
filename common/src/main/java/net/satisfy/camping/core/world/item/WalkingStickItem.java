package net.satisfy.camping.core.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.util.CampingTiers;

public class WalkingStickItem extends TieredItem {

    private static final float ATTACK_DAMAGE = 1.0F;
    private static final float ATTACK_SPEED = -2.0F;

    public WalkingStickItem(Properties properties) {
        super(CampingTiers.STICK, properties.attributes(
                ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, ATTACK_DAMAGE, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                        .build()
        ));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        if (!isSelected || !(entity instanceof Player player)) return;
        if (!player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 0, false, false, false));
        }
        if (player.isHurt() && !player.getCooldowns().isOnCooldown(CampingItems.WALKING_STICK)) {
            player.getCooldowns().addCooldown(CampingItems.WALKING_STICK, 100);
        }
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) return 15.0F;
        else return state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity attacker, LivingEntity defender) {
        stack.hurtAndBreak(1, defender, EquipmentSlot.MAINHAND);
        return true;
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(2, miner, EquipmentSlot.MAINHAND);
        }

        return true;
    }
}
