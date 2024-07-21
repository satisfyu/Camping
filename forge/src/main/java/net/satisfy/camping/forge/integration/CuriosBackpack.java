package net.satisfy.camping.forge.integration;

import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CuriosBackpack implements ICurioItem {
    @Override
    public boolean canEquipFromUse(SlotContext context, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canSync(SlotContext context, ItemStack stack)
    {
        return true;
    }

}