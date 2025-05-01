package net.satisfy.camping.core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ForgeCampingConfig {

    public static boolean enableGrilling = true;
    public static boolean enableGlint = true;
    public static boolean enableEffect = true;

    // define and build the config spec for Forge
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLE_GRILLING = BUILDER.define("enableGrilling", true);
    private static final ForgeConfigSpec.BooleanValue ENABLE_GLINT = BUILDER.define("enableGlint", true);
    private static final ForgeConfigSpec.BooleanValue ENABLE_EFFECT = BUILDER.define("enableEffect", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static void onModConfigEvent(final ModConfigEvent event) {
        enableGrilling = ENABLE_GRILLING.get();
        enableGlint = ENABLE_GLINT.get();
        enableEffect = ENABLE_EFFECT.get();
    }


//    public ForgeCampingConfig(ForgeConfigSpec.Builder builder) {
//
//        ForgeConfigSpec.ConfigValue<Boolean> enableGrilling = builder.define("enableGrilling", true);
//        ForgeConfigSpec.ConfigValue<Boolean> enableGlint = builder.define("enableGlint", true);
//        ForgeConfigSpec.ConfigValue<Boolean> enableEffect = builder.define("enableEffect", true);
//    }

    // public final boolean enableGrilling = true;
    // public final boolean enableGlint = true;
    // public final boolean enableEffect = true;
}
