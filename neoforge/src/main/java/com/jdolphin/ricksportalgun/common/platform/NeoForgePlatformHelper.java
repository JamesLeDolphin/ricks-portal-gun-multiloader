package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.packet.CBOpenBarrierGuiPacket;
import com.jdolphin.ricksportalgun.common.util.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
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
    public String getConfigPath() {
        return "";
    }

    @Override
    public void openBarrierScreen(ServerPlayer player, BlockPos pos) {
        sendPacketToClient(player, new CBOpenBarrierGuiPacket(pos));
    }
}