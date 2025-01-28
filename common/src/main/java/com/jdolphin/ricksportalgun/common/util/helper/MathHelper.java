package com.jdolphin.ricksportalgun.common.util.helper;

import net.minecraft.core.BlockPos;
import org.joml.Vector3d;


public class MathHelper {

    public static BlockPos vecToBlockPos(Vector3d vec3) {
        return new BlockPos((int) vec3.x, (int) vec3.y, (int) vec3.z);
    }

    public static Vector3d blockPosToVec(BlockPos pos) {
        return new Vector3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos XYZtoBlockPos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }
}