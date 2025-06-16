package com.jdolphin.ricksportalgun;


import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.data.PortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;
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

        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, PGCommonConfig.SPEC, "portalgun-common.toml");
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
        for (Map.Entry<Item, ResourceKey<CreativeModeTab>> entry : PGItems.TABS.entrySet()) {
            ResourceKey<CreativeModeTab> entryKey = entry.getValue();
            if (key.equals(entryKey)) event.accept(entry.getKey());
        }
    }
}