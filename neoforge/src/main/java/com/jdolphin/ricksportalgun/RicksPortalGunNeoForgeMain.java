package com.jdolphin.ricksportalgun;


import com.jdolphin.ricksportalgun.common.config.PGClientConfig;
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
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(PGConstants.MODID)
public class RicksPortalGunNeoForgeMain {

    public RicksPortalGunNeoForgeMain(IEventBus bus) {
        RicksPortalGunCommonMain.init();

        NeoForge.EVENT_BUS.addListener(this::reloadListenerAddEvent);
        NeoForge.EVENT_BUS.addListener(this::onPlayerJoin);
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

        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, PGCommonConfig.SPEC, "ricksportalgun-common.toml");
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, PGClientConfig.SPEC, "ricksportalgun-client.toml");
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

    private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverPlayer.getServer();
            if (server != null) {
                List<String> dims = LevelHelper.getDimensionsAsString(server.getAllLevels());
                if (!dims.contains(PGHelper.id("blender").toString())) dims.add(PGHelper.id("blender").toString());
                CBSyncDimensionListPacket dimSync = new CBSyncDimensionListPacket(dims);
                CBSyncGunTypesPacket typeSync = new CBSyncGunTypesPacket(PortalGunTypeRegistry.PORTAL_GUN_TYPES);
                PacketDistributor.sendToPlayer(serverPlayer, dimSync, typeSync);
            }
        }
    }

    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> key = event.getTabKey();
        for (Map.Entry<Item, ResourceKey<CreativeModeTab>> entry : PGItems.TABS.entrySet()) {
            ResourceKey<CreativeModeTab> entryKey = entry.getValue();
            if (key.equals(entryKey)) event.accept(entry.getKey());
        }
    }
    @EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PGConstants.MODID)
    public static class SyncEventClass {

        @SubscribeEvent
        public static void dataPackSyncEvent(OnDatapackSyncEvent event) {
            event.sendRecipes(PGRecipeTypes.WORKBENCH_TYPE);
        }
    }
}