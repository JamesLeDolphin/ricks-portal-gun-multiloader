package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class FabricDataComponents {


    private static <T> DataComponentType<T> registerComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, PGHelper.createLocation(name), builder.apply(DataComponentType.builder()).build());
    }

    public static void register() {
        PGDataComponents.PORTAL_POS = registerComponent("portal_pos", typeBuilder -> typeBuilder.persistent(BlockPos.CODEC));
        PGDataComponents.PORTAL_DIM = registerComponent("portal_dim", typeBuilder -> typeBuilder.persistent(ResourceLocation.CODEC));
        PGDataComponents.DEFAULT_COLOUR = registerComponent("default_color", typeBuilder -> typeBuilder.persistent(Codec.INT));
        PGDataComponents.BOOTLEG = registerComponent("bootleg", typeBuilder -> typeBuilder.persistent(Codec.BOOL));
        PGDataComponents.PORTAL_COLOUR = registerComponent("portal_color", typeBuilder -> typeBuilder.persistent(Codec.INT));
        PGDataComponents.FUEL = registerComponent("fuel", typeBuilder -> typeBuilder.persistent(Codec.INT));
        PGDataComponents.MAX_FUEL = registerComponent("max_fuel", typeBuilder -> typeBuilder.persistent(Codec.INT));
        PGDataComponents.PRIMARY_DYE = registerComponent("primary_dye", typeBuilder -> typeBuilder.persistent(Codec.INT));
        PGDataComponents.SECONDARY_DYE = registerComponent("secondary_dye", typeBuilder -> typeBuilder.persistent(Codec.INT));
        PGDataComponents.PORTAL_SIZE = registerComponent("portal_size", typeBuilder -> typeBuilder.persistent(Codec.FLOAT));
        PGDataComponents.LOCK = registerComponent("lock", typeBuilder -> typeBuilder.persistent(Codec.BOOL));
        PGDataComponents.OWNER = registerComponent("owner", typeBuilder -> typeBuilder.persistent(Codec.STRING));
        PGDataComponents.CODE = registerComponent("code", typeBuilder -> typeBuilder.persistent(Codec.STRING));
        PGDataComponents.WAYPOINTS = registerComponent("waypoints", typeBuilder -> typeBuilder
                .persistent(Waypoint.CODEC.listOf()).networkSynchronized(Waypoint.PACKET_CODEC.apply(ByteBufCodecs.list())).cacheEncoding());

        PGDataComponents.PORTAL_GUN_TYPE = registerComponent("portal_gun_type", typeBuilder -> typeBuilder
                .persistent(PortalGunType.CODEC).networkSynchronized(PortalGunType.PACKET_CODEC).cacheEncoding());
    }
}
