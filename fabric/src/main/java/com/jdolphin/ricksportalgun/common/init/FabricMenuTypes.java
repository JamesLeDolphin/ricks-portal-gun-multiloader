package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class FabricMenuTypes {

    public static void register() {
        PGMenuTypes.PORTAL_DISPENSER = Registry.register(BuiltInRegistries.MENU,
                Helper.createLocation("portal_dispenser"), new MenuType<>((PortalDispenserMenu::new), FeatureFlags.DEFAULT_FLAGS));
    }
}
