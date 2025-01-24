package com.jdolphin.ricksportalgun.common.component;

import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record WaypointComponent(BlockPos pos, String dim, String name) {
    public static final Codec<List<Waypoint>> LIST_CODEC;
    public static final Codec<Waypoint> CODEC;
    public static final StreamCodec<ByteBuf, Waypoint> PACKET_CODEC;

    public WaypointComponent(Waypoint waypoint) {
        this(waypoint.getBlockPos(), waypoint.getDim(), waypoint.getName());
    }

    public BlockPos pos() {
        return this.pos;
    }

    public WaypointComponent(BlockPos pos, String dim, String name) {
        this.pos = pos;
        this.name = name;
        this.dim = dim;
    }

    static {
        CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(BlockPos.CODEC.fieldOf("pos").forGetter(Waypoint::getBlockPos),
                                Codec.STRING.fieldOf("dimension").forGetter(Waypoint::getDim),
                                Codec.STRING.fieldOf("name").forGetter(Waypoint::getName))
                        .apply(instance, Waypoint::new));

        LIST_CODEC = CODEC.listOf();
        PACKET_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC,
                Waypoint::getBlockPos, ByteBufCodecs.STRING_UTF8,
                Waypoint::getDim, ByteBufCodecs.STRING_UTF8,
                Waypoint::getName, Waypoint::new);
    }
}