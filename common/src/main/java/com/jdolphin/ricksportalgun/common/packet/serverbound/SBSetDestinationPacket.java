package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBSetDestinationPacket(BlockPos pos, String dim) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
        if (!PGConfigHelper.getDisabledDimensions().contains(dim)) {
            PortalGunItem.setHopLocation(stack, new ResourceLocation(dim), pos);
        } else PGHelper.sendFailMsg(player, "error.ricksportalgun.dimension.disabled");
    }

    public static SBSetDestinationPacket decode(FriendlyByteBuf buf) {
        return new SBSetDestinationPacket(buf.readBlockPos(), buf.readUtf());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos).writeUtf(dim);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("destination");
    }
}
