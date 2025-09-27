package net.satisfy.camping.core.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoForgeCampingConfig {

    public static boolean enableGrilling = true;
    public static boolean enableGlint = true;
    public static boolean enableEffect = true;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_GRILLING = BUILDER.define("enableGrilling", true);
    private static final ModConfigSpec.BooleanValue ENABLE_GLINT = BUILDER.define("enableGlint", true);
    private static final ModConfigSpec.BooleanValue ENABLE_EFFECT = BUILDER.define("enableEffect", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void onLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) bake();
    }

    public static void onReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) bake();
    }

    private static void bake() {
        enableGrilling = ENABLE_GRILLING.get();
        enableGlint = ENABLE_GLINT.get();
        enableEffect = ENABLE_EFFECT.get();
    }
}
