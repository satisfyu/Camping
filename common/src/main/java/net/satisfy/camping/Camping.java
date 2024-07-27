package net.satisfy.camping;

import com.google.common.reflect.Reflection;
import net.satisfy.camping.event.CommonEvents;
import net.satisfy.camping.network.PacketHandler;
import net.satisfy.camping.network.OpenBackpackPacket;
import net.satisfy.camping.network.OpenEnderChestPacket;
import net.satisfy.camping.registry.*;

public class Camping {
    public static final String MODID = "camping";

    /* To-Do List */
    // -> no respawn point sew - how?! tried a playermixin which didnt work
    // [ ] backpacks (jason)
    //     ~ Small, Wanderer, Large, Ender
    /* Config for */
    // [ ] SleepingBag -> MobEffect yes / no
    // [ ] More Ideas here...

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