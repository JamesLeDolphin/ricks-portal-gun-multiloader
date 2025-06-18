package com.jdolphin.ricksportalgun.common.menu.workbench;

import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

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

    public void setPortalGunType(Player player, PortalGunType type) {
        this.access.execute((level, pos) -> {

            ItemStack gun = this.getSlot(36).getItem();
            ItemStack dye1 = this.getSlot(37).getItem();
            ItemStack dye2 = this.getSlot(38).getItem();

            int primary = 0;
            int secondary = 0;

            if (!dye1.isEmpty()) {
                DyeItem dyeItem = (DyeItem) dye1.getItem();
                primary = dyeItem.getDyeColor().getTextureDiffuseColor();
                if (!player.isCreative()) dye1.shrink(1);
            }
            if (!type.monoTone() && !dye2.isEmpty()) {
                DyeItem dyeItem = (DyeItem) dye2.getItem();
                secondary = dyeItem.getDyeColor().getTextureDiffuseColor();
                if (!player.isCreative()) dye2.shrink(1);
            }
            PortalGunItem.setPortalGunType(gun, type);
            if (primary != 0) PortalGunItem.setPrimaryDye(gun, primary);
            if (secondary != 0) PortalGunItem.setSecondaryDye(gun, secondary);
        });
    }

    protected void addSlots(Container container) {
        this.addSlot(new Slot(container, 7, 25, 73) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(PGTags.Items.PORTAL_GUNS);
            }
        });
        this.addSlot(new Slot(container, 8, 55, 73) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }
        });

        this.addSlot(new Slot(container, 9, 85, 73) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
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
