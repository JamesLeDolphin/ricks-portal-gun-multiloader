package com.jdolphin.ricksportalgun.common.fluid;

import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.init.PGFluids;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class PortalFluid extends FlowingFluid {

    @Override
    public Fluid getFlowing() {
        return PGFluids.PORTAL_FLUID.getB();
    }

    @Override
    public Fluid getSource() {
        return PGFluids.PORTAL_FLUID.getA();
    }

    @Override
    public Item getBucket() {
        return PGItems.PORTAL_FLUID_BUCKET;
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 1;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isSame(PGFluids.PORTAL_FLUID.getA());
    }

    public boolean isSame(Fluid fluid) {
        return fluid == PGFluids.PORTAL_FLUID.getA() || fluid == PGFluids.PORTAL_FLUID.getB();
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return PGBlocks.PORTAL_FLUID.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public static class Flowing extends PortalFluid {
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

    public static class Source extends PortalFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
