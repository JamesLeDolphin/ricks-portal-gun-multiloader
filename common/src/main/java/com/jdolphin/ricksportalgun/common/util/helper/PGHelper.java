package com.jdolphin.ricksportalgun.common.util.helper;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.platform.Services;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PGHelper {

    public static ResourceLocation createLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(PGConstants.MODID, string);
    }

    public static <P extends CustomPacketPayload> void sendPacketToServer(P packet) {
        Services.PLATFORM.sendPacketToServer(packet);
    }

    public static <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P packet) {
        Services.PLATFORM.sendPacketToClient(player, packet);
    }

    @Nullable
    public static PortalGunItem getPortalGun(@NotNull ItemStack stack) {
        if (stack.is(PGTags.Items.PORTAL_GUNS)) {
            return (PortalGunItem) stack.getItem();
        }
        return null;
    }
}
