package com.jdolphin.ricksportalgun.common;


import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.RicksPortalGunCommonMain;
import com.jdolphin.ricksportalgun.common.data.PortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(PGConstants.MODID)
public class RicksPortalGunNeoForgeMain {

    public RicksPortalGunNeoForgeMain(IEventBus bus) {
        RicksPortalGunCommonMain.init();

        NeoForge.EVENT_BUS.addListener(this::reloadListenerAddEvent);
        bus.addListener(this::buildContents);
        bus.addListener(this::registerPackets);
        bind(bus, Registries.DATA_COMPONENT_TYPE, PGDataComponents::init);
        bind(bus, Registries.BLOCK, PGBlocks::init);
        bind(bus, Registries.ITEM, PGItems::init);
        bind(bus, Registries.BLOCK_ENTITY_TYPE, PGBlockEntities::init);
        bind(bus, Registries.ENTITY_TYPE, PGEntities::init);
        bind(bus, Registries.MENU, PGMenuTypes::init);
        bind(bus, Registries.RECIPE_TYPE, PGRecipeTypes::init);
        bind(bus, Registries.RECIPE_SERIALIZER, PGRecipeSerializers::init);
    }

    public void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        NeoForgePackets.init(registrar);
    }

    private static <T> void bind(IEventBus bus, ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        bus.addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }

    public void reloadListenerAddEvent(AddServerReloadListenersEvent event) {
        PortalGunTypeReloadListener listener = new PortalGunTypeReloadListener();
        event.addListener(listener.getID(), listener);
    }

    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> key = event.getTabKey();
        if (key.equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            event.accept(PGItems.PORTAL_FLUID, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(PGItems.QUANTUM_LEAP_ELIXIR);
            event.accept(PGItems.BOOTLEG_PORTAL_FLUID);
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
            event.accept(PGItems.PORTAL_GUN);
            event.accept(PGItems.PRIME_PORTAL_GUN);
            event.accept(PGItems.GOLDEN_PORTAL_GUN);
        }
        if (key.equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            event.accept(PGItems.PORTAL_GUN_WORKBENCH.asItem());
        }
        if (key.equals(CreativeModeTabs.INGREDIENTS)) {
            event.accept(PGItems.DATA_CARD);
        }
    }
}