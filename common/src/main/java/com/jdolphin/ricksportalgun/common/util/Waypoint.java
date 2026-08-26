package com.jdolphin.ricksportalgun.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.LogManager;

public class Waypoint implements Comparable<Waypoint> {
    public static final Codec<Waypoint> CODEC;
    public static final Waypoint ZERO = new Waypoint(BlockPos.ZERO, 0, "minecraft:overworld", "");
    private int x;
    private int y;
    private int z;
    private String dim;
    private String name;
    private float rotation;
    private final String waypointString;

    public Waypoint(int x, int y, int z, float rotation, String dimension, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.dim = dimension;
        this.name = name;
        this.waypointString = this.x + "|"  + this.y + "|" + this.z + "|" + this.dim + "|" + this.name;
    }

    public Waypoint(BlockPos pos, float rotation, String dimension, String name) {
        this(pos.getX(), pos.getY(), pos.getZ(), rotation, dimension, name);
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

    public float getRotation() {
        return rotation;
    }

    public String getWaypointString() {
        return waypointString;
    }

    public static Waypoint getWaypoint(String waypointString) {
        String[] dataParts = waypointString.split("\\|");
        if (dataParts.length >= 5) {
            try {
                int x = Integer.parseInt(dataParts[0]);
                int y = Integer.parseInt(dataParts[1]);
                int z = Integer.parseInt(dataParts[2]);
                String dimension = dataParts[3];
                String name = dataParts[4];
                float rot;
                if (dataParts.length == 6) rot = Float.parseFloat(dataParts[5]);
                else rot = 0;

                return new Waypoint(x, y, z, rot, dimension, name);
            } catch (NumberFormatException e){
                e.printStackTrace();
            }
        } else {
            LogManager.getLogger().warn("Invalid waypoint data format");
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

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public int compareTo(Waypoint wp) {
        return this.getBlockPos().compareTo(wp.getBlockPos()) + this.getDimension().compareTo(wp.getDimension()) + this.getName().compareTo(wp.getName());
    }

    public boolean equals(Object o) {
        if (o instanceof Waypoint waypoint) {
            if (waypoint.getX() == this.getX() && waypoint.getY() == this.getY() && waypoint.getZ() == this.getZ()) {
                if (waypoint.getDimension().equals(this.getDimension()) && waypoint.getName().equals(this.getName())) {
                    return waypoint.getRotation() == this.getRotation();
                }
            }
        }
        return false;
    }

    static {
        CODEC = RecordCodecBuilder.create((inst) -> inst.group(
                        BlockPos.CODEC.fieldOf("pos").forGetter(Waypoint::getBlockPos),
                        Codec.FLOAT.optionalFieldOf("rotation", 0f).forGetter(Waypoint::getRotation),
                        Codec.STRING.fieldOf("dimension").forGetter(Waypoint::getDimension),
                        Codec.STRING.fieldOf("name").forGetter(Waypoint::getName))
                .apply(inst, Waypoint::new));
    }
}