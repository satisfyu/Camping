package net.satisfy.camping.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.Camping;
import net.satisfy.camping.fabric.compat.trinkets.BackpackTrinket;
import net.satisfy.camping.fabric.compat.trinkets.TrinketsCompatibility;
import net.satisfy.camping.fabric.config.ConfigFabric;
import net.satisfy.camping.item.BackpackItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CampingFabric implements ModInitializer {

    public static boolean trinketsLoaded;

    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigFabric.class, GsonConfigSerializer::new);
        Camping.init();

        CampingFabric.trinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");

        if (trinketsLoaded) {
            TrinketsCompatibility.load();
        }
    }

    public static boolean trinketsLoaded() {
        return CampingFabric.trinketsLoaded;
    }
}
