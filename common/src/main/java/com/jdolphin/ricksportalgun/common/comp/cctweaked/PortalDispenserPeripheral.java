package com.jdolphin.ricksportalgun.common.comp.cctweaked;

import dan200.computercraft.api.peripheral.IPeripheral;
import org.jspecify.annotations.Nullable;

public class PortalDispenserPeripheral implements IPeripheral {

    @Override
    public String getType() {
        return "portalDispenserController";
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return iPeripheral == this;
    }
}
