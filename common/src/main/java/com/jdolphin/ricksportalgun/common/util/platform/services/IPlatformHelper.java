package com.jdolphin.ricksportalgun.common.util.platform.services;

import com.jdolphin.ricksportalgun.common.util.PGPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.BiFunction;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    <P extends PGPayload> void sendPacketToServer(P packet);

    <P extends PGPayload> void sendPacketToClient(ServerPlayer player, P... packet);

    String getConfigPath();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks);

    <M extends AbstractContainerMenu> MenuType<M> createMenuType(BiFunction<Integer, Inventory, M> constructor);

    List<? extends String> getDisabledDimensions();

    boolean disableStructureLocating();

    boolean disableBiomeLocating();

    boolean disablePlayerLocating();

    int getRandomizerMax();

    boolean disablePortalGunColorTint();

    List<? extends String> getDisabledEntities();
}
