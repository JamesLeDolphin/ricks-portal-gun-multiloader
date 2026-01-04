package com.jdolphin.ricksportalgun.common.platform;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.comp.immersive_portals.PortalHolder;
import com.jdolphin.ricksportalgun.common.config.PGClientConfig;
import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGPayload;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import com.jdolphin.ricksportalgun.common.util.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

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
    public <P extends PGServerPayload> void sendPacketToServer(P packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ClientPlayNetworking.send(packet.getId(), buf);
    }

    @Override
    public <P extends PGPayload> void sendPacketToClient(ServerPlayer player, P... packets) {
        for (P packet : packets) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            packet.encode(buf);
            ServerPlayNetworking.send(player, packet.getId(), buf);
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
    public <M extends AbstractContainerMenu> MenuType<M> createExtendedMenuType(TriFunction<Integer, Inventory, FriendlyByteBuf, M> constructor) {
        return new ExtendedScreenHandlerType<>(constructor::apply);
    }

    @Override
    public void openDispenserMenu(ServerPlayer player, PortalDispenserBlockEntity be) {
        var factory = new ExtendedScreenHandlerFactory() {

            @Override
            public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return be.createMenu(i, inventory, player);
            }

            @Override
            public Component getDisplayName() {
                return be.getDisplayName();
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer serverPlayer, FriendlyByteBuf buf) {
                buf.writeBlockPos(be.getDestinationPos());
                buf.writeUtf(be.getDestinationDim());
            }
        };
        player.openMenu(factory);
    }


    @Override
    public List<? extends String> getDisabledDimensions() {
        return PGCommonConfig.COMMON_CONFIG.getBlacklistedDims();
    }

    @Override
    public boolean disableStructureLocating() {
        return PGCommonConfig.COMMON_CONFIG.disableStructureLocating();
    }

    @Override
    public boolean disableBiomeLocating() {
        return PGCommonConfig.COMMON_CONFIG.disableBiomeLocating();
    }

    @Override
    public boolean disablePlayerLocating() {
        return PGCommonConfig.COMMON_CONFIG.disablePlayerLocating();
    }

    @Override
    public int getRandomizerMax() {
        return PGCommonConfig.COMMON_CONFIG.getMaxRandomizerDistance();
    }

    @Override
    public boolean disablePortalGunColorTint() {
        return PGClientConfig.CLIENT_CONFIG.disablePortalGunColorTint();
    }

    @Override
    public List<? extends String> getDisabledEntities() {
        return PGCommonConfig.COMMON_CONFIG.getBlacklistedEntities();
    }

    @Override
    public EntityType<? extends Entity> getPortalEntityType() {
        return PGHelper.hasImmersivePortals() ? PortalHolder.TYPE : null;
    }
}
