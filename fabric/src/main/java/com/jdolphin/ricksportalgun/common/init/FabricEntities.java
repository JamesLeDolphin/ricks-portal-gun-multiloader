package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class FabricEntities {

    public static void register() {
        PGEntities.PORTAL = Registry.register(BuiltInRegistries.ENTITY_TYPE, "portal",
                EntityType.Builder.of(PortalEntity::new, MobCategory.MISC).sized(1.0f, 2.0f)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, Helper.createLocation("portal"))));
    }
}