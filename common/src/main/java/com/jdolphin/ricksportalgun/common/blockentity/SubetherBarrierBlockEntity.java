package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.SubetherBarrierBlock;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SubetherBarrierBlockEntity extends BlockEntity {
    private String code = "";
    private final String TAG_CODE = "Code";

    public SubetherBarrierBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public SubetherBarrierBlockEntity(BlockPos pos, BlockState blockState) {
        this(PGBlockEntities.SUBETHER_BARRIER, pos, blockState);
    }

    public void setCode(String code) {
        this.code = code;
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        this.code = tag.getString(TAG_CODE);
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putString(TAG_CODE, code);
    }

    public boolean canBlockPortal(Level level, BlockPos pos, String code) {
        return level.getBlockState(pos).getValue(SubetherBarrierBlock.ACTIVE) && !this.code.isEmpty() && !this.code.equals(code);
    }


}
