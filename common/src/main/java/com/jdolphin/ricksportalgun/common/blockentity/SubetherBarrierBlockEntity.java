package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SubetherBarrierBlockEntity extends BlockEntity {

    public SubetherBarrierBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public SubetherBarrierBlockEntity(BlockPos pos, BlockState blockState) {
        this(PGBlockEntities.SUBETHER_BARRIER, pos, blockState);
    }
}
