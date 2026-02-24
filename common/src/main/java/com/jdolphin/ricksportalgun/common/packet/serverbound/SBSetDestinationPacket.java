package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBSetDestinationPacket(BlockPos pos, String dim, boolean manualTarget) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        String dimension = dim().toLowerCase().replaceAll(" ", "_");
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            CompoundTag tag = stack.getOrCreateTag();
            tag.putBoolean(PGNbtKeys.PROJECTILE_MODE, manualTarget);
            if (!PGConfigHelper.getDisabledDimensions().contains(dimension)) {
                PortalGunItem.setHopLocation(stack, dimension, pos);
            } else PGHelper.sendFailMsg(player, "error.ricksportalgun.dimension.disabled");
        });
    }

    public static SBSetDestinationPacket decode(FriendlyByteBuf buf) {
        return new SBSetDestinationPacket(buf.readBlockPos(), buf.readUtf(), buf.readBoolean());
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(dim);
        buf.writeBoolean(manualTarget);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("destination");
    }
}
