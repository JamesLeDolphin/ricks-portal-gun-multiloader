package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PortalFluidStorageBlockEntity extends BlockEntity {
    private int amount = 0;

    public PortalFluidStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_FLUID_TANK, pos, blockState);
    }

    public int getMaxAmount() {
        return 256;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setAmount(int amount) {
        this.amount = amount;
        setChanged();
    }

    public int getAmount() {
        return amount;
    }

    public void load(CompoundTag tag) {
        this.amount = tag.getInt(PGNbtKeys.TAG_FUEL);
    }

    protected void saveAdditional(CompoundTag tag) {
        tag.putInt(PGNbtKeys.TAG_FUEL, amount);
    }

}
