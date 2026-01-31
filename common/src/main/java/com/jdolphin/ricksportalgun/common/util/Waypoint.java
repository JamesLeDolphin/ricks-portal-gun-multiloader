package com.jdolphin.ricksportalgun.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.LogManager;

public class Waypoint implements Comparable<Waypoint> {
    public static final Codec<Waypoint> CODEC;
    public static final Waypoint ZERO = new Waypoint(BlockPos.ZERO, "minecraft:overworld", "");
    private int x;
    private int y;
    private int z;
    private String dim;
    private String name;
    private final String waypointString;

    public Waypoint(int x, int y, int z, String dimension, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dimension;
        this.name = name;
        this.waypointString = this.x + "|"  + this.y + "|" + this.z + "|" + this.dim + "|" + this.name;
    }

    public Waypoint(BlockPos pos, String dimension, String name) {
        this(pos.getX(), pos.getY(), pos.getZ(), dimension, name);
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
        return waypointString;
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

    public String getDimension() {
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

    public void setDimension(String dimension) {
        this.dim = dimension;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Waypoint wp) {
        return this.getBlockPos().compareTo(wp.getBlockPos()) + this.getDimension().compareTo(wp.getDimension()) + this.getName().compareTo(wp.getName());
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
            } else if (!this.getDimension().equals(waypoint.getDimension())) {
                return false;
            } else return this.getName().equals(waypoint.getName());

        }
    }

    static {
        CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(BlockPos.CODEC.fieldOf("mobId").forGetter(Waypoint::getBlockPos),
                                Codec.STRING.fieldOf("dimension").forGetter(Waypoint::getDimension),
                                Codec.STRING.fieldOf("name").forGetter(Waypoint::getName))
                        .apply(instance, Waypoint::new));
    }
}