package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PortalFrameBlockEntity extends BlockEntity {
    private BlockPos masterPos;

    public PortalFrameBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_FRAME, pos, blockState);
    }

    public void setMasterPos(BlockPos pos) {
        this.masterPos = pos;
    }

    public BlockPos getMasterPos() {
        return masterPos;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (masterPos != null) {
            CompoundTag pos = NbtUtils.writeBlockPos(masterPos);
            tag.put("MasterPos", pos);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("MasterPos")) {
            CompoundTag pos = tag.getCompound("MasterPos");
            this.masterPos = NbtUtils.readBlockPos(pos);
        }
    }
}
