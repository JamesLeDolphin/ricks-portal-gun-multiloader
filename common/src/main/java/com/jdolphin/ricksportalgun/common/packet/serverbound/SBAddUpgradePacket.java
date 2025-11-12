package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.item.UpgradeItem;
import com.jdolphin.ricksportalgun.common.item.types.UpgradeType;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SBAddUpgradePacket(String id) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBAddUpgradePacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBAddUpgradePacket::id, SBAddUpgradePacket::new);
    public static final Type<SBAddUpgradePacket> ID = new Type<>(PGHelper.id("add_upgrade"));

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            UpgradeType type = PGUpgradeTypes.getFromId(id);
            List<ItemStack> stacks = player.getInventory().items;
            ItemStack upgrade = ItemStack.EMPTY;
            for (ItemStack itemStack : stacks) {
                if (itemStack.getItem() instanceof UpgradeItem upgradeItem) {
                    if (upgradeItem.getUpgradeType().getId().equals(id)) {
                        upgrade = itemStack;
                        break;
                    }
                }
            }
            if (upgrade != ItemStack.EMPTY || player.isCreative()) {
                if (PGItems.getItemFromUpgradeType(type).onApply(player, stack).equals(InteractionResult.SUCCESS)) {
                    upgrade.shrink(1);
                }
            }
        });
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}