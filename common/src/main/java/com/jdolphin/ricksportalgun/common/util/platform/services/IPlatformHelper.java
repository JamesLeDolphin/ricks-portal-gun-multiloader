package com.jdolphin.ricksportalgun.common.util.platform.services;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.util.network.PGPayload;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.function.BiFunction;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    <P extends PGServerPayload> void sendPacketToServer(P packet);

    <P extends PGPayload> void sendPacketToClient(ServerPlayer player, P... packet);

    String getConfigPath();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks);

    <M extends AbstractContainerMenu> MenuType<M> createMenuType(BiFunction<Integer, Inventory, M> constructor);

    <M extends AbstractContainerMenu> MenuType<M> createExtendedMenuType(TriFunction<Integer, Inventory, FriendlyByteBuf, M> constructor);

    void openDispenserMenu(ServerPlayer player, PortalDispenserBlockEntity be);

    List<? extends String> getDisabledDimensions();

    boolean disableStructureLocating();

    boolean disableBiomeLocating();

    boolean disablePlayerLocating();

    int getRandomizerMax();

    boolean disablePortalGunColorTint();

    List<? extends String> getDisabledEntities();

    EntityType<? extends Entity> getPortalEntityType();

    //TODO Find a better implementation of this
    FlowingFluid getStillFluid(String type);

    FlowingFluid getFlowingFluid(String type);
}
