package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.blockentity.PortalFluidStorageBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGFluids;
import com.jdolphin.ricksportalgun.common.util.platform.services.IFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public class FabricFluidStorage extends SingleFluidStorage implements IFluidStorage {
    private int buckets;
    private PortalFluidStorageBlockEntity be;

    private ContainerItemContext fluidItemContext;

    protected FabricFluidStorage(PortalFluidStorageBlockEntity blockEntity, int buckets) {
        super();
        this.be = blockEntity;
        this.buckets = buckets;

        InventoryStorage inventoryStorage = InventoryStorage.of(be.getInventory(), null);
        fluidItemContext = ContainerItemContext.ofSingleSlot(inventoryStorage.getSlot(0));
    }

    //Contructor only for classloading purposes. Do not call.
    public FabricFluidStorage() {

    }

    @Override
    protected void onFinalCommit() {
        super.onFinalCommit();
        be.setChanged();
    }

    @Override
    public IFluidStorage create(PortalFluidStorageBlockEntity blockEntity, int buckets) {
        return new FabricFluidStorage(blockEntity, buckets);
    }

    @Override
    public Fluid[] getAllowedFluids() {
        return new Fluid[]{PGFluids.PORTAL_FLUID.getA(), PGFluids.BOOTLEG_PORTAL_FLUID.getA(), PGFluids.QUANTUM_LEAP_ELIXIR.getA()};
    }

    @Override
    public void tickStorage(Level level, BlockPos pos) {
        if (!this.be.getInventory().isEmpty()) {

            Storage<FluidVariant> itemFluidStorage = this.fluidItemContext.find(FluidStorage.ITEM);
            if (itemFluidStorage != null) {
                FluidVariant match = null;
                for (StorageView<FluidVariant> storageView : itemFluidStorage.nonEmptyViews()) {
                    if (storageView.isResourceBlank())
                        continue;

                    try (Transaction transaction = Transaction.openOuter()) {
                        if (this.insert(storageView.getResource(), FluidConstants.BUCKET, transaction) > 0) {
                            match = storageView.getResource();
                            break;
                        }
                    }
                }

                if (match != null && !match.isBlank()) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        long inserted = this.insert(match, FluidConstants.BUCKET, transaction);
                        long extracted = itemFluidStorage.extract(match, inserted, transaction);

                        if (extracted < FluidConstants.BUCKET) {
                            long extra = FluidConstants.BUCKET - extracted;
                            this.extract(match, extra, transaction); // Take any extra fluid
                        }

                        transaction.commit();
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag1 = new CompoundTag();
        this.writeNbt(tag1);
        return tag1;
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("FluidTank")) {
            this.readNbt(tag.getCompound("FluidTank"));
        }
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void invalidateCaps() {

    }

    @Override
    public Optional<Object> getCapability(Object object) {
        return Optional.empty();
    }

    @Override
    public void setFluid(Object fluidStack) {

    }

    @Override
    protected long getCapacity(FluidVariant variant) {
        return this.buckets * FluidConstants.BUCKET;
    }
}
