package com.jdolphin.ricksportalgun;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.comp.immersive_portals.PortalHolder;
import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class RicksPortalGunFabricMain implements ModInitializer {

    @Override
    public void onInitialize() {
        RicksPortalGunCommonMain.init();

        PGBlocks.init(bind(BuiltInRegistries.BLOCK));
        PGBlockEntities.init(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        PGItems.init(bind(BuiltInRegistries.ITEM));
        PGEntities.init(bind(BuiltInRegistries.ENTITY_TYPE));
        PGMenuTypes.init(bind(BuiltInRegistries.MENU));
        PGRecipeSerializers.init(bind(BuiltInRegistries.RECIPE_SERIALIZER));
        PGRecipeTypes.init(bind(BuiltInRegistries.RECIPE_TYPE));
        FabricPackets.registerC2SPackets();
        PGMeeseeksCommands.init();
        if (PGHelper.hasImmersivePortals()) {
            Registry.register(BuiltInRegistries.ENTITY_TYPE, PGHelper.id("seethrough_portal"), PortalHolder.TYPE);
        }
        ForgeConfigRegistry.INSTANCE.register(PGConstants.MODID, ModConfig.Type.COMMON, PGCommonConfig.SPEC, "ricksportalgun-common.toml");
        initEvents();

        if (PGHelper.hasCCTweaked()) {
            dan200.computercraft.api.peripheral.PeripheralLookup.get().registerFallback((level, blockPos, blockState, blockEntity, direction) -> {
                if (blockEntity instanceof PortalDispenserBlockEntity be) return (dan200.computercraft.api.peripheral.IPeripheral) be.getPeripheral();
                if (blockEntity instanceof SubetherBarrierBlockEntity be) return (dan200.computercraft.api.peripheral.IPeripheral) be.getPeripheral();
                return null;
            });
        }
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }


    private void initEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            PGDamageTypes.init(server.registryAccess());
            List<String> strings = LevelHelper.getDimensionsAsString(server.getAllLevels());
            if (!strings.contains(PGHelper.id("blender").toString())) strings.add(PGHelper.id("blender").toString());
            LevelHelper.addDimensions(strings);
        });

        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            CBSyncDimensionListPacket dimPacket = new CBSyncDimensionListPacket(LevelHelper.getDimensionsAsString(server.getAllLevels()));
            PGHelper.sendPacketToClient(listener.player, dimPacket);
        });

        for (Map.Entry<Item, ResourceKey<CreativeModeTab>> entry : PGItems.TABS.entrySet()) {
            ResourceKey<CreativeModeTab> tabKey = entry.getValue();
            if (tabKey != null) {
                ItemGroupEvents.modifyEntriesEvent(entry.getValue()).register((entries) -> {
                    Item item = entry.getKey();
                    entries.accept(item);
                });
            } else if (FabricLoader.getInstance().isDevelopmentEnvironment() && entry.getKey() instanceof PortalGunItem item) {
                ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.accept(item));
            }
        }
    }
}
