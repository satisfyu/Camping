package net.satisfy.camping;

import com.google.common.reflect.Reflection;
import net.satisfy.camping.event.CommonEvents;
import net.satisfy.camping.network.OpenBackpackPacket;
import net.satisfy.camping.network.OpenEnderChestPacket;
import net.satisfy.camping.network.PacketHandler;
import net.satisfy.camping.registry.*;

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