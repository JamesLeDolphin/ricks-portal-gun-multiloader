package com.jdolphin.ricksportalgun.common.init;


import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class PGDamageTypes {
    private static Registry<DamageType> damageTypes;
    private static DamageSource bootleg;
    private static DamageSource teleport;
    private static DamageSource blender;
    private static DamageSource self_destruct;

    public static void init(RegistryAccess registryAccess) {
        damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
        bootleg = source("bootleg");
        teleport = source("teleport");
        blender = source("blender");
        self_destruct = source("self_destruct");
    }

    public static DamageSource bootleg() {
        return bootleg;
    }

    public static DamageSource teleport() {
        return teleport;
    }

    public static DamageSource blender() {
        return blender;
    }

    public static DamageSource selfDestruct() {
        return self_destruct;
    }

    private static DamageSource source(String name) {
        return new DamageSource(damageTypes.getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, PGHelper.id(name))));
    }
}