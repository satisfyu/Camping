package net.satisfy.camping.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.fabric.compat.trinkets.TrinketsCompatibility;
import net.satisfy.camping.fabric.core.config.ConfigFabric;
import net.satisfy.camping.fabric.core.network.CampingMessagesFabric;
import net.satisfy.camping.fabric.core.registry.RegistryFabric;
import net.satisfy.camping.core.world.block.SleepingBagBlock;

public class CampingFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Camping.init();
        AutoConfig.register(ConfigFabric.class, GsonConfigSerializer::new);
        RegistryFabric.register();
        //CampingMessagesFabric.registerC2SPackets();//TODO fixme

        if (FabricLoader.getInstance().isModLoaded("trinkets")) TrinketsCompatibility.load();

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, sleepingPos) -> {
            boolean onClient = player.level().isClientSide;
            BlockState blockState = player.level().getBlockState(sleepingPos);
            return !(!onClient && blockState.getBlock() instanceof SleepingBagBlock);
        });
        UseItemCallback.EVENT.register((player, level, interactionHand) -> {
            if (!CampingUtil.Grilling.isGrilled(player.getItemInHand(interactionHand)) || !ConfigFabric.enableGrilling) {
                return InteractionResultHolder.success(player.getItemInHand(interactionHand));
            }

            FoodProperties foodProperties = player.getItemInHand(interactionHand).get(DataComponents.FOOD);
            CampingUtil.Grilling.FoodValue additionalFoodValues = CampingUtil.Grilling.getAdditionalFoodValue(player.getItemInHand(interactionHand));

            if (foodProperties != null) {
                int nutrition = (int) (foodProperties.nutrition() * 1.25) + additionalFoodValues.nutrition();
                float saturation = foodProperties.saturation() * 1.25F + additionalFoodValues.saturationModifier();
                foodProperties.nutrition = nutrition;
                foodProperties.saturation = saturation;
            }
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        });
    }
}
