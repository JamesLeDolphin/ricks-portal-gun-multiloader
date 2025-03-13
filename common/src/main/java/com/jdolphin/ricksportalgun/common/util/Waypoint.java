package com.jdolphin.ricksportalgun.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

public class Waypoint implements Comparable<Waypoint> {
    public static final Codec<Waypoint> CODEC;
    public static final StreamCodec<ByteBuf, Waypoint> PACKET_CODEC;
    public static final Waypoint ZERO = new Waypoint(BlockPos.ZERO, "minecraft:overworld", "");
    private int x;
    private int y;
    private int z;
    private String dim;
    private String name;
    private String allDatMixed;

    public Waypoint(int X, int Y, int Z, String dimension, String name) {
        this.x = X;
        this.y = Y;
        this.z = Z;
        this.dim = dimension;
        this.name = name;
        this.allDatMixed = this.x + "|"  + this.y + "|" + this.z + "|" + this.dim + "|" + this.name;
    }

    public Waypoint(BlockPos pos, String dimension, String name) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.dim = dimension;
        this.name = name;
        this.allDatMixed = this.x + "|"  + this.y + "|" + this.z + "|" + this.dim + "|" + this.name;
    }
    public BlockPos getBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public String getWaypointString() {
        return allDatMixed;
    }

    public static Waypoint getWaypoint(String waypointString) {
        String[] dataParts = waypointString.split("\\|");
        if (dataParts.length == 5) {
            try {
                int x = Integer.parseInt(dataParts[0]);
                int y = Integer.parseInt(dataParts[1]);
                int z = Integer.parseInt(dataParts[2]);
                String dimension = dataParts[3];
                String name = dataParts[4];

                return new Waypoint(x, y, z, dimension, name);
            } catch (NumberFormatException e){
                e.printStackTrace();
            }
        } else {
            LogManager.getLogger().warn("Invalid data format");
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getDim() {
        return this.dim;
    }

    public void setX(int X) {
        this.x = X;
    }

    public void setY(int Y) {
        this.y = Y;
    }
    public void setZ(int Z) {
        this.z = Z;
    }
    public void setX(BlockPos pos) {
        this.z = pos.getX();
    }

    public void setY(BlockPos pos) {
        this.z = pos.getZ();
    }

    public void setZ(BlockPos pos) {
        this.z = pos.getZ();
    }

    public void setDim(String dimension) {
        this.dim = dimension;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NotNull Waypoint wp) {
        return this.getBlockPos().compareTo(wp.getBlockPos()) + this.getDim().compareTo(wp.getDim()) + this.getName().compareTo(wp.getName());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Waypoint waypoint)) {
            return false;
        } else {
            if (this.getX() != waypoint.getX()) {
                return false;
            } else if (this.getY() != waypoint.getY()) {
                return false;
            } else if (this.getZ() != waypoint.getZ()) {
                return false;
            } else if (!this.getDim().equals(waypoint.getDim())) {
                return false;
            } else return this.getName().equals(waypoint.getName());

        }
    }

    static {
        CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(BlockPos.CODEC.fieldOf("pos").forGetter(Waypoint::getBlockPos),
                                Codec.STRING.fieldOf("dimension").forGetter(Waypoint::getDim),
                                Codec.STRING.fieldOf("name").forGetter(Waypoint::getName))
                        .apply(instance, Waypoint::new));

        PACKET_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC,
                Waypoint::getBlockPos, ByteBufCodecs.STRING_UTF8,
                Waypoint::getDim, ByteBufCodecs.STRING_UTF8,
                Waypoint::getName, Waypoint::new);
    }
}