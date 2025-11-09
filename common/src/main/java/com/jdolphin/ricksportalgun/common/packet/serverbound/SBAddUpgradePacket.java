package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.item.upgrade.UpgradeItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.types.UpgradeType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SBAddUpgradePacket(String id) implements PGServerPayload {

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
                if (PGItems.getItemFromType(type).onApply(player, stack).equals(InteractionResult.SUCCESS)) {
                    upgrade.shrink(1);
                }
            }
        });
    }

    public static SBAddUpgradePacket decode(FriendlyByteBuf buf) {
        return new SBAddUpgradePacket(buf.readUtf());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(id);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("upgrade");
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }
}
