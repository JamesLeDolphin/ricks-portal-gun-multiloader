package com.jdolphin.ricksportalgun.common.util.helper;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.platform.Services;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

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

    public static <T> T getRandomFromList(List<T> list) {
        int i = list.size();
        int index = PGConstants.RANDOM.nextInt(i);
        return list.get(index);
    }

    @Nullable
    public static PortalGunItem getPortalGun(@NotNull ItemStack stack) {
        if (stack.is(PGTags.Items.PORTAL_GUNS)) {
            return (PortalGunItem) stack.getItem();
        }
        return null;
    }
}
