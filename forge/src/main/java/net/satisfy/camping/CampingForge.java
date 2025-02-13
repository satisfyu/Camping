package net.satisfy.camping;

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
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.registry.RegistryForge;
import net.satisfy.camping.core.world.block.SleepingBagBlock;
import net.satisfy.camping.integration.CuriosBackpack;
import net.satisfy.camping.integration.CuriosBackpackRenderer;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(Constants.MOD_ID)
public class CampingForge {

    public static IEventBus EVENT_BUS = null;

    public CampingForge() {
        Camping.init();
        CampingForge.EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryForge.register(CampingForge.EVENT_BUS);
        CampingForge.EVENT_BUS.addListener(this::setup);
        CampingForge.EVENT_BUS.addListener(this::enqueueIMC);
        CampingForge.EVENT_BUS.addListener(this::clientSetup);
    }

    @SuppressWarnings("all")
    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
    }

    private void setup(final FMLCommonSetupEvent evt) {
        CuriosApi.registerCurio(CampingItems.SMALL_BACKPACK, new CuriosBackpack());
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(
                CampingItems.SMALL_BACKPACK,
                () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.SMALL_BACKPACK)
        );
        CuriosRendererRegistry.register(
                CampingItems.ENDERPACK,
                () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.ENDERPACK)
        );
        CuriosRendererRegistry.register(
                CampingItems.ENDERBAG,
                () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.ENDERBAG)
        );
        CuriosRendererRegistry.register(
                CampingItems.GOODYBAG,
                () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.GOODYBAG)
        );
        CuriosRendererRegistry.register(
                CampingItems.LARGE_BACKPACK,
                () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.LARGE_BACKPACK)
        );
        CuriosRendererRegistry.register(
                CampingItems.SHEEPBAG,
                () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.SHEEPBAG)
        );
        CuriosRendererRegistry.register(
                CampingItems.WANDERER_BACKPACK,
                () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.WANDERER_BACKPACK)
        );
        CuriosRendererRegistry.register(
                CampingItems.WANDERER_BAG,
                () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.WANDERER_BAG)
        );
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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