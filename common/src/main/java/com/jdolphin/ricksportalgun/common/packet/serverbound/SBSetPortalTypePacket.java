package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.customization.shape.PortalShape;
import com.jdolphin.ricksportalgun.common.customization.type.PortalType;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.init.PGPortalShapes;
import com.jdolphin.ricksportalgun.common.init.PGPortalTypes;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBSetPortalTypePacket(PortalType type, PortalShape shape, int color) implements PGServerPayload {

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt(PGNbtKeys.TAG_COLOR, color);
            if (type != null) {
                tag.putString(PGNbtKeys.PORTAL_TYPE, type.getId().toString());
            }
            if (shape != null) {
                tag.putString(PGNbtKeys.PORTAL_SHAPE, shape.getId().toString());
            }
        });
    }

    public static SBSetPortalTypePacket decode(FriendlyByteBuf buf) {
        return new SBSetPortalTypePacket(PGPortalTypes.get(buf.readResourceLocation()), PGPortalShapes.get(buf.readResourceLocation()), buf.readVarInt());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(type.getId());
        buf.writeResourceLocation(shape.getId());
        buf.writeVarInt(color);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("portal_type");
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }
}