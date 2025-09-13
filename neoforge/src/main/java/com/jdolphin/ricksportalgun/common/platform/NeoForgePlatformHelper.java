package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.config.PGClientConfig;
import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.util.platform.services.IPlatformHelper;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

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
        PacketDistributor.sendToServer(packet);
    }

    @Override
    public <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P... packets) {
        for (P packet : packets) {
            PacketDistributor.sendToPlayer(player, packet);
        }
    }

    @Override
    public String getConfigPath() {
        return FMLConfig.defaultConfigPath();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, "");
        return new BlockEntityType<>(func::apply, Set.of(blocks), type);
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

    @Override
    public boolean disablePortalGunColorTint() {
        return PGClientConfig.disablePortalGunColorTint();
    }

    @Override
    public List<? extends String> getDisabledEntities() {
        return PGCommonConfig.getBlacklistedEntities();
    }
}