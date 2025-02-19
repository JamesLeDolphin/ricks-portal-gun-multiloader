package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import io.netty.buffer.ByteBuf;
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

public record SBCoordCheckerPacket(String dim) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBCoordCheckerPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBCoordCheckerPacket::dim, SBCoordCheckerPacket::new);
    public static final Type<SBCoordCheckerPacket> ID = new Type<>(Helper.createLocation("coord_check"));

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.dim);
    }

    public SBCoordCheckerPacket(String dim) {
        this.dim = dim;
    }

    public SBCoordCheckerPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        player.sendSystemMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.start").withStyle(ChatFormatting.YELLOW));
        BlockPos bPos = LevelHelper.getSafePos(LevelHelper.getRandomCoord(player.serverLevel(), 1000), player.serverLevel()); //TODO: Config the radius

        ResourceLocation dim = ResourceLocation.parse(this.dim);
        ServerLevel level;
        level = server.getLevel(ResourceKey.create(Registries.DIMENSION, dim));
        if (level == null) level = player.serverLevel();

        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = Helper.getPortalGun(stack);

        item.setHopLocation(stack, level.dimension().location(), bPos);
        player.sendSystemMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.success").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
