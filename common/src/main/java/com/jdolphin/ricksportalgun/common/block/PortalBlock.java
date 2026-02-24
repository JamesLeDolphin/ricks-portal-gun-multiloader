package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;


public class PortalBlock extends NetherPortalBlock implements EntityBlock {

    public PortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PortalBlockEntity portal) {
            portal.onEntityInside(entity);
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 2; ++i) {
            double d0 = (double)pos.getX() + random.nextDouble();
            double d1 = (double)pos.getY() + random.nextDouble();
            double d2 = (double)pos.getZ() + random.nextDouble();
            double d3 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;

            int j = random.nextInt(2) * 2 - 1;
            if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
                d0 = (double)pos.getX() + 0.5D + 0.25D * (double)j;
                d3 = random.nextFloat() * 2.0F * (float)j;
            } else {
                d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)j;
                d5 = random.nextFloat() * 2.0F * (float)j;
            }

            level.addParticle(new DustParticleOptions(new Vector3f(0, 0.8f, 1), 1), d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.PORTAL.create(pos, state);
    }
}
