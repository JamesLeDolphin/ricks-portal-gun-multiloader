package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.comp.immersive_portals.SeeThroughPortalEntity;
import com.jdolphin.ricksportalgun.common.entity.ExplosiveItemEntity;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
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

    public static EntityType<SeeThroughPortalEntity> SEETHROUGH_PORTAL = null;

    public static final EntityType<ExplosiveItemEntity> EXPLOSIVE_ITEM = register("explosive_item",
            EntityType.Builder.<ExplosiveItemEntity>of((ExplosiveItemEntity::new), MobCategory.MISC).sized(0.25f, 0.25f)
                    .clientTrackingRange(6).updateInterval(20));

    private static <E extends Entity> EntityType<E> register(String name, EntityType.Builder<E> builder) {
        EntityType<E> type = builder.build(name);
        ALL.put(PGHelper.id(name), type);
        return type;
    }

    public static void init(BiConsumer<EntityType<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
}
