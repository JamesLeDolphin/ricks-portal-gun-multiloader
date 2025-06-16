package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBCoordCheckerPacket(String dim) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBCoordCheckerPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBCoordCheckerPacket::dim, SBCoordCheckerPacket::new);
    public static final Type<SBCoordCheckerPacket> ID = new Type<>(PGHelper.createLocation("coord_check"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        player.displayClientMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.start").withStyle(ChatFormatting.YELLOW), false);
        BlockPos bPos = LevelHelper.getSafePos(LevelHelper.getRandomCoord(player.serverLevel(), PGHelper.getRandomizerMax()), player.serverLevel());

        ResourceLocation dim = ResourceLocation.parse(this.dim);
        ServerLevel level;
        level = server.getLevel(ResourceKey.create(Registries.DIMENSION, dim));
        if (level == null) level = player.serverLevel();

        ItemStack stack = player.getMainHandItem();

        PortalGunItem.setHopLocation(stack, level.dimension().location(), bPos);
        player.sendSystemMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.success").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
