package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.init.ForgePackets;
import com.jdolphin.ricksportalgun.common.packets.*;
import com.jdolphin.ricksportalgun.common.platform.services.IPlatformHelper;
import com.jdolphin.ricksportalgun.common.util.PGPacketType;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.List;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public void sendPacketToServer(PGPacketType packet) {
        switch (packet.getType()) {
            case COLOUR -> ForgePackets.sendToServer(new SBColourPacket(((int) packet.getArg1())));
            case OPEN_GUI -> ForgePackets.sendToServer(new SBOpenGuiPacket());
            case SETTINGS -> ForgePackets.sendToServer(new SBSettingsPacket((Boolean) packet.getArg1(), (String) packet.getArg2()));
            case COORD_CHECK ->ForgePackets.sendToServer(new SBCoordCheckerPacket((String) packet.getArg1()));
            case LOCATE_PLAYER -> ForgePackets.sendToServer(new SBLocatePlayerPacket((String) packet.getArg1()));
            //case CHANGE_GUN_TYPE -> ForgePackets.sendToServer(new SBChangePortalGunTypePacket((PortalGunType) packet.getArg1()));
            case DESTINATION_SET -> ForgePackets.sendToServer(new SBSetDestinationPacket((BlockPos) packet.getArg1(), (ResourceLocation) packet.getArg2()));
            case MANAGE_WAYPOINTS -> ForgePackets.sendToServer(new SBManageWaypointsPacket((String) packet.getArg1(), (Boolean) packet.getArg2()));
        }
    }
}