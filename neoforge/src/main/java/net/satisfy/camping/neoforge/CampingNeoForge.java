package net.satisfy.camping.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.satisfy.camping.Camping;
import net.satisfy.camping.Constants;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.neoforge.core.crafting.CapabilityBackpackWrapper;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.neoforge.core.registry.RegistryNeoForge;
import net.satisfy.camping.core.world.block.SleepingBagBlock;
import net.satisfy.camping.neoforge.integration.CuriosBackpack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(Constants.MOD_ID)
public class CampingNeoForge {

    public static IEventBus EVENT_BUS = null;

    public CampingNeoForge(final IEventBus modEventbus, ModContainer modContainer) {
        EventBusesHooks.whenAvailable(Constants.MOD_ID, IEventBus::start);
        Camping.init();
        CampingNeoForge.EVENT_BUS = modEventbus;
        RegistryNeoForge.register(CampingNeoForge.EVENT_BUS);
        CampingNeoForge.EVENT_BUS.addListener(this::setup);
        CampingNeoForge.EVENT_BUS.addListener(this::enqueueIMC);
        CampingNeoForge.EVENT_BUS.addListener(CapabilityBackpackWrapper::onRegister);
        NeoForge.EVENT_BUS.addListener(this::onFoodEating);
    }

    @SuppressWarnings("all")
    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
    }

    private void setup(final FMLCommonSetupEvent event) {
        CuriosApi.registerCurio(CampingItems.SMALL_BACKPACK, new CuriosBackpack());
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class ForgeEventsHandler {

        @SubscribeEvent
        public static void playerSetSpawn(PlayerSetSpawnEvent event)
        {
            Level level = event.getEntity().level();

            if(event.getNewSpawn() != null)
            {
                Block block = level.getBlockState(event.getNewSpawn()).getBlock();

                if(!level.isClientSide && block instanceof SleepingBagBlock && !event.isForced())
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    private void onFoodEating(LivingEntityUseItemEvent event) {
        var player = event.getEntity();
        if (!CampingUtil.Grilling.isGrilled(player.getItemInHand(player.getUsedItemHand()))) {
            return;
        }

        FoodProperties foodProperties = player.getItemInHand(player.getUsedItemHand()).get(DataComponents.FOOD);
        CampingUtil.Grilling.FoodValue additionalFoodValues = CampingUtil.Grilling.getAdditionalFoodValue(player.getItemInHand(player.getUsedItemHand()));

        if (foodProperties != null) {
            int nutrition = (int) (foodProperties.nutrition() * 1.25) + additionalFoodValues.nutrition();
            float saturation = foodProperties.saturation() * 1.25F + additionalFoodValues.saturationModifier();
            foodProperties.nutrition = nutrition;
            foodProperties.saturation = saturation;
        }
    }
}