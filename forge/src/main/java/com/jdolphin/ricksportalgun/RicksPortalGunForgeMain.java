package com.jdolphin.ricksportalgun;

import com.jdolphin.ricksportalgun.common.config.PGClientConfig;
import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.data.PortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.item.ForgePortalGunItem;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncGunTypesPacket;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.awt.*;
import java.util.List;
import java.util.Map;
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
        hackyItemRegisterStuffIdk(bus);
        bind(bus, Registries.BLOCK_ENTITY_TYPE, PGBlockEntities::init);
        bind(bus, Registries.ENTITY_TYPE, PGEntities::init);
        bind(bus, Registries.MENU, PGMenuTypes::init);
        bind(bus, Registries.RECIPE_TYPE, PGRecipeTypes::init);
        bind(bus, Registries.RECIPE_SERIALIZER, PGRecipeSerializers::init);

        context.registerConfig(ModConfig.Type.COMMON, PGCommonConfig.SPEC, "ricksportalgun-common.toml");
        context.registerConfig(ModConfig.Type.CLIENT, PGClientConfig.SPEC, "ricksportalgun-client.toml");
    }

    private void hackyItemRegisterStuffIdk(IEventBus bus) {

        for (Map.Entry<ResourceLocation, Item> entry : PGItems.ALL.entrySet()) {
            Item item = entry.getValue();
            DataComponentMap map = item.components();
            if (item instanceof PortalGunItem) {
                int color = map.getOrDefault(PGDataComponents.DEFAULT_PORTAL_COLOUR, Color.GREEN.getRGB());
                entry.setValue(new ForgePortalGunItem(PGItems.gunProperties(new Color(color))));
            }
            bind(bus, Registries.ITEM, consumer -> consumer.accept(item, entry.getKey()));
        }
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
        event.enqueueWork(ForgePackets::init);
    }

    public void reloadListenerAddEvent(AddReloadListenerEvent event) {
        event.addListener(new PortalGunTypeReloadListener());
    }

    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> key = event.getTabKey();
        for (Map.Entry<Item, ResourceKey<CreativeModeTab>> entry : PGItems.TABS.entrySet()) {
            ResourceKey<CreativeModeTab> entryKey = entry.getValue();
            if (key.equals(entryKey)) event.accept(entry.getKey());
        }
    }
}