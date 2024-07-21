package net.satisfy.camping.event;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.satisfy.camping.Camping;
import org.jetbrains.annotations.Nullable;

public class CommonEvents {

    public static void registerEvents() {
        LootEvent.MODIFY_LOOT_TABLE.register(CommonEvents::onModifyLootTable);
    }

    public static void onModifyLootTable(@Nullable LootDataManager lootDataManager, ResourceLocation id, LootEvent.LootTableModificationContext ctx, boolean b) {
        LoottableInjector.InjectLoot(id, ctx);
    }

    public static class LoottableInjector {
        public static void InjectLoot(ResourceLocation id, LootEvent.LootTableModificationContext context) {
            String prefix = "minecraft:chests/";
            String name = id.toString();

            if (name.startsWith(prefix)) {
                String file = name.substring(name.indexOf(prefix) + prefix.length());
                if (file.equals("end_city_treasure")) {
                    context.addPool(getPool(file));
                }
            }
        }

        public static LootPool getPool(String entryName) {
            return LootPool.lootPool().add(getPoolEntry(entryName)).build();
        }

        @SuppressWarnings("rawtypes")
        private static LootPoolEntryContainer.Builder getPoolEntry(String name) {
            ResourceLocation table = new ResourceLocation(Camping.MODID, "chests/" + name);
            return LootTableReference.lootTableReference(table);
        }
    }
}