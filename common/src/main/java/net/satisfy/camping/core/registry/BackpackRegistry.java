package net.satisfy.camping.core.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.Item;
import net.satisfy.camping.client.model.*;

import java.util.HashMap;
import java.util.Map;

public class BackpackRegistry {
    private static final Map<Item, BackpackModel> models = new HashMap<>();

    public static Model getBodyModel(Item item, ModelPart baseBody) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        BackpackModel model = models.computeIfAbsent(item, key -> {
            if (key == CampingItems.SMALL_BACKPACK) {
                return new SmallBackpackModel<>(modelSet.bakeLayer(SmallBackpackModel.LAYER_LOCATION));
            } else if (key == CampingItems.LARGE_BACKPACK) {
                return new LargeBackpackModel<>(modelSet.bakeLayer(LargeBackpackModel.LAYER_LOCATION));
            } else if (key == CampingItems.WANDERER_BACKPACK) {
                return new WandererBackpackModel<>(modelSet.bakeLayer(WandererBackpackModel.LAYER_LOCATION));
            } else if (key == CampingItems.WANDERER_BAG) {
                return new WandererBagModel<>(modelSet.bakeLayer(WandererBagModel.LAYER_LOCATION));
            } else if (key == CampingItems.GOODYBAG) {
                return new GoodybagModel<>(modelSet.bakeLayer(GoodybagModel.LAYER_LOCATION));
            } else if (key == CampingItems.SHEEPBAG) {
                return new SheepbagModel<>(modelSet.bakeLayer(SheepbagModel.LAYER_LOCATION));
            } else if (key == CampingItems.ENDERPACK) {
                return new EnderpackModel<>(modelSet.bakeLayer(EnderpackModel.LAYER_LOCATION));
            } else if (key == CampingItems.ENDERBAG) {
                return new EnderbagModel<>(modelSet.bakeLayer(EnderbagModel.LAYER_LOCATION));
            } else {
                return null;
            }
        });

        assert model != null;
        model.copyBody(baseBody);

        return (Model) model;
    }
}
