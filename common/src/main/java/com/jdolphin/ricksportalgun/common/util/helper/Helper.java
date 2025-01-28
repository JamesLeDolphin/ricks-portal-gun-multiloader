package com.jdolphin.ricksportalgun.common.util.helper;

import com.jdolphin.ricksportalgun.Constants;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.platform.Services;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Helper {
    public static ResourceLocation createLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MODID, string);
    }

    public static <P extends CustomPacketPayload> void sendPacketToServer(P packet) {
        Services.PLATFORM.sendPacketToServer(packet);
    }

    public static <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P packet) {
        Services.PLATFORM.sendPacketToClient(player, packet);
    }

    public static PortalGunItem getPortalGun(@NotNull ItemStack stack) {
        if (stack.is(PGTags.Items.PORTAL_GUNS)) {
            return (PortalGunItem) stack.getItem();
        }
        return null;
    }
}
