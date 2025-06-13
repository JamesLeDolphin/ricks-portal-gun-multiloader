package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.menu.workbench.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.menu.workbench.WaypointTransferMenu;
import com.jdolphin.ricksportalgun.common.menu.workbench.WorkbenchCraftingMenu;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class PGMenuTypes {
    private static final Map<ResourceLocation, MenuType<?>> ALL = new HashMap<>();

    public static MenuType<PortalDispenserMenu> PORTAL_DISPENSER = register("portal_dispenser",
            PortalDispenserMenu::new);

    public static MenuType<WorkbenchCraftingMenu> WORKBENCH_CRAFTING = register("workbench_crafting",
            WorkbenchCraftingMenu::new);
    public static MenuType<SkinSelectorMenu> WORKBENCH_SKIN_SELECTOR = register("skin_selector",
            SkinSelectorMenu::new);
    public static MenuType<WaypointTransferMenu> WORKBENCH_WAYPOINT_TRANSFER = register("waypoint_transfer",
            WaypointTransferMenu::new);

    private static <M extends AbstractContainerMenu> MenuType<M> register(String name, BiFunction<Integer, Inventory, M> constructor) {
        MenuType<M> menu = PGServices.PLATFORM.createMenuType(constructor);
        ALL.put(PGHelper.createLocation(name), menu);
        return menu;
    }

    public static void init(BiConsumer<MenuType<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
}
