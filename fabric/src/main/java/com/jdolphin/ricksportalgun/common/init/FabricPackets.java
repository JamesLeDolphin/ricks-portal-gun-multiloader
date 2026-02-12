package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.packet.serverbound.*;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class FabricPackets {

    public static void registerC2SPackets() {
        registerGlobalReceiver(SBColourPacket.getID(), SBColourPacket::decode);
        registerGlobalReceiver(SBCoordCheckerPacket.getID(), SBCoordCheckerPacket::decode);
        registerGlobalReceiver(SBSecuritySettingsPacket.getID(), SBSecuritySettingsPacket::decode);
        registerGlobalReceiver(SBLocatePacket.getID(), SBLocatePacket::decode);
        registerGlobalReceiver(SBManageWaypointsPacket.getID(), SBManageWaypointsPacket::decode);
        registerGlobalReceiver(SBSetDestinationPacket.getID(), SBSetDestinationPacket::decode);
        registerGlobalReceiver(SBOpenCoordGuiPacket.getID(), SBOpenCoordGuiPacket::decode);
        registerGlobalReceiver(SBSetBarrierCodePacket.getID(), SBSetBarrierCodePacket::decode);
        registerGlobalReceiver(SBSetDispenserDestinationPacket.getID(), SBSetDispenserDestinationPacket::decode);
        registerGlobalReceiver(SBSetWorkbenchTypePacket.getID(), SBSetWorkbenchTypePacket::decode);
        registerGlobalReceiver(SBOpenLocatorScreenPacket.getID(), SBOpenLocatorScreenPacket::decode);
        registerGlobalReceiver(SBOpenSecuritySettingsPacket.getID(), SBOpenSecuritySettingsPacket::decode);
        registerGlobalReceiver(SBSetPortalGunStylePacket.getID(), SBSetPortalGunStylePacket::decode);
        registerGlobalReceiver(SBCustomizeSettingsPacket.getID(), SBCustomizeSettingsPacket::decode);
        registerGlobalReceiver(SBWorkbenchWaypointEditPackage.getID(), SBWorkbenchWaypointEditPackage::decode);
        registerGlobalReceiver(SBActivateSelfDestructPacket.getID(), SBActivateSelfDestructPacket::decode);
        registerGlobalReceiver(SBSetPortalGunTypePacket.getID(), SBSetPortalGunTypePacket::decode);
        registerGlobalReceiver(SBAddUpgradePacket.getID(), SBAddUpgradePacket::decode);
        registerGlobalReceiver(SBSetPortalTypePacket.getID(), SBSetPortalTypePacket::decode);
        registerGlobalReceiver(SBRunMeeseeksCommandPacket.getID(), SBRunMeeseeksCommandPacket::decode);
        registerGlobalReceiver(SBPortalDialActionPacket.getID(), SBPortalDialActionPacket::decode);
    }

    private static  <P extends PGServerPayload> void registerGlobalReceiver(ResourceLocation rl, Function<FriendlyByteBuf, P> func) {
        ServerPlayNetworking.registerGlobalReceiver(rl, (server, player,
                                                         packetListener, buf,
                                                         packetSender) -> func.apply(buf).handle(player));
    }
}