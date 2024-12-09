package net.satisfy.camping;

import com.google.common.reflect.Reflection;
import net.satisfy.camping.core.event.CommonEvents;
import net.satisfy.camping.core.network.OpenBackpackPacket;
import net.satisfy.camping.core.network.OpenEnderChestPacket;
import net.satisfy.camping.core.network.PacketHandler;
import net.satisfy.camping.core.registry.*;

public class Camping {
    public static final String MODID = "camping";
    public static void init() {
        Reflection.initialize(
                ObjectRegistry.class,
                EntityTypeRegistry.class,
                TabRegistry.class,
                TagRegistry.class,
                ScreenhandlerTypeRegistry.class,
                CommonEvents.class
        );
        OpenBackpackPacket.register();
        OpenEnderChestPacket.register();
        CommonEvents.registerEvents();
        PacketHandler.registerC2SPackets();
    }
}