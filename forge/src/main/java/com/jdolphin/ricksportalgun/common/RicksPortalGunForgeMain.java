package com.jdolphin.ricksportalgun.common;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.RicksPortalGunCommonMain;
import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.data.PortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncGunTypesPacket;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(PGConstants.MODID)
public class RicksPortalGunForgeMain {

    public RicksPortalGunForgeMain(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        RicksPortalGunCommonMain.init();
        MinecraftForge.EVENT_BUS.addListener(this::reloadListenerAddEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerJoin);
        bus.addListener(this::commonSetup);
        bus.addListener(this::buildContents);
        bind(bus, Registries.DATA_COMPONENT_TYPE, PGDataComponents::init);
        bind(bus, Registries.BLOCK, PGBlocks::init);
        bind(bus, Registries.ITEM, PGItems::init);
        bind(bus, Registries.BLOCK_ENTITY_TYPE, PGBlockEntities::init);
        bind(bus, Registries.ENTITY_TYPE, PGEntities::init);
        bind(bus, Registries.MENU, PGMenuTypes::init);
        bind(bus, Registries.RECIPE_TYPE, PGRecipeTypes::init);
        bind(bus, Registries.RECIPE_SERIALIZER, PGRecipeSerializers::init);
    }

    private static <T> void bind(IEventBus bus, ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        bus.addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }


    private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverPlayer.getServer();
            if (server != null) {
                List<String> dims = LevelHelper.getDimensionsAsString(server.getAllLevels());
                if (!dims.contains(PGHelper.createLocation("blender").toString())) dims.add(PGHelper.createLocation("blender").toString());
                CBSyncDimensionListPacket dimSync = new CBSyncDimensionListPacket(dims);
                CBSyncGunTypesPacket typeSync = new CBSyncGunTypesPacket(PortalGunTypeRegistry.PORTAL_GUN_TYPES);
                ForgePackets.sendToPlayer(serverPlayer, dimSync, typeSync);
            }
        }
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        PGCommonConfig.INSTANCE = new PGCommonConfig();
        event.enqueueWork(ForgePackets::init);
    }

    public void reloadListenerAddEvent(AddReloadListenerEvent event) {
        event.addListener(new PortalGunTypeReloadListener());
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