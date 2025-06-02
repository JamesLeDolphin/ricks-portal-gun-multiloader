package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
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

public record SBSetDestinationPacket(BlockPos pos, String dim) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBSetDestinationPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, SBSetDestinationPacket::pos,
            ByteBufCodecs.STRING_UTF8, SBSetDestinationPacket::dim, SBSetDestinationPacket::new);
    public static final Type<SBSetDestinationPacket> ID = new Type<>(PGHelper.createLocation("destination"));

    public void handle(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = PGHelper.getPortalGun(stack);
        if (!PGCommonConfig.INSTANCE.getDisabledDimensions().contains(dim)) {
            item.setHopLocation(stack, ResourceLocation.parse(dim), pos);
        } else player.sendSystemMessage(Component.translatable("error.ricksportalgun.dimension_disabled").withStyle(ChatFormatting.RED), false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
