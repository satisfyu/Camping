package net.satisfy.camping.neoforge.core.network;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.satisfy.camping.Constants;
import net.satisfy.camping.neoforge.core.network.packet.EnderPackPayload;

/**
 * @author wdog5
 * For new network system
 * since neoforge reworked the network system
 * @see <a href="https://neoforged.net/news/20.4networking-rework">...</a>
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class NetworkEventHandler {

    private static final boolean REGISTER_FOR_SERVER = true;
    private static final boolean REGISTER_FOR_CLIENT = true;
    private static final String CHANNEL_PROTOCOL_VERSION = FMLEnvironment.dist == Dist.CLIENT ? "V1" : "V2";
    private static PayloadRegistrar INSTANCE;

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        if ((FMLEnvironment.dist == Dist.DEDICATED_SERVER && REGISTER_FOR_SERVER) || (FMLEnvironment.dist == Dist.CLIENT && REGISTER_FOR_CLIENT)) {
            INSTANCE  = event.registrar(CHANNEL_PROTOCOL_VERSION)
                    .versioned(CHANNEL_PROTOCOL_VERSION);
        }
    }

    public static void sendToServer(IPayloadHandler<EnderPackPayload> message) {
        INSTANCE.commonToServer(EnderPackPayload.TYPE, EnderPackPayload.STREAM_CODEC, message);
    }
}
