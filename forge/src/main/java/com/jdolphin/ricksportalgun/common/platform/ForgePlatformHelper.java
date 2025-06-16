package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.init.ForgePackets;
import com.jdolphin.ricksportalgun.common.util.platform.services.IPlatformHelper;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

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
    public <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P... packets) {
        ForgePackets.sendToPlayer(player, packets);
    }

    @Override
    public String getConfigPath() {
        return FMLConfig.defaultConfigPath();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        return new BlockEntityType<>(func::apply, Set.of(blocks));
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