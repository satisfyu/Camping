package net.satisfy.camping.core.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Objects;

public class CampingUtil {

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
            return itemStack.has(DataComponents.CUSTOM_DATA) && Objects.requireNonNull(itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).contains(GRILLED_KEY);
        }

        public static void setGrilled(ItemStack itemStack) {
            CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            tag.putBoolean("Grilled", true);
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            CampingUtil.Grilling.increaseFoodValue(itemStack);
        }

        public static void increaseFoodValue(ItemStack itemStack) {
            FoodProperties food = itemStack.get(DataComponents.FOOD);
            if (food != null) {
                int newNutrition = (int) (food.nutrition() * 1.25);
                float newSaturation = food.saturation() * 1.25F;
                CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                tag.putInt(NUTRITION_KEY, newNutrition);
                tag.putFloat(SATURATION_KEY, newSaturation);
                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
        }

        public static FoodValue getAdditionalFoodValue(ItemStack itemStack) {
            CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            if (tag != null && tag.contains(NUTRITION_KEY) && tag.contains(SATURATION_KEY)) {
                int nutrition = tag.getInt(NUTRITION_KEY);
                float saturation = tag.getFloat(SATURATION_KEY);
                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
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
