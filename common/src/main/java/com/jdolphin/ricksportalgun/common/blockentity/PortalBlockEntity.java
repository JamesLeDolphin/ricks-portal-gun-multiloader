package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.PortalControllerBlock;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class PortalBlockEntity extends BlockEntity {
    private BlockPos ownerControllerPos;
    private BlockPos destinationControllerPos;
    private String destinationDimension = "";

    public PortalBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL, pos, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("DestPos")) {
            CompoundTag posTag = tag.getCompound("DestPos");
            destinationControllerPos = NbtUtils.readBlockPos(posTag);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (destinationControllerPos != null) {
            CompoundTag posTag = NbtUtils.writeBlockPos(destinationControllerPos);
            tag.put("DestPos", posTag);
        }
    }

    public void onEntityInside(Entity entity) {
        if (level instanceof ServerLevel serverLevel) {
            if (destinationControllerPos != null && destinationDimension != null) {
                if (!entity.isOnPortalCooldown() && entity.canChangeDimensions()) {
                    ResourceKey<Level> resourceKey = LevelHelper.getWorldKey(new ResourceLocation(destinationDimension));
                    ServerLevel destinationLevel = LevelHelper.getServerWorld(serverLevel, resourceKey);
                    if (destinationLevel != null) {
                        ChunkPos chunkPos = new ChunkPos(destinationControllerPos);
                        destinationLevel.setChunkForced(chunkPos.x, chunkPos.z, true);

                        BlockState state = destinationLevel.getBlockState(destinationControllerPos);
                        if (state.is(PGBlocks.PORTAL_CONTROLLER)) {
                            BlockPos safePos = destinationControllerPos.above().relative(state.getValue(PortalControllerBlock.FACING));
                            entity.teleportTo(destinationLevel, safePos.getX(), safePos.getY(), safePos.getZ(), Set.of(), entity.getYRot(), entity.getXRot());
                        }
                    }
                }
            }
        }
    }

    public BlockPos getOwnerControllerPos() {
        return ownerControllerPos;
    }

    public void setOwnerControllerPos(BlockPos ownerControllerPos) {
        this.ownerControllerPos = ownerControllerPos;
    }

    public void setDestinationDimension(String destinationDimension) {
        this.destinationDimension = destinationDimension;
    }

    public String getDestinationDimension() {
        return destinationDimension;
    }

    public void setDestinationControllerPos(BlockPos destinationControllerPos) {
        this.destinationControllerPos = destinationControllerPos;
    }

    public BlockPos getDestinationControllerPos() {
        return destinationControllerPos;
    }


}
