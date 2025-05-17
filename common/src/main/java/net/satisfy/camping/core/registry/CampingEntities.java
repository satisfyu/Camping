package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.entity.Mosquito;
import net.satisfy.camping.platform.Services;

import java.util.function.BiConsumer;

public class CampingEntities {

    public static final EntityType<Mosquito> MOSQUITO = Services.REGISTER.entity(Mosquito::new, MobCategory.MISC);

    public static void register(BiConsumer<EntityType<?>, ResourceLocation> consumer) {
        consumer.accept(MOSQUITO, Camping.identifier("mosquito"));
    }
}
