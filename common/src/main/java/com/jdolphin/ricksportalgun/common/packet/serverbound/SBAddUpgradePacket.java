package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.UpgradeItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SBAddUpgradePacket(String upgradeType) implements PGServerPayload {

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            List<ItemStack> stacks = player.getInventory().items;
            for (ItemStack itemStack : stacks) {
                if (itemStack.getItem() instanceof UpgradeItem upgradeItem) {
                    if (upgradeItem.getUpgradeType().getUpgradeTag().equals(upgradeType)) {
                        itemStack.shrink(1);
                        break;
                    }
                }
            }
            PortalGunItem.addUpgrade(stack, PGUpgradeTypes.getFromString(upgradeType));
            PGHelper.sendSuccessMsg(player, Component.translatable("notice.ricksportalgun.upgrade"));
        });
    }

    public static SBAddUpgradePacket decode(FriendlyByteBuf buf) {
        return new SBAddUpgradePacket(buf.readUtf());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(upgradeType);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("upgrade");
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }
}
