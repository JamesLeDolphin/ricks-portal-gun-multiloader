package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record SBSetDestinationPacket(BlockPos pos, String dim, Optional<Float> rotation) implements PGServerPayload {

    public SBSetDestinationPacket(Waypoint waypoint) {
        this(waypoint.getBlockPos(), waypoint.getDimension(), Optional.of(waypoint.getRotation()));
    }

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            if (!PGConfigHelper.getDisabledDimensions().contains(dim)) {
                boolean hasDim1 = PortalGunItem.getUpgrades(stack).contains(PGUpgradeTypes.DIM_1.getUpgradeTag());
                if (player.level().dimension().location().toString().equals(dim) || hasDim1)
                    if (player.level().getWorldBorder().isWithinBounds(pos)) PortalGunItem.setHopLocation(stack, new ResourceLocation(dim), pos, rotation);
                    else PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.outside_border"));
                else PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.unreachable");
            } else PGHelper.sendFailMsg(player, "error.ricksportalgun.dimension.disabled");
        });
    }

    public static SBSetDestinationPacket decode(FriendlyByteBuf buf) {
        return new SBSetDestinationPacket(buf.readBlockPos(), buf.readUtf(), buf.readOptional(FriendlyByteBuf::readFloat));
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos).writeUtf(dim).writeOptional(rotation, FriendlyByteBuf::writeFloat);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("destination");
    }
}
