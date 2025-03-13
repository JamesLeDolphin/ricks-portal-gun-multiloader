package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.packet.CBOpenGuiPacket;
import com.jdolphin.ricksportalgun.common.platform.services.IPlatformHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
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
    public <P extends CustomPacketPayload> void sendPacketToServer(P packet) {

    }

    @Override
    public <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P packet) {

    }

    @Override
    public void openScreen(ServerPlayer player, int id) {
        sendPacketToClient(player, new CBOpenGuiPacket(id));
    }
}