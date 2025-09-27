package net.satisfy.camping.core.registry;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.satisfy.camping.Camping;

import java.util.Set;

public class FabricCampingLootModifiers {

    private static final Set<String> VALID_CHEST_FILES = Set.of(
            "end_city_treasure",
            "stronghold_corridor",
            "stronghold_crossing",
            "stronghold_library",
            "spawn_bonus_chest"
    );

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            String prefix = "minecraft:chests/";
            String name = key.location().toString();
            if (source.isBuiltin() && name.startsWith(prefix)) {
                String file = name.substring(prefix.length());
                if (VALID_CHEST_FILES.contains(file)) {
                    ResourceKey<LootTable> table = ResourceKey.create(Registries.LOOT_TABLE, Camping.identifier("chests/" + file));
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .when(LootItemRandomChanceCondition.randomChance(0.35f))
                            .add(NestedLootTable.lootTableReference(table))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)));
                    tableBuilder.withPool(poolBuilder);
                }
            }
        });
    }
}
