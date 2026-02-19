package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.blockentity.PortalFluidStorageBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CBFluidSyncPacket {
    private final FluidStack fluidStack;
    private final BlockPos pos;

    public CBFluidSyncPacket(FluidStack fluidStack, BlockPos pos) {
        this.fluidStack = fluidStack;
        this.pos = pos;
    }

    public CBFluidSyncPacket(FriendlyByteBuf buf) {
        this.fluidStack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof PortalFluidStorageBlockEntity blockEntity) {
                blockEntity.fluidStorage.setFluid(this.fluidStack);
            }
        });
        return true;
    }
}