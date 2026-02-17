package com.jdolphin.ricksportalgun.common.util.platform.services;

import com.jdolphin.ricksportalgun.common.blockentity.PortalFluidStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public interface IFluidStorage {
    Runnable onContentsChanged = () -> {};

    IFluidStorage create(PortalFluidStorageBlockEntity blockEntity, int buckets);

    Fluid[] getAllowedFluids();

    void tickStorage(Level level, BlockPos pos);

    CompoundTag save();

    void load(CompoundTag tag);

    void onLoad();

    void invalidateCaps();

    Optional<Object> getCapability(Object object);

    void setFluid(Object fluidStack);
}
