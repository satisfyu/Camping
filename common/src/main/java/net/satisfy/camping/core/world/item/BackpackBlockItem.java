package net.satisfy.camping.core.world.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.core.util.BackpackVariant;
import org.jetbrains.annotations.NotNull;

public class BackpackBlockItem extends BlockItem implements Equipable {

    public final BackpackVariant variant;
    private final ResourceLocation texture;

    public BackpackBlockItem(Block block, BackpackVariant variant, ResourceLocation texture) {
        super(block, new Properties().fireResistant().stacksTo(1));
        this.variant = variant;
        this.texture = texture;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getNumberOfSlots() {
        return 8*3;
    }

    public int getNumberOfUpgradeSlots() {
        return 0;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public @NotNull Holder<SoundEvent> getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }}
