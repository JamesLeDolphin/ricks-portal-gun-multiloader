package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBSetDestinationPacket(BlockPos pos, String dim) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBSetDestinationPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, SBSetDestinationPacket::pos,
            ByteBufCodecs.STRING_UTF8, SBSetDestinationPacket::dim, SBSetDestinationPacket::new);
    public static final Type<SBSetDestinationPacket> ID = new Type<>(Helper.createLocation("destination"));

    public SBSetDestinationPacket(BlockPos pos, String dim) {
        this.pos = pos;
        this.dim = dim;
    }

    public SBSetDestinationPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.dim);
        buf.writeBlockPos(this.pos);
    }

    public void handle(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = Helper.getPortalGun(stack);
        if (/*!Config.getInstance().getBlacklistedDimensions().contains(dim) TODO: Figure out configs*/ true) {
            item.setHopLocation(stack, ResourceLocation.parse(dim), pos);
        } else player.sendSystemMessage(Component.translatable("notice.ricksportalgun.dimension_disabled").withStyle(ChatFormatting.RED), false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
