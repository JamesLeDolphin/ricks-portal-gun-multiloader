package com.jdolphin.ricksportalgun;

import com.jdolphin.ricksportalgun.common.data.FabricPortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;

public class RicksPortalGunFabricMain implements ModInitializer {

    //public static final MenuType<PortalDispenserMenu> BOX_SCREEN_HANDLER =
    //        Registry.register(BuiltInRegistries.MENU, Helper.createLocation("box_block"), new MenuType<>((PortalDispenserMenu::new), FeatureFlags.DEFAULT_FLAGS));

    @Override
    public void onInitialize() {
        RicksPortalGunCommonMain.init();
        FabricBlocks.register();
        FabricBlockEntities.register();
        FabricItems.register();
        FabricEntities.register();
        FabricDataComponents.register();
        FabricPackets.registerC2SPackets();
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricPortalGunTypeReloadListener());

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
            content.accept(PGItems.PORTAL_GUN);
            content.accept(PGItems.PRIME_PORTAL_GUN);
            content.accept(PGItems.GOLDEN_PORTAL_GUN);
        });


        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(content -> {
            content.accept(PGItems.PORTAL_FLUID);
            content.accept(PGItems.BOOTLEG_PORTAL_FLUID);
            content.accept(PGItems.QUANTUM_LEAP_ELIXIR);
        });
    }
}
