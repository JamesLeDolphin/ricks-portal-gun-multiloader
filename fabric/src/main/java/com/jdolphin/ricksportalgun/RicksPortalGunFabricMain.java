package com.jdolphin.ricksportalgun;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.entity.MeeseeksEntity;
import com.jdolphin.ricksportalgun.common.event.PGCommonEventHandler;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;
import java.util.function.BiConsumer;

public class RicksPortalGunFabricMain implements ModInitializer {

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(PGConstants.MODID, ModConfig.Type.COMMON, PGCommonConfig.SPEC, "ricksportalgun-common.toml");

        RicksPortalGunCommonMain.init();
        PGBlocks.init(bind(BuiltInRegistries.BLOCK));
        PGBlockEntities.init(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        PGItems.init(bind(BuiltInRegistries.ITEM));
        PGEntities.init(bind(BuiltInRegistries.ENTITY_TYPE));
        PGMenuTypes.init(bind(BuiltInRegistries.MENU));
        PGRecipeSerializers.init(bind(BuiltInRegistries.RECIPE_SERIALIZER));
        PGRecipeTypes.init(bind(BuiltInRegistries.RECIPE_TYPE));
        PGFluids.init(bind(BuiltInRegistries.FLUID));
        FabricPackets.registerC2SPackets();
        PGMeeseeksCommands.init();

        FabricDefaultAttributeRegistry.register(PGEntities.MEESEEKS, MeeseeksEntity.createMobAttributes());
        initEvents();
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> (net.fabricmc.fabric.api.transfer.v1.storage.Storage<net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant>) blockEntity.getFluidStorage(direction), PGBlockEntities.PORTAL_FLUID_TANK);
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
        ServerLifecycleEvents.SERVER_STARTED.register(PGCommonEventHandler::serverStartEvent);
        ServerTickEvents.END_SERVER_TICK.register(PGCommonEventHandler::serverTickEvent);
        ServerLifecycleEvents.SERVER_STOPPING.register(PGCommonEventHandler::mapDimensions);

        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) ->
                PGCommonEventHandler.playerJoinEvent(listener.player));

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
