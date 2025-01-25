package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.packets.*;
import com.jdolphin.ricksportalgun.common.platform.services.IPlatformHelper;
import com.jdolphin.ricksportalgun.common.util.PGPacketType;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void sendPacketToServer(PGPacketType packet) {
        switch (packet.getType()) {
            case COLOUR -> ClientPlayNetworking.send(new SBColourPacket(((int) packet.getArg1())));
            case OPEN_GUI -> ClientPlayNetworking.send(new SBOpenGuiPacket(((String) packet.getArg1())));
            case SETTINGS -> ClientPlayNetworking.send(new SBSettingsPacket((Boolean) packet.getArg1(), (String) packet.getArg2()));
            case COORD_CHECK -> ClientPlayNetworking.send(new SBCoordCheckerPacket((String) packet.getArg1()));
            case LOCATE_PLAYER -> ClientPlayNetworking.send(new SBLocatePlayerPacket((String) packet.getArg1()));
            case CHANGE_GUN_TYPE -> ClientPlayNetworking.send(new SBChangePortalGunTypePacket((PortalGunType) packet.getArg1()));
            case DESTINATION_SET -> ClientPlayNetworking.send(new SBSetDestinationPacket((BlockPos) packet.getArg1(), (String) packet.getArg2()));
            case MANAGE_WAYPOINTS -> ClientPlayNetworking.send(new SBManageWaypointsPacket((String) packet.getArg1(), (Boolean) packet.getArg2()));
        }
    }

}
