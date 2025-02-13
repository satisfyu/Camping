package net.satisfy.camping.core.registry;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.satisfy.camping.Camping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CampingLootModifiersFabric {

    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {

            String prefix = "minecraft:chests/";
            String name = id.toString();
            if (name.startsWith(prefix)) {
                String file = name.substring(name.indexOf(prefix) + prefix.length());
                Set<String> validFiles = new HashSet<>(Arrays.asList("end_city_treasure", "stronghold_corridor", "stronghold_crossing", "stronghold_library", "spawn_bonus_chest"));
                if (validFiles.contains(file)) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .when(LootItemRandomChanceCondition.randomChance(0.35f)) // Drops 35% of the time
                            .add(getPoolEntry(file))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)).build());

                    tableBuilder.pool(poolBuilder.build());
                }
            }
        });
    }

    private static LootPoolEntryContainer.Builder<?> getPoolEntry(String name) {
        ResourceLocation table = Camping.identifier("chests/" + name);
        return LootTableReference.lootTableReference(table);
    }
}
