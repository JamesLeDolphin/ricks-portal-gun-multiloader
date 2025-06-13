package com.jdolphin.ricksportalgun.common.item.upgrade;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class UpgradeItem extends Item {
    protected final BiConsumer<ItemStack, PortalGunItem> consumer;

    public UpgradeItem(Properties properties, BiConsumer<ItemStack, PortalGunItem> consumer) {
        super(properties);
        this.consumer = consumer;
    }

    public InteractionResult applyUpgrade(Player player, ItemStack stack, PortalGunItem item) {
        this.consumer.accept(stack, item);
        return InteractionResult.SUCCESS;
    }
}
