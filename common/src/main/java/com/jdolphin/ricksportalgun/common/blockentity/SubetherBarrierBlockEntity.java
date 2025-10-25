package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.SubetherBarrierBlock;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SubetherBarrierBlockEntity extends BlockEntity {
    private String code = "";
    private final String TAG_CODE = "Code";

    public SubetherBarrierBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.SUBETHER_BARRIER, pos, blockState);
    }

    public void setCode(String code) {
        this.code = code;
        this.setChanged();
    }

    public void load(CompoundTag tag) {
        this.code = tag.getString(TAG_CODE);
    }

    protected void saveAdditional(CompoundTag tag) {
        tag.putString(TAG_CODE, code);
    }

    public boolean canBlockPortal(Level level, BlockPos pos, String code) {
        return level.getBlockState(pos).getValue(SubetherBarrierBlock.ACTIVE) && !this.code.isEmpty() && !this.code.equals(code);
    }
}
