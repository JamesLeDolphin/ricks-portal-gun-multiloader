package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.PortalDialerBlock;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PortalDialerBlockEntity extends BlockEntity {
    private BlockPos controllerPos;

    public PortalDialerBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_DIALER, pos, blockState);
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;
        this.setChanged();

        level.setBlock(getBlockPos(), getBlockState().setValue(PortalDialerBlock.CONNECTED, controllerPos != null), 3);
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("ControllerPos")) {
            controllerPos = NbtUtils.readBlockPos(tag.getCompound("ControllerPos"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (controllerPos != null) {
            CompoundTag posTag = NbtUtils.writeBlockPos(controllerPos);
            tag.put("ControllerPos", posTag);
        }
    }
}
