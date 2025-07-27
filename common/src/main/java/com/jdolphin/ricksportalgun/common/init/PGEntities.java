package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PGEntities {
    private static final Map<ResourceLocation, EntityType<?>> ALL = new HashMap<>();

    public static final EntityType<PortalEntity> PORTAL = register("portal", EntityType.Builder.<PortalEntity>of((PortalEntity::new), MobCategory.MISC).sized(1.0f, 2.0f));


    private static <E extends Entity> EntityType<E> register(String name, EntityType.Builder<E> builder) {
        EntityType<E> type = builder.build(keyOf(name));
        ALL.put(PGHelper.id(name), type);
        return type;
    }

    public static void init(BiConsumer<EntityType<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static ResourceKey<EntityType<?>> keyOf(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, PGHelper.id(id));
    }
}
