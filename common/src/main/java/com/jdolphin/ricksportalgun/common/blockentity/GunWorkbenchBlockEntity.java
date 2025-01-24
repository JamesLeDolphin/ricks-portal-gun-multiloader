package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GunWorkbenchBlockEntity extends BlockEntity {
    public GunWorkbenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.GUN_WORKBENCH, pos, blockState);
    }
}
