package net.satisfy.camping.fabric.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.satisfy.camping.Camping;

@Config(name = Camping.MODID)
public class ConfigFabric implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.NoTooltip
    public static boolean enableGrilling = true;
    @ConfigEntry.Gui.NoTooltip
    public boolean enableGlint = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.NoTooltip
    public static boolean enableEffect = true;


}
