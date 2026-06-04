package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBSetDestinationPacket(BlockPos pos, String dim) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBSetDestinationPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, SBSetDestinationPacket::pos,
            ByteBufCodecs.STRING_UTF8, SBSetDestinationPacket::dim, SBSetDestinationPacket::new);
    public static final Type<SBSetDestinationPacket> ID = new Type<>(PGHelper.id("destination"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            if (!PGConfigHelper.getDisabledDimensions().contains(dim)) {
                boolean hasDim1 = PortalGunItem.getUpgrades(stack).contains(PGUpgradeTypes.DIM_1.getId());
                if (player.level().dimension().location().toString().equals(dim) || hasDim1)
                    PortalGunItem.setHopLocation(stack, ResourceLocation.parse(dim), pos);
                else PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.unreachable");
            } else PGHelper.sendFailMsg(player, "error.ricksportalgun.dimension.disabled");
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
