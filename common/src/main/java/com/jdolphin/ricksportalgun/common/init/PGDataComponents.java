package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

public class PGDataComponents {
    private static final Map<ResourceLocation, DataComponentType<?>> ALL = new HashMap<>();

    public static final DataComponentType<BlockPos> PORTAL_POS = registerComponent("portal_pos", typeBuilder -> typeBuilder.persistent(BlockPos.CODEC));
    public static final DataComponentType<ResourceLocation> PORTAL_DIM = registerComponent("portal_dim", typeBuilder -> typeBuilder.persistent(ResourceLocation.CODEC));
    public static final DataComponentType<Integer> DEFAULT_COLOUR = registerComponent("default_color", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final DataComponentType<Boolean> BOOTLEG = registerComponent("bootleg", typeBuilder -> typeBuilder.persistent(Codec.BOOL));
    public static final DataComponentType<Integer> PORTAL_COLOUR = registerComponent("portal_color", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final DataComponentType<Integer> FUEL = registerComponent("fuel", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final DataComponentType<Integer> MAX_FUEL = registerComponent("max_fuel", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final DataComponentType<Boolean> LOCK = registerComponent("lock", typeBuilder -> typeBuilder.persistent(Codec.BOOL));
    public static final DataComponentType<String> OWNER = registerComponent("owner", typeBuilder -> typeBuilder.persistent(Codec.STRING));

    public static final DataComponentType<List<Waypoint>> WAYPOINTS = registerComponent("waypoints", typeBuilder -> typeBuilder
            .persistent(Waypoint.CODEC.listOf()).networkSynchronized(Waypoint.PACKET_CODEC.apply(ByteBufCodecs.list())).cacheEncoding());

    public static DataComponentType<PortalGunType> PORTAL_GUN_TYPE = registerComponent("portal_gun_type", typeBuilder -> typeBuilder
            .persistent(PortalGunType.CODEC).networkSynchronized(PortalGunType.PACKET_CODEC).cacheEncoding());

    public static final DataComponentType<Integer> PRIMARY_DYE = registerComponent("primary_dye", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final DataComponentType<Integer> SECONDARY_DYE = registerComponent("secondary_dye", typeBuilder -> typeBuilder.persistent(Codec.INT));
    public static final DataComponentType<Float> PORTAL_SIZE = registerComponent("portal_size", typeBuilder -> typeBuilder.persistent(Codec.FLOAT));
    public static final DataComponentType<String> CODE = registerComponent("code", typeBuilder -> typeBuilder.persistent(Codec.STRING));

    private static <T> DataComponentType<T> registerComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        ALL.put(PGHelper.createLocation(name), type);
        return type;
    }

    public static void init(BiConsumer<DataComponentType<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
}
