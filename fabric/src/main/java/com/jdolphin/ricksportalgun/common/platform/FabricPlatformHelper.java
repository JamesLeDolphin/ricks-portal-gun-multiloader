package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.util.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.BiFunction;

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
    public <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P... packets) {
        for (P packet : packets) {
            ServerPlayNetworking.send(player, packet);
        }
    }

    @Override
    public String getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().toString();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(func::apply, blocks).build();
    }

    @Override
    public <M extends AbstractContainerMenu> MenuType<M> createMenuType(BiFunction<Integer, Inventory, M> constructor) {
        return new MenuType<>(constructor::apply, FeatureFlags.DEFAULT_FLAGS);
    }

    @Override
    public List<? extends String> getDisabledDimensions() {
        return PGCommonConfig.getBlacklistedDims();
    }

    @Override
    public boolean disableStructureLocating() {
        return PGCommonConfig.disableStructureLocating();
    }

    @Override
    public boolean disableBiomeLocating() {
        return PGCommonConfig.disableBiomeLocating();
    }

    @Override
    public boolean disablePlayerLocating() {
        return PGCommonConfig.disablePlayerLocating();
    }

    @Override
    public int getRandomizerMax() {
        return PGCommonConfig.getMaxRandomizerDistance();
    }
}
