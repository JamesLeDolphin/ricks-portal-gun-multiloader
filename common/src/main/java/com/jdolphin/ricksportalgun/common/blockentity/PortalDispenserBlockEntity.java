package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.PortalDispenserBlock;
import com.jdolphin.ricksportalgun.common.comp.cctweaked.PortalDispenserPeripheral;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class PortalDispenserBlockEntity extends BaseContainerBlockEntity {
    public static final String TAG_DEST_DIM = "DestinationDim";
    public static final String TAG_DEST_BPOS = "DestinationPos";
    public static final String TAG_DIRECTION = "Direction";

    private Object peripheral;
    private Direction dir;
    private int fuel = 16;
    private int maxFuel = 16;
    private int color = Color.GREEN.getRGB();
    private String desDim = "minecraft:overworld";
    private BlockPos desPos = BlockPos.ZERO;
    protected final ContainerData dataAccess;
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public @Nullable Object getPeripheral() {
        return peripheral;
    }

    public PortalDispenserBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_DISPENSER, pos, blockState);
        if (PGHelper.hasCCTweaked()) {
            peripheral = new PortalDispenserPeripheral(this);
        }

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

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setDimension(String dimension) {
        this.desDim = dimension;
    }

    public void setDestPos(BlockPos pos) {
        this.desPos = pos;
    }

    public void setDestination(String dimension, BlockPos pos) {
        this.desDim = dimension;
        this.desPos = pos;
        this.setChanged();
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
            ItemStack stack = this.items.get(0);
            if (stack.is(PGItems.PORTAL_FLUID)) {
                stack.shrink(1);
                this.fuel = maxFuel;
            }
        }
        super.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu.ricksportalgun.portal_dispenser");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new PortalDispenserMenu(i, inventory, this, dataAccess, ContainerLevelAccess.create(this.level, this.worldPosition), this.desPos, this.desDim);
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

    public void onActivation() {
        assert level != null;
        if (!level.isClientSide) {
            BlockState state = level.getBlockState(getBlockPos());
            Direction direction = state.getValue(PortalDispenserBlock.FACING);
            BlockPos portalPos = getBlockPos();
            if (hasFuel()) {
                for (int j = 0; j < 4; j++) {
                    portalPos = getBlockPos().relative(direction, j);
                    if (!level.getBlockState(portalPos.relative(direction, 1)).isAir()) {
                        break;
                    }
                }
                if (!getDestinationDim().isEmpty() && getDestinationPos() != null) {
                    ResourceLocation dim = new ResourceLocation(getDestinationDim());
                    ServerLevel destLevel = LevelHelper.getServerWorld(level, LevelHelper.getWorldKey(dim));
                    if (LevelHelper.canPortalTo(destLevel, getDestinationPos(), null)) {
                        Vec3 vec = Vec3.atCenterOf(portalPos).add(direction.getStepX() * 0.4, 0.5 + direction.getStepY() * 0.4, direction.getStepZ() * 0.4);
                        PortalEntity portal = new PortalEntity(level, vec, direction, this.dir, 3.0f);
                        PortalEntity exitPortal = new PortalEntity(destLevel, Vec3.atCenterOf(getDestinationPos()).add(0, 0.5, 0), direction, this.dir, 3.0f);
                        PGHelper.doForEach(entity -> entity.setColor(this.color), portal, exitPortal);

                        if (!portal.isFlat()) {
                            PGHelper.doForEach(entity -> entity.setYRot(this.dir.toYRot()),
                                    portal, exitPortal);
                        }
                        portal.setHopLocation(level.dimension().location(), getDestinationPos());
                        exitPortal.setHopLocation(dim, portal.blockPosition());

                        if (destLevel.addFreshEntity(exitPortal)) {
                            level.addFreshEntity(portal);
                            decreaseFuel(1);
                        }
                    }
                }
            }
        }
    }

    public void setDirection(Direction direction) {
        this.dir = direction;
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.fuel = tag.getInt(PGNbtKeys.TAG_FUEL);
        this.maxFuel = tag.getInt(PGNbtKeys.TAG_MAX_FUEL);
        CompoundTag bpTag = tag.getCompound(TAG_DEST_BPOS);
        this.desPos = NbtUtils.readBlockPos(bpTag);
        this.desDim = tag.getString(TAG_DEST_DIM);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.dir = Direction.fromYRot(tag.getDouble(TAG_DIRECTION));
        this.color = tag.getInt(PGNbtKeys.TAG_COLOR);
        ContainerHelper.loadAllItems(tag, this.items);

    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt(PGNbtKeys.TAG_FUEL, this.fuel);
        tag.putInt(PGNbtKeys.TAG_MAX_FUEL, this.maxFuel);
        tag.putString(TAG_DEST_DIM, this.desDim);
        tag.put(TAG_DEST_BPOS, NbtUtils.writeBlockPos(this.desPos));
        tag.putDouble(TAG_DIRECTION, this.dir.toYRot());
        tag.putInt(PGNbtKeys.TAG_COLOR, color);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return null;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        items.set(i, itemStack);
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}
