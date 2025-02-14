package net.satisfy.camping.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.satisfy.camping.Camping;
import net.satisfy.camping.Constants;

@Config(name = Constants.MOD_ID)
public class ConfigFabric implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.NoTooltip
    public static boolean enableGrilling = true;
    @ConfigEntry.Gui.NoTooltip
    public boolean enableGlint = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.NoTooltip
    public static boolean enableEffect = true; // todo pass this value up to common, also add config for Forge

}
