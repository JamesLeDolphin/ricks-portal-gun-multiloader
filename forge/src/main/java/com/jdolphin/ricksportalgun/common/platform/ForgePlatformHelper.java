package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.init.ForgePackets;
import com.jdolphin.ricksportalgun.common.platform.services.IPlatformHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

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
    public <P extends CustomPacketPayload> void sendPacketToServer(P packet) {
        ForgePackets.sendToServer(packet);
    }

    @Override
    public <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P packet) {
        ForgePackets.sendToPlayer(packet, player);
    }
}