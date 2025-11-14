package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.init.PGPortalTypes;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBSetPortalTypePacket(PortalType type) implements PGServerPayload {

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            PortalGunItem.setPortalType(stack, this.type);
        });
    }

    public static SBSetPortalTypePacket decode(FriendlyByteBuf buf) {
        return new SBSetPortalTypePacket(PGPortalTypes.TYPES.get(buf.readResourceLocation()));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(type.getId());
    }

    public static ResourceLocation getID() {
        return PGHelper.id("portal_type");
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }
}
