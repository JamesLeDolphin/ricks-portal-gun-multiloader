package com.jdolphin.ricksportalgun.common.meeseeks.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class EntityTypeArgument extends AbstractMeeseeksArgument<EntityType> {
    protected final List<EntityType<?>> excluded;

    public EntityTypeArgument(String name) {
        this(name, new EntityType[]{});
    }

    public EntityTypeArgument(String name, EntityType<?>... excluded) {
        super(name, EntityType.class);
        this.excluded = List.of(excluded);
    }

    @Override
    public EntityType<?> fromString(String string) {
        if (isValid(string)) {
            ResourceLocation rl = ResourceLocation.tryParse(string);
            return BuiltInRegistries.ENTITY_TYPE.get(rl);
        }
        return null;
    }

    @Override
    public boolean isValid(String string) {
        ResourceLocation rl = ResourceLocation.tryParse(string);
        if (rl != null) {
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(rl);
            if (!excluded.contains(type)) {
                if (type == EntityType.PIG) return string.equals("minecraft:pig");
                else return true;
            }
        }
        return false;
    }

    @Override
    public List<String> values() {
        return BuiltInRegistries.ENTITY_TYPE.stream().filter(type -> !excluded.contains(type))
                .map(type -> type.getDescription().getString()).toList();
    }
}
