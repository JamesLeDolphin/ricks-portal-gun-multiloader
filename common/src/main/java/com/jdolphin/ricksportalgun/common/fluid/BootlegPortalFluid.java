package com.jdolphin.ricksportalgun.common.fluid;

import com.jdolphin.ricksportalgun.common.init.PGFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class BootlegPortalFluid extends PortalFluid {

    @Override
    public Fluid getFlowing() {
        return PGFluids.BOOTLEG_PORTAL_FLUID.getB();
    }

    @Override
    public Fluid getSource() {
        return PGFluids.BOOTLEG_PORTAL_FLUID.getA();
    }

    @Override
    public Item getBucket() {
        return Items.AIR;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isSame(PGFluids.BOOTLEG_PORTAL_FLUID.getA());
    }

    public boolean isSame(Fluid fluid) {
        return fluid == PGFluids.BOOTLEG_PORTAL_FLUID.getA() || fluid == PGFluids.BOOTLEG_PORTAL_FLUID.getB();
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    public static class Flowing extends BootlegPortalFluid {
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends BootlegPortalFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
