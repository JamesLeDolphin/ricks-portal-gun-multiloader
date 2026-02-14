package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.util.platform.services.IFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;

public class FabricFluidStorage extends SingleFluidStorage implements IFluidStorage {

    @Override
    public IFluidStorage create() {
        return null;
    }

    @Override
    protected long getCapacity(FluidVariant variant) {
        return 4 * FluidConstants.BUCKET;
    }
}
