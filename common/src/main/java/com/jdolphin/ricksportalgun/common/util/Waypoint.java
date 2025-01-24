package com.jdolphin.ricksportalgun.common.util;

import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.LogManager;

public class Waypoint {
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
}