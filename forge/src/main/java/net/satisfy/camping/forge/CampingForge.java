package net.satisfy.camping.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.camping.Camping;
import net.satisfy.camping.block.SleepingBagBlock;
import net.satisfy.camping.forge.integration.CuriosBackpack;
import net.satisfy.camping.forge.integration.CuriosSmallBackpackRenderer;
import net.satisfy.camping.registry.ObjectRegistry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(Camping.MODID)
public class CampingForge {
    public CampingForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Camping.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        Camping.init();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::clientSetup);

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
    }

    private void setup(final FMLCommonSetupEvent evt) {
        CuriosApi.registerCurio(ObjectRegistry.SMALL_BACKPACK_ITEM.get(), new CuriosBackpack());
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(ObjectRegistry.SMALL_BACKPACK_ITEM.get(), CuriosSmallBackpackRenderer::new);
    }

    @Mod.EventBusSubscriber(modid = Camping.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
}
