package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.blockentity.PortalFluidStorageBlockEntity;
import com.jdolphin.ricksportalgun.common.init.ForgeFluids;
import com.jdolphin.ricksportalgun.common.init.ForgePackets;
import com.jdolphin.ricksportalgun.common.packets.CBFluidSyncPacket;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import com.jdolphin.ricksportalgun.common.util.platform.services.IFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Arrays;
import java.util.Optional;

public class ForgeFluidStorage extends FluidTank implements IFluidStorage {
    private BlockEntity blockEntity;
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    private final FluidTank FLUID_TANK = new FluidTank(4000) {
        @Override
        protected void onContentsChanged() {
            if (blockEntity != null) {

                if (!blockEntity.getLevel().isClientSide()) {
                    ForgePackets.sendToClients(new CBFluidSyncPacket(this.fluid, blockEntity.getBlockPos()));
                    blockEntity.setChanged();
                }
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == PGServices.PLATFORM.getStillFluid("portal_fluid") || stack.getFluid() == PGServices.PLATFORM.getFlowingFluid("portal_fluid");
        }
    };

    protected ForgeFluidStorage(int capacity) {
        super(capacity);
    }

    //Classloader constructor, do not use!
    public ForgeFluidStorage() {
        super(0);
    }

    @Override
    protected void onContentsChanged() {
        blockEntity.setChanged();
        super.onContentsChanged();
    }

    @Override
    public Fluid[] getAllowedFluids() {
        return new Fluid[]{ForgeFluids.PORTAL_FLUID.get(), ForgeFluids.BOOTLEG_PORTAL_FLUID.get(), ForgeFluids.QUANTUM_LEAP_ELIXIR.get()};
    }

    @Override
    public void tickStorage(Level level, BlockPos pos) {

    }

    @Override
    public CompoundTag save() {
        return FLUID_TANK.writeToNBT(new CompoundTag());
    }

    @Override
    public void load(CompoundTag tag) {
        FLUID_TANK.readFromNBT(tag.getCompound("FluidTank"));
    }

    @Override
    public void onLoad() {
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    @Override
    public void invalidateCaps() {
        lazyFluidHandler.invalidate();
    }

    @Override
    public Optional<Object> getCapability(Object object) {
        if (object instanceof Capability<?> cap) {
            if (cap == ForgeCapabilities.FLUID_HANDLER) {
                return lazyFluidHandler.cast().resolve();
            }
        }
        return Optional.empty();
    }

    @Override
    public void setFluid(Object fluidStack) {
        FLUID_TANK.setFluid(((FluidStack) fluidStack));
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return Arrays.stream(getAllowedFluids()).toList().contains(stack.getFluid());
    }

    @Override
    public IFluidStorage create(PortalFluidStorageBlockEntity blockEntity, int buckets) {
        this.blockEntity = blockEntity;
        return new ForgeFluidStorage(buckets);
    }
}
