package com.jdolphin.ricksportalgun.common.fluid;

import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class BootlegPortalFluid extends PortalFluid {

    @Override
    public Fluid getFlowing() {
        return PGServices.PLATFORM.getFlowingFluid("bootleg");
    }

    @Override
    public Fluid getSource() {
        return PGServices.PLATFORM.getStillFluid("bootleg");
    }

    @Override
    public Item getBucket() {
        return Items.AIR;
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
