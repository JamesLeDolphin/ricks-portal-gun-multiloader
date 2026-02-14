package com.jdolphin.ricksportalgun.common.storage;

import com.jdolphin.ricksportalgun.common.util.platform.services.IFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;

public class PortalFluidStorage extends SingleFluidStorage implements IFluidStorage {

    public IFluidStorage create() {
        return new PortalFluidStorage();
    }


    @Override
    protected long getCapacity(FluidVariant variant) {
        return 4 * FluidConstants.BUCKET;
    }
}
