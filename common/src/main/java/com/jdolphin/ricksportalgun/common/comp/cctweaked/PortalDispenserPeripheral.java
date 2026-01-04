package com.jdolphin.ricksportalgun.common.comp.cctweaked;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public class PortalDispenserPeripheral implements IPeripheral {
    private final PortalDispenserBlockEntity be;

    public PortalDispenserPeripheral(PortalDispenserBlockEntity be ) {
        this.be = be;
    }

    @LuaFunction(mainThread = true)
    public final String getDestDim() {
        return be.getDestinationDim();
    }

    @LuaFunction(mainThread = true)
    public final void setDestDim(String dimension) {
        be.setDimension(dimension);
        be.setChanged();
    }

    @LuaFunction(mainThread = true)
    public final void setColor(int color) {
        be.setColor(color);
    }

    public final int getColor() {
        return be.getColor();
    }

    @LuaFunction(mainThread = true)
    public final int getFuel() {
        return be.getFuel();
    }

    @LuaFunction(mainThread = true)
    public final int getMaxFuel() {
        return be.getMaxFuel();
    }

    @LuaFunction(mainThread = true)
    public final boolean hasFuel() {
        return be.hasFuel();
    }

    @LuaFunction(mainThread = true)
    public final void activate() {
        be.onActivation();
        be.setChanged();
    }

    @LuaFunction(mainThread = true)
    public final Object[] getDestPos() {
        BlockPos pos = be.getDestinationPos();
        return new Object[]{pos.getX(), pos.getY(), pos.getZ()};
    }

    @LuaFunction(mainThread = true)
    public final void setDestPos(int x, int y, int z) {
        be.setDestPos(new BlockPos(x, y, z));
        be.setChanged();
    }

    @Override
    public String getType() {
        return PGHelper.id("portal_dispenser").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return iPeripheral == this;
    }
}
