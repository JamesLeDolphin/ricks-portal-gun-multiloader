package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.PORTAL.create(pos, state);
    }
}
