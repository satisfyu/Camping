package net.satisfy.camping;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.satisfy.camping.client.keys.ForgeOpenBackpackKey;
import net.satisfy.camping.core.config.ForgeCampingConfig;
import net.satisfy.camping.core.network.ForgeCampingNetwork;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.registry.RegistryForge;
import net.satisfy.camping.core.util.GrillingUtil;
import net.satisfy.camping.core.world.block.SleepingBagBlock;
import net.satisfy.camping.core.world.recipe.IBackpackWrapper;
import org.apache.commons.lang3.tuple.Pair;

@Mod(Constants.MOD_ID)
public class CampingForge {

    public static IEventBus EVENT_BUS;
    public static Pair<ForgeCampingConfig, ForgeConfigSpec> CONFIG;

    public CampingForge(FMLJavaModLoadingContext context) {
        CampingForge.EVENT_BUS = context.getModEventBus();
        context.registerConfig(ModConfig.Type.COMMON, ForgeCampingConfig.SPEC, "camping-common.toml");
        EVENT_BUS.addListener(ForgeCampingConfig::onModConfigEvent);
        Camping.init();
        RegistryForge.register(CampingForge.EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            new CampingClientForge(CampingForge.EVENT_BUS);
            EVENT_BUS.addListener(ForgeOpenBackpackKey::onRegisterKeyMappings);
            EVENT_BUS.addListener((FMLClientSetupEvent e) -> e.enqueueWork(ForgeOpenBackpackKey::registerInputHandler));
        }
        ForgeCampingNetwork.register();
        MinecraftForge.EVENT_BUS.addListener(CampingForge::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(CampingForge::onItemTooltip);
        MinecraftForge.EVENT_BUS.addListener(CampingForge::onPlayerSetSpawn);
        MinecraftForge.EVENT_BUS.addListener(CampingForge::onRegisterCapabilities);
    }

    public static void onLivingHurt(final LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity livingAttacker)) return;
        ItemStack stack = livingAttacker.getMainHandItem();
        if (stack.is(CampingItems.MARSHMALLOW_ON_A_STICK)) {
            if (!event.getEntity().hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 3, 2, false, true, false));
            }
        }
    }

    public static void onItemTooltip(final ItemTooltipEvent event) {
        GrillingUtil.addGrilledTooltip(event.getItemStack(), event.getToolTip());
    }

    public static void onPlayerSetSpawn(final PlayerSetSpawnEvent event) {
        if (event.getNewSpawn() == null) return;
        if (event.getEntity().level().getBlockState(event.getNewSpawn()).getBlock() instanceof SleepingBagBlock) {
            event.setCanceled(true);
        }
    }

    public static final Capability<IBackpackWrapper> BACKPACK_WRAPPER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(IBackpackWrapper.class);
    }

    @SuppressWarnings({"removal"})
    public CampingForge() {
        this(FMLJavaModLoadingContext.get());
    }
}
