package com.jdolphin.ricksportalgun.common.init;


import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class PGDamageTypes {

    public static final ResourceKey<DamageType> BOOTLEG = ResourceKey.create(Registries.DAMAGE_TYPE, PGHelper.id("bootleg"));
    public static final ResourceKey<DamageType> TELEPORT = ResourceKey.create(Registries.DAMAGE_TYPE, PGHelper.id("teleport"));
    public static final ResourceKey<DamageType> BLENDER = ResourceKey.create(Registries.DAMAGE_TYPE, PGHelper.id("blender"));
    public static final ResourceKey<DamageType> SELF_DESTRUCT = ResourceKey.create(Registries.DAMAGE_TYPE, PGHelper.id("self_destruct"));

    public static DamageSource of(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }
}