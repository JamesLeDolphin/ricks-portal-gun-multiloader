package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.PortalDispenserBlock;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.MathHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class PortalDispenserBlockEntity extends BaseContainerBlockEntity {
    public static final String TAG_FUEL = "Fuel";
    public static final String TAG_MAX_FUEL = "MaxFuel";
    public static final String TAG_COLOR = "Color";
    public static final String TAG_DEST_DIM = "DestinationDim";
    public static final String TAG_DEST_BPOS = "DestinationPos";
    public static final String TAG_DIRECTION = "Direction";
    private Direction dir;
    private int fuel = 16;
    private int maxFuel = 16;
    private String desDim = "minecraft:overworld";
    private BlockPos desPos = BlockPos.ZERO;
    protected final ContainerData dataAccess;
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public PortalDispenserBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_DISPENSER, pos, blockState);

        this.dataAccess = new ContainerData() {
            public int get(int i) {
                switch (i) {
                    case 0 -> {
                        return PortalDispenserBlockEntity.this.fuel;
                    }
                    case 1 -> {
                        return PortalDispenserBlockEntity.this.maxFuel;
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PortalDispenserBlockEntity.this.fuel = value;
                    case 1 -> PortalDispenserBlockEntity.this.maxFuel = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    public void setDestination(String dimension, BlockPos pos) {
        this.desDim = dimension;
        this.desPos = pos;
    }

    public BlockPos getDestinationPos() {
        return this.desPos;
    }

    public String getDestinationDim() {
        return this.desDim;
    }

    @Override
    public void setChanged() {
        if (!hasFuel()) {
            ItemStack stack = this.items.getFirst();
            if (stack.is(PGItems.PORTAL_FLUID)) {
                stack.shrink(1);
                this.fuel = maxFuel;
            }
        }
        super.setChanged();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu.ricksportalgun.portal_dispenser");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        this.items = list;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new PortalDispenserMenu(i, inventory, this, dataAccess, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    public int getFuel() {
        return this.fuel;
    }

    public boolean hasFuel() {
        return this.fuel > 0;
    }

    public int getMaxFuel() {
        return this.maxFuel;
    }

    public void setFuel(int fuel) {
        this.fuel = Math.min(getMaxFuel(), fuel);
    }

    public void decreaseFuel(int amount) {
        this.fuel = Math.max(0, this.fuel - amount);
    }

    //TODO: Check for barrier
    public void onActivation(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            BlockState state = level.getBlockState(pos);
            Direction direction = state.getValue(PortalDispenserBlock.FACING);
            BlockPos portalPos = pos;
            if (hasFuel()) {
                for (int j = 0; j < 4; j++) {
                    portalPos = pos.relative(direction, j);
                    if (!level.getBlockState(portalPos.relative(direction, 1)).isAir()) {
                        break;
                    }
                }
                ResourceLocation dim = ResourceLocation.parse(getDestinationDim());
                ServerLevel destLevel = LevelHelper.getServerWorld(level, LevelHelper.getWorldKey(dim));

                Vec3 vec = Vec3.atCenterOf(portalPos).add(direction.getStepX() * 0.4, direction.getStepY() * 0.4, direction.getStepZ() * 0.4);
                PortalEntity portal = new PortalEntity(level, vec, direction, this.dir, 3.0f);
                PortalEntity exitPortal = new PortalEntity(destLevel, new Vec3(getDestinationPos()), direction, this.dir, 3.0f);
                if (!portal.isFlat()) {
                    portal.setYRot(this.dir.toYRot());
                    exitPortal.setYRot(this.dir.toYRot());
                }
                portal.setHopLocation(dim, getDestinationPos());
                exitPortal.setHopLocation(dim, portal.blockPosition());

                if (level.addFreshEntity(portal)) {
                    destLevel.addFreshEntity(exitPortal);
                    decreaseFuel(1);
                }
            }
        }
    }

    public void setDirection(Direction direction) {
        this.dir = direction;
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.fuel = tag.getInt(TAG_FUEL);
        this.maxFuel = tag.getInt(TAG_MAX_FUEL);
        this.desPos = NbtUtils.readBlockPos(tag, TAG_DEST_BPOS).orElse(BlockPos.ZERO);
        this.desDim = tag.getString(TAG_DEST_DIM);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.dir = Direction.fromYRot(tag.getDouble(TAG_DIRECTION));
        ContainerHelper.loadAllItems(tag, this.items, registries);

    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt(TAG_FUEL, this.fuel);
        tag.putInt(TAG_MAX_FUEL, this.maxFuel);
        tag.putString(TAG_DEST_DIM, this.desDim);
        tag.put(TAG_DEST_BPOS, NbtUtils.writeBlockPos(this.desPos));
        tag.putDouble(TAG_DIRECTION, this.dir.toYRot());
    }

    @Override
    public int getContainerSize() {
        return 1;
    }
}
