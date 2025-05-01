package net.satisfy.camping;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.satisfy.camping.core.config.ForgeCampingConfig;
import net.satisfy.camping.core.network.ForgeCampingNetwork;
import net.satisfy.camping.core.registry.RegistryForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.core.world.block.SleepingBagBlock;
import net.satisfy.camping.core.world.recipe.IBackpackWrapper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class CampingForge {

    public static IEventBus EVENT_BUS;
    public static Pair<ForgeCampingConfig, ForgeConfigSpec> CONFIG;
    
    public CampingForge(FMLJavaModLoadingContext context) {

        CampingForge.EVENT_BUS = context.getModEventBus();

        context.registerConfig(ModConfig.Type.COMMON, ForgeCampingConfig.SPEC, "camping");
        EVENT_BUS.addListener(ForgeCampingConfig::onModConfigEvent);

        Camping.init();
        RegistryForge.register(CampingForge.EVENT_BUS); // ensure items/blocks are registered before reference on client
        if (FMLEnvironment.dist == Dist.CLIENT) new CampingClientForge(CampingForge.EVENT_BUS);
        ForgeCampingNetwork.register();

        MinecraftForge.EVENT_BUS.addListener(CampingForge::onItemTooltip);
        MinecraftForge.EVENT_BUS.addListener(CampingForge::onPlayerSetSpawn);
        MinecraftForge.EVENT_BUS.addListener(CampingForge::onRegisterCapabilities);
    }

    public static void onItemTooltip(final ItemTooltipEvent event) {
        CampingUtil.Grilling.addGrilledTooltip(event.getItemStack(), event.getToolTip());
    }

    public static void onPlayerSetSpawn(final PlayerSetSpawnEvent event) {
        if (event.getNewSpawn() == null) return;
        if (event.getEntity().level().getBlockState(event.getNewSpawn()).getBlock() instanceof SleepingBagBlock) event.setCanceled(true);
    }

    public static final Capability<IBackpackWrapper> BACKPACK_WRAPPER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(IBackpackWrapper.class);
    }

    /**
     * Users may not be using Forge 47.4.0 yet, provide the original mod constructor to avoid errors.
     * @author Jason13
     */
    @SuppressWarnings({"removal"})
    public CampingForge() {
        this(FMLJavaModLoadingContext.get());
    }
}
