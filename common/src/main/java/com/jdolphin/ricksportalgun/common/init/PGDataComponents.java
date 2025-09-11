package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
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
    public static final DataComponentType<Integer> DEFAULT_PORTAL_COLOUR = registerInteger("default_color");
    public static final DataComponentType<Boolean> BOOTLEG = registerBoolean("bootleg");
    public static final DataComponentType<Integer> PORTAL_COLOUR = registerInteger("portal_color");
    public static final DataComponentType<Integer> FUEL = registerInteger("fuel");
    public static final DataComponentType<Integer> MAX_FUEL = registerInteger("max_fuel");
    public static final DataComponentType<Boolean> LOCK = registerBoolean("lock");
    public static final DataComponentType<String> OWNER = registerComponent("owner", typeBuilder -> typeBuilder.persistent(Codec.STRING));
    public static final DataComponentType<Integer> PORTAL_LIFETIME = registerInteger("size");

    public static final DataComponentType<List<Waypoint>> WAYPOINTS = registerComponent("waypoints", typeBuilder -> typeBuilder
            .persistent(Waypoint.CODEC.listOf()).networkSynchronized(Waypoint.PACKET_CODEC.apply(ByteBufCodecs.list())).cacheEncoding());
    public static DataComponentType<PortalGunStyle> PORTAL_GUN_STYLE = registerComponent("portal_gun_style", typeBuilder -> typeBuilder
            .persistent(PortalGunStyle.CODEC).networkSynchronized(PortalGunStyle.PACKET_CODEC).cacheEncoding());

    public static final DataComponentType<Integer> PRIMARY_DYE = registerInteger("primary_dye");
    public static final DataComponentType<Integer> SECONDARY_DYE = registerInteger("secondary_dye");
    public static final DataComponentType<Float> PORTAL_SIZE = registerComponent("portal_size", typeBuilder -> typeBuilder.persistent(Codec.FLOAT));
    public static final DataComponentType<String> CODE = registerComponent("code", typeBuilder -> typeBuilder.persistent(Codec.STRING));
    public static final DataComponentType<Boolean> SELF_DESTRUCT = registerBoolean("self_destruct");

    //Components needed for upgrades
    public static final DataComponentType<Boolean> HAS_WAYPOINTS = registerBoolean("has_waypoints");
    public static final DataComponentType<Boolean> EXTRA_DIMENSIONS = registerBoolean("more_dimensions");
    public static final DataComponentType<Boolean> EXTRA_DIMENSIONS_2 = registerBoolean("extra_dimensions");
    public static final DataComponentType<Boolean> SETTINGS = registerBoolean("settings");
    public static final DataComponentType<Boolean> BIOME_LOC = registerBoolean("biome_locating");
    public static final DataComponentType<Boolean> PLAYER_LOC = registerBoolean("player_locating");
    public static final DataComponentType<Boolean> STRUCTURE_LOC = registerBoolean("structure_locating");

    private static DataComponentType<Boolean> registerBoolean(String name) {
        return registerComponent(name, tBuilder -> tBuilder.persistent(Codec.BOOL));
    }

    private static DataComponentType<Integer> registerInteger(String name) {
        return registerComponent(name, tBuilder -> tBuilder.persistent(Codec.INT));
    }

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
