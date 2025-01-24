package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.config.Config;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBSetDestinationPacket(BlockPos pos, ResourceLocation dim) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBSetDestinationPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, SBSetDestinationPacket::pos,
            ResourceLocation.STREAM_CODEC, SBSetDestinationPacket::dim, SBSetDestinationPacket::new);
    public static final CustomPacketPayload.Type<SBSetDestinationPacket> ID = new CustomPacketPayload.Type<>(Helper.createLocation("destination"));

    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = Helper.getPortalGun(stack);
        if (!Config.getInstance().getBlacklistedDimensions().contains(dim)) {
            item.setHopLocation(stack, dim, pos);
        } else player.sendSystemMessage(Component.translatable("notice.ricksportalgun.dimension_disabled").withStyle(ChatFormatting.RED), false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
