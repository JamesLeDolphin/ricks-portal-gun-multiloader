package com.jdolphin.ricksportalgun.common.item.upgrade;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class SimpleUpgradeItem extends AbstractUpgradeItem {
    protected final BiConsumer<ItemStack, PortalGunItem> consumer;

    public SimpleUpgradeItem(Properties properties, BiConsumer<ItemStack, PortalGunItem> consumer) {
        super(properties);
        this.consumer = consumer;
    }

    public InteractionResult applyUpgrade(Player player, ItemStack stack, PortalGunItem item) {
        this.consumer.accept(stack, item);
        PGHelper.sendSuccessMsg(player, Component.translatable("notice.ricksportalgun.upgrade"));
        return InteractionResult.SUCCESS;
    }
}
