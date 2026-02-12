package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.event.PGClientEventHandler;
import com.jdolphin.ricksportalgun.client.handler.ClientPacketHandler;
import com.jdolphin.ricksportalgun.client.init.*;
import com.jdolphin.ricksportalgun.client.render.PlayerForcefieldLayer;
import com.jdolphin.ricksportalgun.client.render.PortalFluidTankBlockEntityRenderer;
import com.jdolphin.ricksportalgun.common.config.PGClientConfig;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.init.PGFluids;
import com.jdolphin.ricksportalgun.common.init.PGKeyBinds;
import com.jdolphin.ricksportalgun.common.packet.clientbound.*;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGPayload;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.config.ModConfig;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class RicksPortalGunFabricClient implements ClientModInitializer {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void onInitializeClient() {
        ForgeConfigRegistry.INSTANCE.register(PGConstants.MODID, ModConfig.Type.CLIENT, PGClientConfig.SPEC, "ricksportalgun-client.toml");

        PGEntityRenderRegistry.initRenderers();
        PGEntityRenderRegistry.RENDERERS.forEach(EntityRendererRegistry::register);
        BlockEntityRenderers.register(PGBlockEntities.PORTAL_FLUID_TANK, PortalFluidTankBlockEntityRenderer::new);

        PGEntityRenderRegistry.initLayers();
        PGEntityRenderRegistry.BODY_LAYERS.forEach((location, supplier) ->
                EntityModelLayerRegistry.registerModelLayer(location, supplier::get));

        FluidRenderHandlerRegistry.INSTANCE.register(PGFluids.PORTAL_FLUID.getA(), PGFluids.PORTAL_FLUID.getB(),
                new SimpleFluidRenderHandler(PGHelper.id("block/portal_fluid_still"), PGHelper.id("block/portal_fluid_flow"), Color.GREEN.getRGB()));

        PGPortalTypeRenderers.init();
        PGPortalShapeRenderers.init();

        PGMenuScreens.ALL.forEach((type, func) -> {
            MenuScreens.ScreenConstructor constructor = func::apply;
            MenuScreens.register(type, constructor);
        });

        ColorProviderRegistry.ITEM.register(PGTintHandler::tint, PGTintHandler.TINTABLES);

        BlockRenderLayerMap.INSTANCE.putBlock(PGBlocks.GUN_WORKBENCH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PGBlocks.PORTAL_FLUID_TANK, RenderType.cutout());

        KeyBindingHelper.registerKeyBinding(PGKeyBinds.KEY_PORTAL_MENU);

        initClientPackets();
        ClientTickEvents.END_CLIENT_TICK.register(PGClientEventHandler::onClientTick);
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityType.equals(EntityType.PLAYER)) {
                registrationHelper.register(new PlayerForcefieldLayer(((PlayerRenderer) entityRenderer), context.getModelSet()));
            }
        });

    }

    private void initClientPackets() {
        registerGlobalReceiver(CBOpenCoordGuiPacket.getID(), CBOpenCoordGuiPacket::decode, pgPayload -> ClientPacketHandler.openCoordTravelScreen(pgPayload.strings()));

        registerGlobalReceiver(CBSyncDimensionListPacket.getID(), CBSyncDimensionListPacket::decode, packet -> ClientPacketHandler.syncClientDimensions(packet.dimensions()));
        registerGlobalReceiver(CBOpenBarrierGuiPacket.getID(), CBOpenBarrierGuiPacket::decode,packet -> ClientPacketHandler.openBarrierGui(packet.pos()));
        registerGlobalReceiver(CBOpenLocatorScreenPacket.getID(), CBOpenLocatorScreenPacket::decode, packet ->
                ClientPacketHandler.openLocatorScreen(packet.playerList(), packet.biomeList(), packet.structureList()));

        registerGlobalReceiver(CBOpenSecurityGuiPacket.getID(), CBOpenSecurityGuiPacket::decode, packet -> ClientPacketHandler.openSecurityScreen(packet.strings()));
        registerGlobalReceiver(CBOpenMeeseeksGuiPacket.getID(), CBOpenMeeseeksGuiPacket::decode, packet -> ClientPacketHandler.openMeeseeksScreen(packet.mobId()));
        registerGlobalReceiver(CBOpenDialerGuiPacket.getID(), CBOpenDialerGuiPacket::decode,packet -> ClientPacketHandler.openPortalDialerScreen(packet.pos()));
    }

    private static  <P extends PGPayload> void registerGlobalReceiver(ResourceLocation rl, Function<FriendlyByteBuf, P> func, Consumer<P> consumer) {
        ClientPlayNetworking.registerGlobalReceiver(rl,
                (client, handler, buf, responseSender) -> {
                    P p = func.apply(buf);
            client.execute(() -> consumer.accept(p));
        });
    }
}
