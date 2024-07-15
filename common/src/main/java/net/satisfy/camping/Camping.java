package net.satisfy.camping;

import com.google.common.reflect.Reflection;
import net.satisfy.camping.registry.EntityTypeRegistry;
import net.satisfy.camping.registry.ObjectRegistry;
import net.satisfy.camping.registry.TabRegistry;

public class Camping {
    public static final String MODID = "camping";

    public static void init() {
        Reflection.initialize(
                ObjectRegistry.class,
                EntityTypeRegistry.class,
                TabRegistry.class

        );
    }

    public static void commonSetup() {
        Reflection.initialize(
        );
    }
}