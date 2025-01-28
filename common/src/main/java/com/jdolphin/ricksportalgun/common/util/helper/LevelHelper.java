package com.jdolphin.ricksportalgun.common.util.helper;


import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LevelHelper {

    public static ResourceKey<Level> getWorldKey(ResourceLocation dimension) {
        return ResourceKey.create(Registries.DIMENSION, dimension);
    }

    public static ServerLevel getServerWorld(Level level, ResourceKey<Level> dimension) {
        return level.getServer().getLevel(dimension);
    }

    public static ResourceLocation getPlayerDimensionLocation(Player player) {
        return getLevelDimensionLocation(player.level());
    }


    public static ResourceLocation getLevelDimensionLocation(Level world) {
        return world.dimension().location();
    }

    public static void playSound(Level world, BlockPos pos, SoundEvent sound, SoundSource category) {
        world.playSound(null, pos, sound, category, 100, 1);
    }
}
