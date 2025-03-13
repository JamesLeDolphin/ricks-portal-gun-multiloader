package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.packets.CBOpenGuiPacketFabric;
import com.jdolphin.ricksportalgun.common.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

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
    public <P extends CustomPacketPayload> void sendPacketToServer(P packet) {
        ClientPlayNetworking.send(packet);
    }

    @Override
    public <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P packet) {
        ServerPlayNetworking.send(player, packet);
    }

    @Override
    public void openScreen(ServerPlayer player, int id) {
        sendPacketToClient(player, new CBOpenGuiPacketFabric(id));
    }

}
