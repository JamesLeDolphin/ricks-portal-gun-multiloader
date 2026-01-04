package com.jdolphin.ricksportalgun.common.comp.computercraft;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;

public class PGPeripheralProviderForge implements IPeripheralProvider {

    @Override
    public LazyOptional<IPeripheral> getPeripheral(Level level, BlockPos blockPos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof PortalDispenserBlockEntity be) {
            return LazyOptional.of(() -> (IPeripheral) be.getPeripheral());
        } else {
            return LazyOptional.empty();
        }
    }
}
