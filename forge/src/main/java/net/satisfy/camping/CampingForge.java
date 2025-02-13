package net.satisfy.camping;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.LootModifierManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.camping.core.network.CampingMessagesForge;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.registry.RegistryForge;
import net.satisfy.camping.core.world.block.SleepingBagBlock;
import net.satisfy.camping.integration.CuriosBackpack;
import net.satisfy.camping.integration.CuriosBackpackRenderer;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mod(Constants.MOD_ID)
public class CampingForge {

    public static IEventBus EVENT_BUS = null;

    public CampingForge() {
        Camping.init();
        CampingForge.EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryForge.register(CampingForge.EVENT_BUS);
        CampingForge.EVENT_BUS.addListener(this::setup);
        CampingForge.EVENT_BUS.addListener(this::enqueueIMC);
    }

    @SuppressWarnings("all")
    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CampingMessagesForge::register);
        CuriosApi.registerCurio(CampingItems.SMALL_BACKPACK, new CuriosBackpack());
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