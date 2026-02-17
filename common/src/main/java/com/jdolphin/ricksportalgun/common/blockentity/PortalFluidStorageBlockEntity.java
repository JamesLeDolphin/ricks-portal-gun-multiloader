package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import com.jdolphin.ricksportalgun.common.util.platform.services.IFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class PortalFluidStorageBlockEntity extends BlockEntity {
    private int amount = 0;


    private final SimpleContainer inventory = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
        }
    };
    public final IFluidStorage fluidStorage = PGServices.FLUID_STORAGE.create(this, 4);

    public Container getInventory() {
        return inventory;
    }

    public PortalFluidStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_FLUID_TANK, pos, blockState);
    }

    public IFluidStorage getFluidStorage(Direction direction) {
        return fluidStorage;
    }

    protected void baseTick() {
        if (this.level instanceof ServerLevel level) {
            fluidStorage.tickStorage(level, getBlockPos());
        }
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
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setAmount(int amount) {
        this.amount = amount;
        setChanged();
    }

    public void invalidateCaps() {
        try {
            Class<?> clazz = Class.forName("net.minecraft.world.level.block.entity.BlockEntity");
            if (clazz.isInstance(this)) {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                MethodHandle handle = lookup.findSpecial(clazz, "onLoad", MethodType.methodType(void.class), this.getClass());
                handle.bindTo(this).invokeWithArguments();

                Method method = clazz.getMethod("invalidateCaps");
                method.invoke(this);
                this.fluidStorage.invalidateCaps();
            }
        } catch (Throwable ignored) {}
    }

    @Override
    public void setChanged() {
        level.blockEntityChanged(getBlockPos());
        if (!getBlockState().isAir()) {
            level.updateNeighbourForOutputSignal(getBlockPos(), getBlockState().getBlock());
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);

        }
        super.setChanged();

    }

    public void onLoad() {
        try {
            Class<?> clazz = Class.forName("net.minecraft.world.level.block.entity.BlockEntity");
            if (clazz.isInstance(this)) {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                MethodHandle handle = lookup.findSpecial(clazz, "onLoad", MethodType.methodType(void.class), this.getClass());
                handle.bindTo(this).invokeWithArguments();

                Method method = clazz.getMethod("onLoad");
                method.invoke(this);
                this.fluidStorage.onLoad();
            }
        } catch (Throwable ignored) {}
    }

    public int getAmount() {
        return amount;
    }

    public void load(CompoundTag tag) {
        this.amount = tag.getInt(PGNbtKeys.TAG_FUEL);

        if (tag.contains("Inventory")) {
            inventory.fromTag(tag.getList("Inventory", Tag.TAG_COMPOUND));
        }
        this.fluidStorage.load(tag);
    }

    protected void saveAdditional(CompoundTag tag) {
        tag.putInt(PGNbtKeys.TAG_FUEL, amount);

        tag.put("Inventory", this.inventory.createTag());

        CompoundTag tag1 = fluidStorage.save();
        if (tag1 != null) tag.put("FluidTank", tag1);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState blockState, T t) {
        ((PortalFluidStorageBlockEntity) t).baseTick();
    }
}
