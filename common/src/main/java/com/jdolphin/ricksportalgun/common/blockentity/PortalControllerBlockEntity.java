package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.PortalControllerBlock;
import com.jdolphin.ricksportalgun.common.block.PortalFrameBlock;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class PortalControllerBlockEntity extends BlockEntity {
    private final List<BlockPos> frames = new ArrayList<>();

    public PortalControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_CONTROLLER, pos, blockState);
    }

    public void addFrame(BlockPos pos) {
        frames.add(pos);
    }

    public boolean isAttached() {
        return getBlockState().getValue(PortalControllerBlock.ATTACHED);
    }

    public void removeFrame(BlockPos pos) {
        frames.remove(pos);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        frames.clear();
        if (tag.contains("FramePos")) {
            ListTag listTag = tag.getList("FramePos", Tag.TAG_LONG);
            listTag.forEach(tag1 -> frames.add(BlockPos.of(((LongTag) tag1).getAsLong())));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag listTag;
        if (tag.contains("FramePos")) {
            listTag = tag.getList("FramePos", Tag.TAG_LONG);
        } else listTag = new ListTag();
        frames.forEach(pos -> listTag.add(LongTag.valueOf(pos.asLong())));
        tag.put("FramePos", listTag);

    }

    public void setAttached(boolean attached) {
        frames.forEach(pos -> {
            BlockState state = level.getBlockState(pos);
            level.setBlock(pos, state.setValue(PortalFrameBlock.ATTACHED, attached), 2);
        });
    }
}
