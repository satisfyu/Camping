package net.satisfy.camping.core.util;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.Item;
import net.satisfy.camping.client.model.*;
import net.satisfy.camping.core.registry.CampingItems;

import java.util.HashMap;
import java.util.Map;

public class BackpackRegistry {

    private static final Map<Item, BackpackModel> models = new HashMap<>();

    public static Model getBodyModel(Item item, ModelPart baseBody) {
        BackpackModel model = models.computeIfAbsent(item, key -> {
            if (key == CampingItems.SMALL_BACKPACK)
                return new SmallBackpackModel<>(SmallBackpackModel.createBodyLayer().bakeRoot());
            else if (key == CampingItems.LARGE_BACKPACK)
                return new LargeBackpackModel<>(LargeBackpackModel.createBodyLayer().bakeRoot());
            else if (key == CampingItems.WANDERER_BACKPACK)
                return new WandererBackpackModel<>(WandererBackpackModel.createBodyLayer().bakeRoot());
            else if (key == CampingItems.WANDERER_BAG)
                return new WandererBagModel<>(WandererBagModel.createBodyLayer().bakeRoot());
            else if (key == CampingItems.GOODYBAG)
                return new GoodybagModel<>(GoodybagModel.createBodyLayer().bakeRoot());
            else if (key == CampingItems.SHEEPBAG)
                return new SheepbagModel<>(SheepbagModel.createBodyLayer().bakeRoot());
            else if (key == CampingItems.ENDERPACK)
                return new EnderpackModel<>(EnderpackModel.createBodyLayer().bakeRoot());
            else if (key == CampingItems.ENDERBAG)
                return new EnderbagModel<>(EnderbagModel.createBodyLayer().bakeRoot());
            else return null;
        });

        assert model != null;
        model.copyBody(baseBody);
        return (Model) model;
    }
}
