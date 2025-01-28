package com.jdolphin.ricksportalgun.common;

import com.jdolphin.ricksportalgun.Constants;
import com.jdolphin.ricksportalgun.RicksPortalGunCommonMain;
import com.jdolphin.ricksportalgun.common.data.PortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import com.jdolphin.ricksportalgun.common.util.tint.PortalColourTint;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public class RicksPortalGunForgeMain {

    public RicksPortalGunForgeMain(FMLJavaModLoadingContext modLoadingContext) {
        IEventBus bus = modLoadingContext.getModEventBus();
        RicksPortalGunCommonMain.init();
        MinecraftForge.EVENT_BUS.addListener(this::reloadListenerAddEvent);
        bus.addListener(this::commonSetup);
        bus.addListener(this::loadComplete);
        ForgeDataComponents.COMPONENTS.register(bus);
        ForgeItems.ITEMS.register(bus);
        ForgeEntities.ENTITIES.register(bus);
        ItemTintSources.ID_MAPPER.put(Helper.createLocation("portal_color"), PortalColourTint.CODEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ForgePackets::init);
    }

    public void reloadListenerAddEvent(AddReloadListenerEvent event) {
        event.addListener(new PortalGunTypeReloadListener());
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        PGItems.PORTAL_GUN = ForgeItems.PORTAL_GUN.get();
        PGItems.PRIME_PORTAL_GUN = ForgeItems.PRIME_PORTAL_GUN.get();
        PGItems.GOLDEN_PORTAL_GUN = ForgeItems.GOLDEN_PORTAL_GUN.get();

        PGItems.PORTAL_FLUID = ForgeItems.PORTAL_FLUID_BOTTLE.get();
        PGItems.BOOTLEG_PORTAL_FLUID = ForgeItems.BOOTLEG_PORTAL_FLUID_BOTTLE.get();
        PGItems.QUANTUM_LEAP_ELIXIR = ForgeItems.QUANTUM_LEAP_ELIXIR.get();

        PGEntities.PORTAL = ForgeEntities.PORTAL.get();

        PGDataComponents.PORTAL_GUN_TYPE = ForgeDataComponents.PORTAL_GUN_TYPE.get();
        PGDataComponents.PORTAL_POS = ForgeDataComponents.PORTAL_POS.get();
        PGDataComponents.PORTAL_DIM = ForgeDataComponents.PORTAL_DIM.get();
        PGDataComponents.LOCK = ForgeDataComponents.LOCK.get();
        PGDataComponents.BOOTLEG = ForgeDataComponents.BOOTLEG.get();
        PGDataComponents.WAYPOINTS = ForgeDataComponents.WAYPOINTS.get();
        PGDataComponents.MAX_FUEL = ForgeDataComponents.MAX_FUEL.get();
        PGDataComponents.DEFAULT_COLOUR = ForgeDataComponents.DEFAULT_COLOUR.get();
        PGDataComponents.OWNER = ForgeDataComponents.OWNER.get();
        PGDataComponents.FUEL = ForgeDataComponents.FUEL.get();
        PGDataComponents.PORTAL_COLOUR = ForgeDataComponents.PORTAL_COLOUR.get();
    }

    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> key = event.getTabKey();
        if (key.equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            event.accept(ForgeItems.PORTAL_FLUID_BOTTLE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ForgeItems.QUANTUM_LEAP_ELIXIR.get());
            event.accept(ForgeItems.BOOTLEG_PORTAL_FLUID_BOTTLE.get());
        }
        //if (event.getTab().equals(PGCreativeTabs.UPGRADE_TAB.get())) {
        //    event.accept(PGItems.UPGRADE_TEMPLATE.get());
        //    event.accept(PGItems.DIM_1_UPGRADE.get());
        //    event.accept(PGItems.DIM_2_UPGRADE.get());
        //    event.accept(PGItems.WAYPOINT_UPGRADE.get());
        //    event.accept(PGItems.FUEL_UPGRADE.get());
        //    event.accept(PGItems.SETTINGS_UPGRADE.get());
        //    event.accept(PGItems.PLAYER_LOCATOR_UPGRADE.get());
        //    event.accept(PGItems.BIOME_LOCATOR_UPGRADE.get());
        //    event.accept(PGItems.RANDOMIZER_UPGRADE.get());
        //}
        if (key.equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(ForgeItems.PORTAL_GUN.get());
            event.accept(ForgeItems.PRIME_PORTAL_GUN.get());
            event.accept(ForgeItems.GOLDEN_PORTAL_GUN.get());
        }
        if (key.equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            //event.accept(ForgeItems.WORKBENCH_ITEM.get());
        }
        if (key.equals(CreativeModeTabs.INGREDIENTS)) {
            //event.accept(PGItems.DATA_CARD.get());
        }

    }
}