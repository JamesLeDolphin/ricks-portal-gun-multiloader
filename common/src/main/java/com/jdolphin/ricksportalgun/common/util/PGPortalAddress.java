package com.jdolphin.ricksportalgun.common.util;

import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
public class PGPortalAddress {
    private static final char[] BASE52 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    public static String getAddress(BlockPos pos, ResourceLocation dim) {
        long packed = pack(pos.getX(), pos.getZ(), dim);
        return encode(packed);
    }

    public static DecodedAddress getLocation(String address) {
        long packed = decode(address);
        return unpack(packed);
    }


    private static long pack(int blockX, int blockZ, ResourceLocation dim) {
        int chunkX = blockX >> 4;
        int chunkZ = blockZ >> 4;

        long x = (long)(chunkX + 1_875_000) & 0x3FFFFFL;
        long z = (long)(chunkZ + 1_875_000) & 0x3FFFFFL;
        long d = LevelHelper.DIM_TO_INT.get(dim.toString()) & 0x7F;

        return (x << 29) | (z << 7) | d;
    }

    private static String encode(long packed) {
        char[] out = new char[9];
        for (int i = 8; i >= 0; i--) {
            out[i] = BASE52[(int)(packed % 52)];
            packed /= 52;
        }
        return new String(out);
    }

    private static long decode(String code) {
        long value = 0;
        for (char c : code.toCharArray()) {
            int idx = 0;
            if (c >= 'A' && c <= 'Z') {
                idx = c - 'A';
            } else if (c >= 'a' && c <= 'z') {
                idx = c - 'a' + 26;
            } else {
                System.out.println("Illegal character: " + c);
            }
            value = value * 52 + idx;
        }
        return value;
    }

    private static DecodedAddress unpack(long packed) {
        long d = packed & 0x7FL;
        long z = (packed >> 7) & 0x3FFFFFL;
        long x = (packed >> 29) & 0x3FFFFFL;

        int chunkX = (int)x - 1_875_000;
        int chunkZ = (int)z - 1_875_000;
        ResourceLocation dim = new ResourceLocation(PGHelper.getKeyFromValue(LevelHelper.DIM_TO_INT, ((int) d)));

        return new DecodedAddress(chunkX, chunkZ, dim);
    }

    public record DecodedAddress(int chunkX, int chunkZ, ResourceLocation dimension) {
    }
}
