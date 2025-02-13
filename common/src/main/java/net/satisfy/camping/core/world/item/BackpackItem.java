package net.satisfy.camping.core.world.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.core.world.block.BackpackBlock;
import net.satisfy.camping.core.world.block.BackpackType;


public class BackpackItem extends BlockItem implements Equipable {

    private final ResourceLocation backpackTexture;
    public final BackpackType backpackType;

    public BackpackItem(Block block, ResourceLocation backpackTexture) {
        super(block, new Properties().fireResistant().stacksTo(1));

        this.backpackType = ((BackpackBlock) block).getBackpackType();
        this.backpackTexture = backpackTexture;
    }

    public ResourceLocation getBackpackTexture() {
        return backpackTexture;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public SoundEvent getEquipSound() {
        return ArmorMaterials.LEATHER.getEquipSound();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
    }
}
