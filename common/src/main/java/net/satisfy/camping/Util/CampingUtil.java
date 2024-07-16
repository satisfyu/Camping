package net.satisfy.camping.Util;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.platform.PlatformHelper;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class CampingUtil {

    public static <T extends Block> RegistrySupplier<T> registerWithItem(DeferredRegister<Block> registerB, Registrar<Block> registrarB, DeferredRegister<Item> registerI, Registrar<Item> registrarI, ResourceLocation name, Supplier<T> block) {
        RegistrySupplier<T> toReturn = registerWithoutItem(registerB, registrarB, name, block);
        registerItem(registerI, registrarI, name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(DeferredRegister<Block> register, Registrar<Block> registrar, ResourceLocation path, Supplier<T> block) {
        return Platform.isForge() ? register.register(path.getPath(), block) : registrar.register(path, block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(DeferredRegister<Item> register, Registrar<Item> registrar, ResourceLocation path, Supplier<T> itemSupplier) {
        return Platform.isForge() ? register.register(path.getPath(), itemSupplier) : registrar.register(path, itemSupplier);
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        for (int i = 0; i < times; ++i) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.joinUnoptimized(buffer[1], Shapes.box(1.0 - maxZ, minY, minX, 1.0 - minZ, maxY, maxX), BooleanOp.OR));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    public static class Grilling {

        public record FoodValue(int nutrition, float saturationModifier) {
            @Override
            public String toString() {
                return "{Nutrition:" + nutrition + ",Saturation:" + saturationModifier + "}";
            }
        }

        private static final String GRILLED_KEY = "Grilled";
        private static final String NUTRITION_KEY = "GrilledNutrition";
        private static final String SATURATION_KEY = "GrilledSaturation";

        public static boolean isGrilled(ItemStack itemStack) {
            return itemStack.hasTag() && Objects.requireNonNull(itemStack.getTag()).contains(GRILLED_KEY);
        }

        public static void setGrilled(ItemStack itemStack) {
            PlatformHelper.setGrilled(itemStack);
        }

        public static void increaseFoodValue(ItemStack itemStack) {
            FoodProperties food = itemStack.getItem().getFoodProperties();
            if (food != null) {
                int newNutrition = (int) (food.getNutrition() * 1.25);
                float newSaturation = food.getSaturationModifier() * 1.25F;
                CompoundTag tag = itemStack.getOrCreateTag();
                tag.putInt(NUTRITION_KEY, newNutrition);
                tag.putFloat(SATURATION_KEY, newSaturation);
            }
        }

        public static FoodValue getAdditionalFoodValue(ItemStack itemStack) {
            CompoundTag tag = itemStack.getTag();
            if (tag != null && tag.contains(NUTRITION_KEY) && tag.contains(SATURATION_KEY)) {
                int nutrition = tag.getInt(NUTRITION_KEY);
                float saturation = tag.getFloat(SATURATION_KEY);
                return new FoodValue(nutrition, saturation);
            }
            return new FoodValue(0, 0.0F);
        }

        public static void addGrilledTooltip(ItemStack itemStack, List<Component> tooltip) {
            if (isGrilled(itemStack)) {
                tooltip.add(Component.translatable("tooltip.camping.grilled").withStyle(ChatFormatting.GOLD));
                FoodValue value = getAdditionalFoodValue(itemStack);
                tooltip.add(Component.translatable("tooltip.camping.grilled.nutrition", value.nutrition()).withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("tooltip.camping.grilled.saturationModifier", value.saturationModifier()).withStyle(ChatFormatting.GREEN));
            }
        }
    }
}
