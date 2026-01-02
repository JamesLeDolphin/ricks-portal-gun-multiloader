package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.customization.shape.PortalShape;
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

public record SBSetPortalTypePacket(PortalType type, PortalShape shape) implements PGServerPayload {

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            if (type != null) {
                CompoundTag tag = stack.getOrCreateTag();
                tag.putString(PGNbtKeys.PORTAL_TYPE, type.getId().toString());
            }
            if (shape != null) {
                CompoundTag tag = stack.getOrCreateTag();
                tag.putString(PGNbtKeys.PORTAL_SHAPE, shape.getId().toString());
            }
        });
    }

    public static SBSetPortalTypePacket decode(FriendlyByteBuf buf) {

        return new SBSetPortalTypePacket(PGPortalTypes.TYPES.get(buf.readResourceLocation()), PGPortalShapes.SHAPES.get(buf.readResourceLocation()));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(type.getId());
        buf.writeResourceLocation(shape.getId());
    }

    public static ResourceLocation getID() {
        return PGHelper.id("portal_type");
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }
}
