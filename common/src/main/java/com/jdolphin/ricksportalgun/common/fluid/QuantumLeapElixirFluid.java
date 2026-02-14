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

public abstract class QuantumLeapElixirFluid extends PortalFluid {

    @Override
    public Fluid getFlowing() {
        return PGFluids.QUANTUM_LEAP_ELIXIR.getB();
    }

    @Override
    public Fluid getSource() {
        return PGFluids.QUANTUM_LEAP_ELIXIR.getA();
    }

    @Override
    public Item getBucket() {
        return Items.AIR;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isSame(PGFluids.QUANTUM_LEAP_ELIXIR.getA());
    }

    public boolean isSame(Fluid fluid) {
        return fluid == PGFluids.QUANTUM_LEAP_ELIXIR.getA() || fluid == PGFluids.QUANTUM_LEAP_ELIXIR.getB();
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    public static class Flowing extends QuantumLeapElixirFluid {
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

    public static class Source extends QuantumLeapElixirFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
