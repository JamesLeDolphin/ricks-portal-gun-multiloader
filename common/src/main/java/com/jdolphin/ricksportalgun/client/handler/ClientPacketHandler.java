package com.jdolphin.ricksportalgun.client.handler;

import com.jdolphin.ricksportalgun.client.screen.MeeseeksCommandScreen;
import com.jdolphin.ricksportalgun.client.screen.SubetherBarrierScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.CoordTravelScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.LocatorScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.settings.SecuritySettingsScreen;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.UUID;

public class ClientPacketHandler {

    public static void openBarrierGui(BlockPos pos) {
        Minecraft.getInstance().setScreen(new SubetherBarrierScreen(pos));
    }

    public static void openCoordTravelScreen(List<String> dimensions) {
        Minecraft.getInstance().setScreen(new CoordTravelScreen(dimensions));
    }

    public static void syncClientDimensions(List<String> dimensions) {
        LevelHelper.CLIENT_DIMENSIONS = dimensions;
    }

    public static void openLocatorScreen(List<String> playerList, List<String> biomeList, List<String> structureList) {
        Minecraft.getInstance().setScreen(new LocatorScreen(playerList, biomeList, structureList));
    }

    public static void openSecurityScreen(List<String> playerList) {
        Minecraft.getInstance().setScreen(new SecuritySettingsScreen(playerList));
    }

    public static void openMeeseeksScreen(UUID uuid) {
        Minecraft.getInstance().setScreen(new MeeseeksCommandScreen(uuid));
    }
}
