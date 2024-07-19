package net.satisfy.camping;

import com.google.common.reflect.Reflection;
import net.satisfy.camping.registry.EntityTypeRegistry;
import net.satisfy.camping.registry.ObjectRegistry;
import net.satisfy.camping.registry.TabRegistry;

public class Camping {
    public static final String MODID = "camping";

    /* To-Do List */
    // [✓] sleeping bags
    // -> no respawn point sew - how?! tried a playermixin which didnt work
    // [✓] tents
    // [ ] backpacks (jason)
    //     ~ Small, Wanderer, Large, Ender
    // [✓] grill
    // [✓] swiss utility knife
    /* Config for */
    // [ ] FoodDataMixin -> Values configurable
    // [✓] ItemMixin -> Glint yes / no
    // [ ] SleepingBag -> MobEffect yes / no
    // [ ] More Ideas here...

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