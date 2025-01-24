package com.jdolphin.ricksportalgun.common;

import com.jdolphin.ricksportalgun.Constants;
import com.jdolphin.ricksportalgun.RicksPortalGunCommonMain;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.common.data.PortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.platform.Services;
import com.jdolphin.ricksportalgun.common.util.PGPacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
}