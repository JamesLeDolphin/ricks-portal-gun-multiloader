package com.jdolphin.ricksportalgun.common.init;


import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class PGDamageTypes {

    public static final ResourceKey<DamageType> BOOTLEG = ResourceKey.create(Registries.DAMAGE_TYPE, PGHelper.createLocation("bootleg"));
    public static final ResourceKey<DamageType> TELEPORT = ResourceKey.create(Registries.DAMAGE_TYPE, PGHelper.createLocation("teleport"));
    public static final ResourceKey<DamageType> BLENDER = ResourceKey.create(Registries.DAMAGE_TYPE, PGHelper.createLocation("blender"));

    public static DamageSource of(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }
}