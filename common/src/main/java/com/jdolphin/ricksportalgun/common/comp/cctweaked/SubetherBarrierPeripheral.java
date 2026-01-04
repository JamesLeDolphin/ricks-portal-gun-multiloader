package com.jdolphin.ricksportalgun.common.comp.cctweaked;

import com.jdolphin.ricksportalgun.common.block.SubetherBarrierBlock;
import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SubetherBarrierPeripheral implements IPeripheral {
    private final SubetherBarrierBlockEntity be;

    public SubetherBarrierPeripheral(SubetherBarrierBlockEntity be) {
        this.be = be;
    }

    @LuaFunction(mainThread = true)
    public final String getCode() {
        return be.getCode();
    }

    @LuaFunction(mainThread = true)
    public final boolean isActive() {
        BlockState state = be.getBlockState();
        return state.hasProperty(SubetherBarrierBlock.ACTIVE) && state.getValue(SubetherBarrierBlock.ACTIVE) && !be.getCode().isEmpty();
    }

    @LuaFunction(mainThread = true)
    public final void setCode(String code) {
        be.setCode(code);
    }


    @Override
    public String getType() {
        return PGHelper.id("subether_barrier").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other == this;
    }
}
