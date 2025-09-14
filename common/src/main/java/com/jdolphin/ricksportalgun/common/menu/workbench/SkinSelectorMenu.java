package com.jdolphin.ricksportalgun.common.menu.workbench;

import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class SkinSelectorMenu extends AbstractWorkbenchMenu {
    private Container container;
    private final ContainerLevelAccess access;

    public SkinSelectorMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(10), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public SkinSelectorMenu(int i, Inventory inventory, Container container, ContainerData data, ContainerLevelAccess access) {
        super(PGMenuTypes.WORKBENCH_SKIN_SELECTOR, i, access);
        this.container = container;
        this.access = access;
        checkContainerDataCount(data, 2);
        checkContainerSize(container, 10);

        addInventoryExtendedSlots(inventory, 25, 110);
        addInventoryHotbarSlots(inventory, 25, 168);

        addSlots(container);
    }

    public void setPortalGunType(Player player, ResourceLocation type, int tints) {
        this.access.execute((level, pos) -> {

            ItemStack gun = this.getSlot(36).getItem();
            ItemStack dye1 = this.getSlot(37).getItem();
            ItemStack dye2 = this.getSlot(38).getItem();
            CompoundTag tag = gun.getOrCreateTag();
            int primary = 0;
            int secondary = 0;

            if (!(tints < 2) && !dye1.isEmpty()) {
                if (!dye1.is(Items.WATER_BUCKET)) {
                    DyeItem dyeItem = (DyeItem) dye1.getItem();
                    primary = PGHelper.getTextureDiffuseColor(dyeItem);
                    if (!player.isCreative()) dye1.shrink(1);
                } else {
                    tag.remove(PGNbtKeys.PRIMARY_COLOR);
                    if (!player.isCreative()) getSlot(37).set(dye1.getItem().getCraftingRemainingItem().getDefaultInstance());
                }
            }
            if (!(tints < 3) && !dye2.isEmpty()) {
                if (!dye2.is(Items.WATER_BUCKET)) {
                    DyeItem dyeItem = (DyeItem) dye2.getItem();
                    secondary = PGHelper.getTextureDiffuseColor(dyeItem);
                    if (!player.isCreative()) dye2.shrink(1);
                } else {
                    tag.remove(PGNbtKeys.SECONDARY_COLOR);
                    if (!player.isCreative()) getSlot(38).set(dye2.getItem().getCraftingRemainingItem().getDefaultInstance());
                }
            }
            Item newType = BuiltInRegistries.ITEM.get(type);
            ItemStack newStack = newType.getDefaultInstance();
            newStack.setTag(tag);
            this.getSlot(36).set(newStack);
            if (primary != 0) PortalGunItem.setPrimaryDye(newStack, primary);
            if (secondary != 0) PortalGunItem.setSecondaryDye(newStack, secondary);
        });
    }

    protected void addSlots(Container container) {
        Function<ItemStack, Boolean> bool = stack -> stack.getItem() instanceof DyeItem || stack.is(Items.WATER_BUCKET);
        this.addSlot(new Slot(container, 7, 25, 73) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(PGTags.Items.PORTAL_GUNS);
            }
        });
        this.addSlot(new Slot(container, 8, 55, 73) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return bool.apply(stack);
            }
        });

        this.addSlot(new Slot(container, 9, 85, 73) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return bool.apply(stack);
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        Slot fromSlot = getSlot(i);
        ItemStack fromStack = fromSlot.getItem();

        if (fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if (!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if (i < 36) {
            // We are inside of the player's inventory
            if (!moveItemStackTo(fromStack, 36, 39, false))
                return ItemStack.EMPTY;
        } else if (i < 39) {
            // We are inside of the block entity inventory
            if (!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else {
            System.err.println("Invalid slot index: " + i);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(player, fromStack);
        return copyFromStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
