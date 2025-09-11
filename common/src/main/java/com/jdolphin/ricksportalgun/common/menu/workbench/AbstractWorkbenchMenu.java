package com.jdolphin.ricksportalgun.common.menu.workbench;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractWorkbenchMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;

    protected AbstractWorkbenchMenu(@Nullable MenuType<?> menuType, int containerId, ContainerLevelAccess access) {
        super(menuType, containerId);
        this.access = access;
    }

    protected void addInventoryHotbarSlots(Container container, int x, int y) {
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(container, i, x + i * 18, y));
        }

    }

    protected void addInventoryExtendedSlots(Container container, int x, int y) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(container, j + (i + 1) * 9, x + j * 18, y + i * 18));
            }
        }

    }

    public void setMenuType(int i, Player player) {
        access.execute((level, pos) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof GunWorkbenchBlockEntity workbench) {
                if (!level.isClientSide()) {
                    workbench.setMenuType(i);
                    player.openMenu(workbench);
                }
            }
        });
    }
}
