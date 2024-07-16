package net.satisfy.camping.platform.fabric;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.satisfy.camping.Util.CampingUtil;
import net.satisfy.camping.fabric.config.ConfigFabric;
import net.satisfy.camping.platform.PlatformHelper;

public class PlatformHelperImpl {
    public static void addRegenEffect(Player player, MobEffectInstance effectInstance) {
        if (ConfigFabric.enableEffect) {
            player.addEffect(effectInstance);
        }
    }

    public static void setGrilled(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean("Grilled", true);
        CampingUtil.Grilling.increaseFoodValue(itemStack);
    }
}
