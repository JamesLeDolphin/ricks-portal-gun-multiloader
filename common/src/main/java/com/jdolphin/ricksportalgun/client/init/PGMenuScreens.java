package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.client.screen.PortalDispenserScreen;
import com.jdolphin.ricksportalgun.client.screen.workbench.SkinSelectingScreen;
import com.jdolphin.ricksportalgun.client.screen.workbench.WaypointTransferScreen;
import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.function.TriFunction;

import java.util.HashMap;
import java.util.Map;

public class PGMenuScreens {

    public static final Map<MenuType<? extends AbstractContainerMenu>, TriFunction<AbstractContainerMenu, Inventory, Component, Screen>> ALL = new HashMap<>();

    static {
        register(PGMenuTypes.PORTAL_DISPENSER, PortalDispenserScreen::new);
        register(PGMenuTypes.WORKBENCH_CRAFTING, WorkbenchCraftingScreen::new);
        register(PGMenuTypes.WORKBENCH_SKIN_SELECTOR, SkinSelectingScreen::new);
        register(PGMenuTypes.WORKBENCH_WAYPOINT_TRANSFER, WaypointTransferScreen::new);
    }

    @SuppressWarnings("unchecked")
    private static <M extends AbstractContainerMenu> void register(MenuType<M> menu, TriFunction<M, Inventory, Component, Screen> screen) {
        ALL.put(menu, (TriFunction<AbstractContainerMenu, Inventory, Component, Screen>) screen);
    }
}
