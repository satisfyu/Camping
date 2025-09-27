package net.satisfy.camping;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.client.keys.NeoForgeOpenBackpackKey;
import net.satisfy.camping.core.config.NeoForgeCampingConfig;
import net.satisfy.camping.core.network.NeoForgeCampingNetwork;
import net.satisfy.camping.core.registry.NeoForgeRegistry;
import net.satisfy.camping.core.util.GrillingUtil;
import net.satisfy.camping.core.world.block.SleepingBagBlock;

import java.util.Objects;

@Mod(Constants.MOD_ID)
public class CampingNeoForge {

    public CampingNeoForge(ModContainer modContainer) {
        IEventBus bus = Objects.requireNonNull(modContainer.getEventBus());
        modContainer.registerConfig(ModConfig.Type.COMMON, NeoForgeCampingConfig.SPEC);
        bus.addListener(NeoForgeCampingConfig::onLoad);
        bus.addListener(NeoForgeCampingConfig::onReload);
        Camping.init();
        NeoForgeRegistry.register(bus);
        NeoForgeCampingNetwork.register(bus);
        NeoForge.EVENT_BUS.addListener(CampingNeoForge::onItemTooltip);
        NeoForge.EVENT_BUS.addListener(CampingNeoForge::onPlayerSetSpawn);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            new CampingClientNeoForge(bus);
            bus.addListener(CampingNeoForge::onClientSetup);
            bus.addListener(NeoForgeOpenBackpackKey::onRegisterKeyMappings);
        }
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(NeoForgeOpenBackpackKey::registerInputHandler);
    }

    private static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        GrillingUtil.addGrilledTooltip(stack, event.getToolTip());
    }

    private static void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        if (event.getNewSpawn() == null) return;
        if (event.getEntity().level().getBlockState(event.getNewSpawn()).getBlock() instanceof SleepingBagBlock) {
            event.setCanceled(true);
        }
    }
}
