package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.Constants;
import com.jdolphin.ricksportalgun.common.component.WaypointComponent;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.UnaryOperator;

public class ForgeDataComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MODID);

    public static final RegistryObject<DataComponentType<BlockPos>> PORTAL_POS = register("portal_pos", builder -> builder.persistent(BlockPos.CODEC));
    public static final RegistryObject<DataComponentType<ResourceLocation>> PORTAL_DIM = register("portal_dim", typeBuilder -> typeBuilder.persistent(ResourceLocation.CODEC));
    public static final RegistryObject<DataComponentType<Boolean>> BOOTLEG = register("bootleg", typeBuilder -> typeBuilder.persistent(Codec.BOOL));
    public static final RegistryObject<DataComponentType<Integer>> PORTAL_COLOUR = register("portal_color", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final RegistryObject<DataComponentType<Integer>> DEFAULT_COLOUR = register("default_color", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final RegistryObject<DataComponentType<Integer>> FUEL = register("fuel", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final RegistryObject<DataComponentType<Integer>> MAX_FUEL = register("max_fuel", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final RegistryObject<DataComponentType<Boolean>> LOCK = register("lock", typeBuilder -> typeBuilder.persistent(Codec.BOOL));
    public static final RegistryObject<DataComponentType<String >> OWNER = register("owner", builder -> builder.persistent(Codec.STRING));

    public static final RegistryObject<DataComponentType<List<Waypoint>>> WAYPOINTS = register("waypoints", typeBuilder -> typeBuilder
            .persistent(WaypointComponent.CODEC.listOf()).networkSynchronized(WaypointComponent.PACKET_CODEC.apply(ByteBufCodecs.list())).cacheEncoding());

    public static final RegistryObject<DataComponentType<PortalGunType>> PORTAL_GUN_TYPE = register("portal_gun_type", typeBuilder -> typeBuilder
            .persistent(PortalGunType.CODEC).networkSynchronized(PortalGunType.PACKET_CODEC).cacheEncoding());

    private static <T> RegistryObject<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return COMPONENTS.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }
}