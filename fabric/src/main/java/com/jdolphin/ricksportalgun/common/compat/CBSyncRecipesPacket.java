package com.jdolphin.ricksportalgun.common.compat;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public record CBSyncRecipesPacket(List<RecipeHolder<?>> holderList) implements CustomPacketPayload {
    public static final Type<CBSyncRecipesPacket> ID = new Type<>(PGHelper.id("sync_recipes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CBSyncRecipesPacket> CODEC = StreamCodec.composite(RecipeHolder.STREAM_CODEC.apply(ByteBufCodecs.list()),
            CBSyncRecipesPacket::holderList, CBSyncRecipesPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
