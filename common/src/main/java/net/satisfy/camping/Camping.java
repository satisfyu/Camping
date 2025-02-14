package net.satisfy.camping;

import net.minecraft.resources.ResourceLocation;

public class Camping {

    // todo checklist
    /*
    * 1. Walking Stick - faster walking
    * 2. Coffee Pot & Cofee Pot Stand
    * 3. New Campfire models
    * 4. Marshmallow on a stick
    * 5. campfires turn off after after configurable amount of time / during rain
    * 6. campfires have a chance to ignite wood around them
    * 7. twist bread / bread on a stick
    * 8. New grill variants
    * 9. fire pit
    * 10. pillager tent variant
    * 11. park bench/table set
    * 12. mosquito rare chance during the daytime, small swarms
    */

    public static void init() {}

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}