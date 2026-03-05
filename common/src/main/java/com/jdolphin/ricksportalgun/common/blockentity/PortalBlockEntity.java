package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.PortalControllerBlock;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

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
        if (tag.contains("OwnerPos")) {
            CompoundTag posTag = tag.getCompound("OwnerPos");
            ownerControllerPos = NbtUtils.readBlockPos(posTag);
        }
        destinationDimension = tag.getString("DestDim");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (destinationControllerPos != null) {
            CompoundTag posTag = NbtUtils.writeBlockPos(destinationControllerPos);
            tag.put("DestPos", posTag);
        }
        if (ownerControllerPos != null) {
            CompoundTag posTag = NbtUtils.writeBlockPos(ownerControllerPos);
            tag.put("OwnerPos", posTag);
        }
        tag.putString("DestDim", destinationDimension);
    }

    public void teleportEntity(Entity entity) {
        if (level instanceof ServerLevel serverLevel) {
            if (destinationControllerPos != null && destinationDimension != null && !destinationDimension.isEmpty()) {
                if (entity.canChangeDimensions()) {
                    ResourceKey<Level> resourceKey = LevelHelper.getWorldKey(new ResourceLocation(destinationDimension));
                    ServerLevel destinationLevel = LevelHelper.getServerWorld(serverLevel, resourceKey);
                    if (destinationLevel != null) {
                        ChunkPos chunkPos = new ChunkPos(destinationControllerPos);
                        destinationLevel.setChunkForced(chunkPos.x, chunkPos.z, true);

                        BlockState destinationControllerState = destinationLevel.getBlockState(destinationControllerPos);
                        if (destinationControllerState.is(PGBlocks.PORTAL_CONTROLLER)) {
                            BlockPos safePos = destinationControllerPos.relative(destinationControllerState.getValue(PortalControllerBlock.FACING), 2).above();
                            BlockState state = level.getBlockState(ownerControllerPos);

                            Direction entranceDir = state.getValue(PortalControllerBlock.FACING).getOpposite();
                            Direction exitDir = destinationControllerState.getValue(PortalControllerBlock.FACING);

                            float entranceRot = Mth.wrapDegrees(entranceDir.toYRot());
                            float exitRot = Mth.wrapDegrees(exitDir.toYRot());
                            float diff = entity.getYRot() - entranceRot;
                            float destRot = Mth.wrapDegrees(exitRot - diff);

                            Vec3 vel = entity.getDeltaMovement();

                            Vec3 updatedVel = vel.yRot(Mth.wrapDegrees(180 + exitRot - entranceRot));
                            entity.setDeltaMovement(Vec3.ZERO);
                            entity.setDeltaMovement(updatedVel);

                            entity.teleportTo(destinationLevel, safePos.getX(), safePos.getY(), safePos.getZ(), Set.of(),
                                    destRot, entity.getXRot());
                        }
                    }
                }
            }
        }
    }

    public void onEntityInside(Entity entity) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos pos = getOwnerControllerPos();
            BlockEntity be = serverLevel.getBlockEntity(pos);
            if (be instanceof PortalControllerBlockEntity controller) {
                controller.queueForTeleport(entity);
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
