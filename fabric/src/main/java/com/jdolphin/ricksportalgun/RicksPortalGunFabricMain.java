package com.jdolphin.ricksportalgun;

import com.jdolphin.ricksportalgun.common.compat.CBSyncRecipesPacket;
import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.data.FabricPortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncGunTypesPacket;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class RicksPortalGunFabricMain implements ModInitializer {
    public static List<RecipeHolder<?>> recipes = new ArrayList<>();

    @Override
    public void onInitialize() {
        RicksPortalGunCommonMain.init();

        PGBlocks.init(bind(BuiltInRegistries.BLOCK));
        PGBlockEntities.init(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        PGItems.init(bind(BuiltInRegistries.ITEM));
        PGEntities.init(bind(BuiltInRegistries.ENTITY_TYPE));
        PGDataComponents.init(bind(BuiltInRegistries.DATA_COMPONENT_TYPE));
        PGMenuTypes.init(bind(BuiltInRegistries.MENU));
        PGRecipeSerializers.init(bind(BuiltInRegistries.RECIPE_SERIALIZER));
        PGRecipeTypes.init(bind(BuiltInRegistries.RECIPE_TYPE));
        FabricPackets.registerC2SPackets();

        ForgeConfigRegistry.INSTANCE.register(PGConstants.MODID, ModConfig.Type.COMMON, PGCommonConfig.SPEC, "ricksportalgun-common.toml");
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricPortalGunTypeReloadListener());

        initEvents();
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }


    private void initEvents() {

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            List<String> strings = LevelHelper.getDimensionsAsString(server.getAllLevels());
            if (!strings.contains(PGHelper.id("blender").toString())) strings.add(PGHelper.id("blender").toString());
            LevelHelper.addDimensions(strings);
        });

        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            List<String> dims = new ArrayList<>(LevelHelper.getDimensionsAsString(server.getAllLevels()).stream().filter(s -> !PGConfigHelper.getDisabledDimensions().contains(s)).toList());
            if (!dims.contains(PGHelper.id("blender").toString()) && !PGConfigHelper.getDisabledDimensions().contains(PGHelper.id("blender").toString()))
                dims.add(PGHelper.id("blender").toString());
            CBSyncDimensionListPacket dimPacket = new CBSyncDimensionListPacket(dims);
            CBSyncGunTypesPacket typesPacket = new CBSyncGunTypesPacket(PortalGunTypeRegistry.PORTAL_GUN_TYPES);
            PGHelper.sendPacketToClient(listener.player, dimPacket, typesPacket);
        });

        for (Map.Entry<Item, ResourceKey<CreativeModeTab>> entry : PGItems.TABS.entrySet()) {
            ResourceKey<CreativeModeTab> tabKey = entry.getValue();
            if (tabKey != null) {
                ItemGroupEvents.modifyEntriesEvent(entry.getValue()).register((entries) -> {
                    Item item = entry.getKey();
                    entries.accept(item);
                });
            }
        }

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, b) -> {
            if (PGServices.PLATFORM.isModLoaded("roughlyenoughitems")) {
                RecipeManager manager = player.serverLevel().recipeAccess();
                List<RecipeHolder<?>> list = manager.getRecipes().stream().filter(recipeHolder -> (recipeHolder.value() instanceof PortalGunWorkbenchRecipe)).toList();
                CBSyncRecipesPacket packet = new CBSyncRecipesPacket(list);
                PGHelper.sendPacketToClient(player, packet);
            }
        });
        ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> {
            if (PGServices.PLATFORM.isModLoaded("roughlyenoughitems")) {
                CBSyncRecipesPacket packet = new CBSyncRecipesPacket(List.of());
                PGHelper.sendPacketToClient(listener.player, packet);
            }
        });
    }
}
